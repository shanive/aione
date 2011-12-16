package com.ATP;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Vector;
import com.tree.AstarAgent;
import com.tree.GreedySearch;
import com.tree.RTAstar;
import com.tree.SearchAgent;


public class ATPenv {
	Vector<Vector<ATPmove>> agents_moves;
	Vector<Agent> agents_list;
	Vector<AgentScore> agents_scores;
	ATPstate state;

	// configurable parameters, see main
	static boolean batch = false, print_state = false;

	public ATPenv(String file) {
		initEnv(file);
		this.agents_scores = new Vector<AgentScore>();
		this.agents_moves = new Vector<Vector<ATPmove>>();
		for (int i = 0; i < this.agents_list.size(); i++)
		{
			this.agents_moves.add(new Vector<ATPmove>());
			this.agents_scores.add(new AgentScore());
		}
	}

	private void error(){
		System.out.println("Wrong input!");
		System.exit(1);
	}

	private Agent createAgent(BufferedReader userInputReader,
							  String name, int id, int initial, int goal){
		try {
			if (name.compareTo("human") == 0)
				return new Human(id, initial, goal);
			else if (name.compareTo("speed") == 0)
				return new SpeedNutAutomaton(id, initial, goal);
			else if (name.compareTo("greedy") == 0)
				return new Greedy(id, initial, goal);
			else if (name.compareTo("greedy-search") == 0)
				return new GreedySearch(id, initial, goal);
			else if (name.compareTo("A*") == 0)
				return new AstarAgent(id, initial, goal);
			else if (name.compareTo("RTA*") == 0){
				System.out.println("Enter RTA* max depth:");
				int depth;
				depth = Integer.parseInt(userInputReader.readLine());
				return new RTAstar(id, initial, goal, depth);
			}
			else error();
		} catch (NumberFormatException e) {
			System.out.println("Failed to read input");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Failed to read input");
			e.printStackTrace();
		}
		return null;
	}

	public void initEnv(String addressofFile){
		try {
			BufferedReader br = new BufferedReader(new FileReader(addressofFile));
			Vector<ATPedge> allEdges = new Vector<ATPedge>();
			Vector<ATPvehicle> vehicles = new Vector<ATPvehicle>();
			String line;
			int verticesNum = -1;
			int maxV = -1;
			int vehicleCount = 0;

			while((line = br.readLine()) != null) {
				String[] tokens = line.split(" ");
				if(tokens[0].compareTo("#V") == 0) {
					verticesNum = Integer.parseInt(tokens[1]);
					break;
				}
			}
			this.state = new ATPstate(verticesNum);
			maxV = verticesNum;

			// parse list of edges and vehices, #V means vehicle now
			while((line = br.readLine()) != null){
				String[] Tokens = line.split(" ");
				if (Tokens[0].compareTo("#E") == 0){
					int source = Integer.parseInt(Tokens[1]);
					int target = Integer.parseInt(Tokens[2]);
					if (source>maxV) maxV = source;
					if (target>maxV) maxV = target;
					int w = Integer.parseInt(Tokens[3].substring(1));
					boolean f = true;
					if (Tokens[4].compareTo("C") == 0) f = false;

					allEdges.add(new ATPedge(source, target, w, f));
					allEdges.add(new ATPedge(target, source, w, f));
				}
				else if (Tokens[0].compareTo("#V") == 0){
					int to = Integer.parseInt(Tokens[1]);
					int vel = Integer.parseInt(Tokens[2]);
					double eff = Double.parseDouble(Tokens[3]);
					ATPvehicle vehicle = new ATPvehicle(vel, eff, vehicleCount);
					this.state.addVehicle(to, vehicle);
					vehicles.add(vehicle);
					vehicleCount +=1;
				}
			}
			this.state.initVehiclesOwners(vehicleCount);
			BufferedReader userInputReader = new BufferedReader(
					new InputStreamReader(System.in));
			System.out.println("Enter Tswitch:");
			double tswitch = Double.parseDouble(userInputReader.readLine());


			ATPgraph.initiate(filterEdges(allEdges, maxV), vehicles, tswitch);
			this.agents_list = new Vector<Agent>();

			System.out.println("Enter number of agents:");
			int agentsNum = Integer.parseInt(userInputReader.readLine());
			Vector<Integer> agents_positions = new Vector<Integer>();
			System.out.println("For each agent enter: initial vertex, " +
					"goal vertex and type(human/speed/greedy/greedy-search/a*)");
			for (int i = 0; i < agentsNum ; i++)
			{
				String[] inputs = userInputReader.readLine().split(" ");
				if (inputs.length != 3) error();
				agents_positions.addElement(Integer.parseInt(inputs[0]));
				Agent agent = this.createAgent(userInputReader,
											   inputs[2], i,
											   Integer.parseInt(inputs[0]),
											   Integer.parseInt(inputs[1]));
				this.agents_list.add(agent);
			}

			this.state.setAgents(agents_positions);

			br.close();

		} catch (FileNotFoundException e) {
			System.err.println("Error opening file");
			//e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error reading file");
			e.printStackTrace();
		}
	}

