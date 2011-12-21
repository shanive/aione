package com.ATP;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Vector;

import com.game.GameHeuristic;
import com.game.MinimaxAgent;
import com.tree.AstarAgent;
import com.tree.GreedySearch;
import com.tree.RTAstar;
import com.tree.SearchAgent;


public class ATPenv {
	Vector<Vector<ATPmove>> agents_moves;
	ATPstate state;
	boolean horizon;
	int T;
	final int F = 100; //penalty
	// configurable parameters, see main
	static boolean batch = false, print_state = false;

	static final String state_html = "state.html";

	public ATPenv(String file) {
		this.horizon = true; //get number of moves per agent from user
		this.T = 0; // initial value of horizon
		initEnv(file);
		this.agents_moves = new Vector<Vector<ATPmove>>();
		for (int i = 0; i < ATPgraph.instance().getAgentsNum(); i++)
		{
			this.agents_moves.add(new Vector<ATPmove>());
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

			//read user input.
			BufferedReader userInputReader = new BufferedReader(
					new InputStreamReader(System.in));
			System.out.println("Enter Tswitch:");
			double tswitch = Double.parseDouble(userInputReader.readLine());
			if (this.horizon){
				System.out.println("Enter horizon T (moves per agent):");
				this.T = Integer.parseInt(userInputReader.readLine());
			}
			ATPgraph.initiate(filterEdges(allEdges, maxV), vehicles, tswitch);

			Vector<Agent> agents_list = new Vector<Agent>();


			System.out.println("Enter number of agents:");
			int agentsNum = Integer.parseInt(userInputReader.readLine());
			Vector<AgentState> agents_state = new Vector<AgentState>();
			int agentid = 0;
			boolean game = false;
			System.out.println("Play Game? (yes/no)");
			String answer = "";
			while (true){
				answer = userInputReader.readLine();
				if (answer.compareTo("yes") == 0){
					game = true;
					break;
				}
				else if (answer.compareTo("no") == 0)
					break;
				else
					System.out.println("Enter yes/no:");

			}
			if (game){
				System.out.println("Enter: <game-type> (zero-sum/non-zero-sum/cooperative)"+
						"<cutoff> <initial-1> <goal-1> <initial-2> <goal-2>");
				String[] gameInfo = userInputReader.readLine().split(" ");
				agentid = 2;
				int cutoff = Integer.parseInt(gameInfo[1]);
				int initial1 = Integer.parseInt(gameInfo[2]);
				int goal1 = Integer.parseInt(gameInfo[3]);
				int initial2 = Integer.parseInt(gameInfo[4]);
				int goal2 = Integer.parseInt(gameInfo[5]);
				if (gameInfo[0].compareTo("zero-sum")==0){
					MinimaxAgent firstagent = new MinimaxAgent(0, initial1, goal1,
																	1, goal2, cutoff);
					MinimaxAgent secondagent = new MinimaxAgent(1, initial2, goal2,
																	0, goal1, cutoff);
					agents_list.add(firstagent);
					agents_state.add(new AgentState(initial1));
					agents_list.add(secondagent);
					agents_state.add(new AgentState(initial2));
				}

			}


			for (int i = agentid; i < agentsNum ; i++)
			{
				System.out.println("Enter: initial vertex, " +
				"goal vertex and type(human/speed/greedy/greedy-search/A*/RTA*)");
				String[] inputs = userInputReader.readLine().split(" ");
				if (inputs.length != 3) error();
				agents_state.addElement(new AgentState(Integer.parseInt(inputs[0])));
				Agent agent = this.createAgent(userInputReader,
											   inputs[2], i,
											   Integer.parseInt(inputs[0]),
											   Integer.parseInt(inputs[1]));
				agents_list.add(agent);
			}
			this.state.setAgents(agents_state);
			ATPgraph.instance().setAgents(agents_list);
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

	/*
	 * run ply of given Agent.
	 * return true if agent reached goal or got stuck.
	 */
	private boolean runPly(Agent agent){
		if (agent.reachedGoal()){
			return true;
		}
		agent.repeatedStates.add(new ATPstate(this.state));
		ATPmove move = agent.nextMove(this.state);
		if (move != null){//no available move
			if (Debug.instance().isDebugOn()){
				System.out.println("Agent's move:"+move);
			}
			boolean legalMove = this.state.agentMove(agent, move);
			//update agent's score
			this.agents_moves.get(agent.getID()).add(move);
			agent.setPosition(move.getTarget());
			agent.setVehicleID(move.getVehicleID());
		} else if(batch && ATPgraph.instance().getAgentsNum()==1) {
			return true; //agent is stuck
		}
		else{//release the vehicle that the agent have
			int vehicle = this.state.agentVehicle(agent.getID());
			if (vehicle != -1){
				this.state.setVehicleAvailable(vehicle, agent.getID());
			}
		}
		return false;
	}

	private void printGameInfo(){
		System.out.println("Agents' Moves:");
		for(int i = 0; i < ATPgraph.instance().getAgentsNum();i++){
			System.out.print("Agent "+i+": ");
			Iterator<ATPmove> moves = this.agents_moves.get(i).iterator();
			while (moves.hasNext()){
				System.out.print(moves.next()+", ");
			}
			System.out.println("");
			if (ATPgraph.instance().getAgentByID(i) instanceof SearchAgent){
				SearchAgent agent = (SearchAgent)ATPgraph.instance().getAgentByID(i);
				double S = this.state.getAgentScore(i),
					   E = agent.expandCount;
				System.out.println("P(0.001)="+(0.001*S+E)+" P(1) = "+(1.0*S+E)+
						" P(100.0)="+(100.0*S+E)+" P(10000.0)="+(10000.0*S+E));
			}
		}
	}

	public void RunEnv() throws java.io.IOException
	{
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in));
		boolean gameover = false;
		boolean all_done = false;
		int movesPerAgent = 1;

		this.state.htmlPrinterHeader(state_html);
		this.state.htmlPrinter(state_html);
		while (!(gameover || all_done))
		{
			Iterator<Agent> it = ATPgraph.instance().agentsIterator();
			all_done = true;
			while(it.hasNext()){
				Agent agent = it.next();
				all_done = all_done & this.runPly(agent); // must be & so every agent will play
			}
			this.state.htmlPrinter(state_html);
			if(print_state) {
				this.state.printer();
			}
			else {// still print the current node
				for(int i = 0; i < ATPgraph.instance().getAgentsNum(); i++){
					System.out.println("Agent "+i+" position: "+ this.state.getAgentPosition(i));
				}
			}
			this.printScores();
			if(!this.horizon && !batch) {
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
			if (this.horizon && (movesPerAgent == this.T)){
				gameover = true;
			}
			movesPerAgent++;
		}
		//add penalty if necessary
		for (int i = 0; i < ATPgraph.instance().getAgentsNum(); i++){
			if (!ATPgraph.instance().getAgentByID(i).reachedGoal())
				this.state.agentPenalty(i, this.F);
		}

		this.printGameInfo();
		System.out.println("bye bye.");
	}

	private void printScores(){
		System.out.println("Agents' Scores:");
		for (int agent = 0; agent < ATPgraph.instance().getAgentsNum(); agent++)
		{
			System.out.println("Agent "+agent+": steps: "+this.state.getAgentStepsNum(agent)+
					", time: "+ this.state.getAgentScore(agent));
			agent++;
		}
	}

	public String toString(){
		return ATPgraph.instance().toString();
	}

	public static void main(String[] args) throws java.io.IOException {
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