	/**
	 * Takes all the ATPedges and sorts them in the graph (Adjacency list).
	 *
	 * @param vec a Vector containing all the edges
	 * @param max the maximum vertex number
	 */
	private Vector<Vector<ATPedge>> filterEdges(Vector<ATPedge> vec, int max){
		Vector<Vector<ATPedge>> res = new Vector<Vector<ATPedge>>();
		for(int i=0;i<=max;i++){
			Iterator<ATPedge> it = vec.iterator();
			ATPedge e;
			Vector<ATPedge> tmpVec = new Vector<ATPedge>();
			while(it.hasNext()){
				e = it.next();
				if (e.getSource() == i) tmpVec.add(e);
			}

			res.add(tmpVec);
		}

		return res;
	}


	//return price
	private double executeMove(Agent agent, ATPmove move)
	{
		//default large values to discorage illegal moves
		double weight = 1000;
		double vel = 1.0;
		int vehID = move.getVehicleID();
		int agent_pos = this.state.getAgentPosition(agent.getID());
		ATPvehicle vehicle = this.state.getVehicleAt(move.getVehicleID(), agent_pos);
		if (vehicle == null){
			System.err.println("No Such Vehicle");
			System.out.println("Agent Get Default Price");
			return weight / vel;
		}
		vel = vehicle.getVel();
		if (!this.state.isVehicleAvailable(vehID) && (this.state.getVehicleOwner(vehID)!=agent.getID())){
			System.err.println("Unavailable Vehicle");
			System.out.println("Agent Get Default Price");
			return weight / vel;
		}
		ATPedge edge = ATPgraph.instance().getEdge(agent_pos, move.getTarget());
		if (edge ==  null){
			System.err.println("No Such Edge");
			System.out.println("Agent Get Default Price");
			return weight / vel;
		}
		weight = edge.getWeight();
		if (edge.isFlooded() && (vehicle.getEff() == 0)){
			System.out.println("Incompatible Vehicle");
			return (weight / vel);
		}
		double price = 0.0;
		double speed = 0.0;
		if (edge.isFlooded())
			speed = vehicle.speedFlooded();
		else
			speed = vehicle.speedUnflooded();
		int currentVeh = this.state.agentVehicle(agent.getID());
		//if agent switch vehicle, release the old one
		if ((currentVeh != -1) && (currentVeh != vehicle.getVehicleId())){
			this.state.setVehicleAvailable(currentVeh);
		}
		//change vehicle if needed
		if (currentVeh != vehicle.getVehicleId()){
			price += ATPgraph.instance().getTswitch();
			this.state.setVehicleOwner(vehicle.getVehicleId(), agent.getID());
		}
		//even if agent didn't changed vehicle, it moves with him
		this.state.moveVeh(agent_pos, move.getTarget(), move.getVehicleID());//update vehicle's position
		vehicle.setAgent(agent.getID());//update vehicle's owner//TODO redundant?

		this.state.setAgentPosition(agent.getID(), move.getTarget());//update agent's position on state
		//update agent's inner state
		agent.setPosition(move.getTarget());
		agent.setVehicleID(move.getVehicleID());//TODO redundant?
		price += edge.getWeight() / speed;
		return price;
	}

	public void RunEnv()
	{
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in));
		boolean gameover = false;
		boolean all_done = false;
		while (!(gameover || all_done))
		{
			Iterator<Agent> it = this.agents_list.iterator();
			all_done = true;
			while(it.hasNext()){
				Agent agent = it.next();
				if (agent.reachedGoal()){
					continue;
				} else {
					all_done = false;
				}
				agent.repeatedStates.add(new ATPstate(this.state));
				ATPmove move = agent.nextMove(this.state);
				if (move != null){//no available move
					if (Debug.instance().isDebugOn()){
						System.out.println("Agent's move:"+move);
					}
					double price = this.executeMove(agent, move);
					//update agent's score
					AgentScore score = this.agents_scores.get(agent.getID());
					score.addStep();
					score.addTime(price);
					this.agents_moves.get(agent.getID()).add(move);
				} else if(batch && this.agents_list.size()==1) {
					gameover = true;
					break;
				}
				else{//release the vehicle that the agent have
					int vehicle = this.state.agentVehicle(agent.getID());
					if (vehicle != -1){
						this.state.setVehicleAvailable(vehicle);
					}
				}
			}
			if(print_state)
				this.state.printer();
			else {// still print the current node
				for(int i = 0; i < this.agents_list.size(); i++){
					System.out.println("Agent "+i+" position: "+ this.state.getAgentPosition(i));
				}
			}
			this.printScores();
			if(!batch) {
				System.out.println("Continue Simulation? y/n");
				while(true) {
						try {
							String input = reader.readLine();
							if (input.compareTo("n") == 0) {
								gameover = true;
								break;
							} else if (input.compareTo("y") == 0) {
								break;
							} else {
								System.err.println("Wrong Input.");
							}
						} catch (IOException e) {
							System.err.println("Simulation failed reading input.");
							e.printStackTrace();
						}
				}
			}
		}

		System.out.println("Agents' Moves:");
		for(int i = 0; i < this.agents_list.size();i++){
			System.out.print("Agent "+i+": ");
			Iterator<ATPmove> moves = this.agents_moves.get(i).iterator();
			while (moves.hasNext()){
				System.out.print(moves.next()+", ");
			}
			System.out.println("");
			if (this.agents_list.get(i) instanceof SearchAgent){
				SearchAgent agent = (SearchAgent)this.agents_list.get(i);
				double S = this.agents_scores.get(i).getTime(),
					   T = agent.expandCount;
				System.out.println("P(0.001)="+(0.001*S+T)+" P(1) = "+(1.0*S+T)+" P(100.0)="+(100.0*S+T)+" P(10000.0)="+(10000.0*S+T));
			}
		}

		System.out.println("bye bye.");
	}

	private void printScores(){
		Iterator<AgentScore> it = this.agents_scores.iterator();
		System.out.println("Agents' Scores:");
		int agent = 0;
		while (it.hasNext())
		{
			AgentScore score = it.next();
			System.out.println("Agent "+agent+": steps: "+score.getSteps()+
					", time: "+ score.getTime());
			agent++;
		}
	}

	public String toString(){
		return ATPgraph.instance().toString();
	}

	public static void main(String[] args) {
		if (args.length == 0){
			System.out.println("Usage: [-d|-b|-s <value>] <input-file>");
			System.exit(1);
		}
		boolean debug = false;
		int iarg = 0;
		while(iarg != args.length) {
			if(args[iarg].compareTo("-d")==0) {
				debug = true;
			} else if(args[iarg].compareTo("-b")==0) {
				batch = true;
			} else if(args[iarg].compareTo("-s")==0) {
				print_state = true;
			} else break;
			++iarg;
		}
		Debug.initDebug(debug);
		ATPenv env = new ATPenv(args[iarg]);
		env.RunEnv();
	}

}
