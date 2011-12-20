package com.ATP;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

public class ATPstate {
	/* highest vertex index */
    int nv;
	/* agents_positions[i] = The number of vertex where the i agent is. */
	Vector<AgentState> agents_state;
	/* vehicles_positions[i] = All The vehicles at vertex number i. */
	Vector<Vector<ATPvehicle>> vehicles_positions;
	/* vehicle_owner[i] = the ID of the agent who currently owns the i vehicle. */
	Vector<Integer> vehicle_owner;

	public ATPstate(int n){
		this.nv = n;
		this.vehicles_positions = new Vector<Vector<ATPvehicle>>();
		for(int i=0; i<=n; i++)
		{
			this.vehicles_positions.add(new Vector<ATPvehicle>());
		}
	}

	public ATPstate(ATPstate state){
		this.agents_state = new Vector<AgentState>();
		Iterator<AgentState> st = state.agents_state.iterator();
		while (st.hasNext()){
			this.agents_state.add(new AgentState(st.next()));
		}
		this.vehicle_owner = (Vector<Integer>) state.vehicle_owner.clone();
		this.vehicles_positions = new Vector<Vector<ATPvehicle>>();
		Iterator<Vector<ATPvehicle>> it = state.vehicles_positions.iterator();
		while (it.hasNext()){
			Vector<ATPvehicle> vehicles = new Vector<ATPvehicle>();
			Iterator<ATPvehicle> veh = it.next().iterator();
			while (veh.hasNext()){
				vehicles.add((ATPvehicle)veh.next().clone());
			}
			this.vehicles_positions.add(vehicles);
		}
	}

	public void initVehiclesOwners(int vehNum)
	{
		this.vehicle_owner = new Vector<Integer>();
		for(int i=0; i < vehNum; i++ )
		{
			this.vehicle_owner.add(-1);
		}
	}

	public void addVehicle(int to, ATPvehicle veh){
		Vector<ATPvehicle> vehicles = this.vehicles_positions.get(to);
		vehicles.add(veh);
		//sort vehicles by id. each vehicle has a different id.
		//sort for comparing states.
		Collections.sort(vehicles, new Comparator<ATPvehicle>(){
										public int compare(ATPvehicle o1, ATPvehicle o2){
											return (o1.getVehicleId() - o2.getVehicleId());
										}
		});
	}

	public void setAgents(Vector<AgentState> agents){
		this.agents_state = agents;
	}

	protected void moveVeh(int from, int to, int id){
		ATPvehicle veh = this.getVehicleAt(id, from);
		this.removeVehicle(from, veh);
		this.addVehicle(to, veh);
	}

	/**
	 *
	 * @param agentId
	 * @return the id of the agent's vehicle (-1 if has no vehicle)
	 */
	public int agentVehicle(int agentId)
	{
		return (this.agents_state.get(agentId).getAgentVehicleId());
	}

	protected void removeVehicle(int from, ATPvehicle veh){
		this.vehicles_positions.get(from).remove(veh);
	}

	/**
	 * make agent's move
	 * @param id Agent's id
	 * @param target Vertex to move to
	 * @param vehicle Agent's vehicle
	 * @param cost Cost of move
	 * @return true if move is legal and false otherwise
	 */
	public boolean agentMove(Agent agent, ATPmove move){

		ATPvehicle vehicle = ATPgraph.instance().getVehicle(move.getVehicleID());

		//check if move is legal, if not- return false
		if (vehicle == null){
			System.err.println("No Such Vehicle");
			System.out.println("Agent Get Default Price");
			return false;
		}

		if (!this.isVehicleAvailable(move.getVehicleID()) &&
				(this.getVehicleOwner(move.getVehicleID())!= agent.getID())){
			System.err.println("Unavailable Vehicle");
			System.out.println("Agent Get Default Price");
			return false;
		}
		ATPedge edge = ATPgraph.instance().getEdge(this.getAgentPosition(
				agent.getID()), move.getTarget());

		if (edge ==  null){
			System.err.println("No Such Edge");
			System.out.println("Agent Get Default Price");
			return false;
		}

		if (edge.isFlooded() && (vehicle.getEff() == 0)){
			System.out.println("Incompatible Vehicle");
			return false;
		}
		//move is legal- compute time (cost) and change the state.
		double time = 0.0;
		//calculate time to travers the edge
		if (edge.isFlooded())
			time += (edge.getWeight() / vehicle.speedFlooded());
		else
			time += (edge.getWeight() / vehicle.speedUnflooded());

		int currentVeh = this.agentVehicle(agent.getID());
		//if agent switch vehicle, release the old one
		if ((currentVeh != -1) && (currentVeh != move.getVehicleID())){
			this.setVehicleAvailable(currentVeh, agent.getID());
		}
		//get into new vehicle
		if (currentVeh != move.getVehicleID()){
			time += ATPgraph.instance().getTswitch();
			this.setVehicleOwner(agent.getID(), move.getVehicleID());
		}
		//vehicle moves with agent
		this.moveVeh(this.getAgentPosition(agent.getID()), move.getTarget(), move.getVehicleID());//update vehicle's position
		//update agent's state
		this.agents_state.get(agent.getID()).moveAgent(move.getTarget(), move.getVehicleID(), time);
		//update agent's inner state

		agent.setPosition(move.getTarget());
		agent.setVehicleID(move.getVehicleID());

		return true;
	}

	/**
	 * @param id Agent's id.
	 * @return the agent's position in the state.
	 */
	public int getAgentPosition(int id)
	{
		return this.agents_state.get(id).getAgentPosition();
	}


	public double getAgentScore(int id){
		return this.agents_state.get(id).getAgentScore();
	}

	public int getAgentStepsNum(int id){
		return this.agents_state.get(id).getAgentStepsNum();
	}

	public void agentPenalty(int id, double time){
		this.agents_state.get(id).addTime(time);
	}

	/**
	 *
	 * @param vertex The position.
	 * @return vehicles at a given position.
	 */
	public Iterator<ATPvehicle> iterateVehicleAt(int vertex)
	{
		return this.vehicles_positions.get(vertex).iterator();
	}

	/**
	 *
	 * @param vertex
	 * @return vector of vehicles at a given vertex
	 */
	public Vector<ATPvehicle> getVehiclesAt(int vertex)
	{
		return this.vehicles_positions.get(vertex);
	}

	/**
	 *
	 * @param vehicle
	 * @param vertex
	 * @return vehicle with a given id at a given vertex
	 * @return null if no such vehicle at the given vertex.
	 */
	public ATPvehicle getVehicleAt(int vehicle, int vertex)
	{
		Iterator<ATPvehicle> it = this.iterateVehicleAt(vertex);
		while (it.hasNext()){
			ATPvehicle temp = it.next();
			if (temp.getVehicleId() == vehicle)
				return temp;
		}
		return null;
	}

	/**
	 *
	 * @param vehID vehicle's id
	 * @return id of agent owns this vehicle
	 */
	public int getVehicleOwner(int vehID)
	{
		return this.vehicle_owner.get(vehID);
	}

	/**
	 * set the vehicle id of the agent with a given id
	 * @param agentID agent's is
	 * @param vehID vehicle's id
	 */
	protected void setVehicleOwner(int agentID, int vehID)
	{
		this.vehicle_owner.set(vehID, agentID);
	}

	/**
	 * sets the vehicle to be available
	 * @param vehID vehicle id
	 * @param currentOwner current owner of the vehicle
	 */
	protected void setVehicleAvailable(int vehID, int currentOwner)
	{
		this.vehicle_owner.set(vehID, -1);
		this.agents_state.get(currentOwner).setAgentVehicleId(-1); //agent releases vehicle
	}

	/**
	 *
	 * @param vehID vehicle id
	 * @return true is vehicle available and false else.
	 */
	public boolean isVehicleAvailable(int vehID){
		return (this.vehicle_owner.get(vehID) == -1);
	}


	public Iterator<AgentState> agentsStateIterate(){
		return this.agents_state.iterator();
	}

	/**
	 *
	 * @param state
	 * @return true if this equals to a given state,
	 */
	public boolean isEqual(ATPstate state){
		//check agents positions
		if (!this.agents_state.equals(state.agents_state))
			return false;
		if (!this.vehicle_owner.equals(state.vehicle_owner))
			return false;
		Iterator< Vector<ATPvehicle> > me =
			this.vehicles_positions.iterator();
		Iterator< Vector<ATPvehicle> > other =
			state.vehicles_positions.iterator();
		while (me.hasNext() && other.hasNext())
		{
			if (!me.next().equals(other.next()))
				return false;
		}
		return true;
	}

	/**
	 * prints state info.
	 */
	public void printer()
	{
		System.out.println("Current State:\nPlayers Positions:");
		for(int i = 0; i < this.agents_state.size(); i++)
		{
			AgentState temp = this.agents_state.elementAt(i);
			System.out.println("Agent "+i+": vertex "+temp.getAgentPosition()+
								", vehicle: "+temp.getAgentVehicleId());
		}
		System.out.println("Vehicles Positions:");
		for(int i = 0; i < this.vehicles_positions.size(); i++)
		{
			Iterator<ATPvehicle> init = this.vehicles_positions.get(i).iterator();
			if (!init.hasNext()) continue;
			System.out.print("Vertex "+i+": ");
			while (init.hasNext())
			{
				ATPvehicle vehicle = init.next();
				System.out.print("Vehicle ID: "+vehicle.getVehicleId());
			}
			System.out.println("");
		}
	}

	public void htmlPrinterHeader(String htmlname) throws java.io.IOException {
		java.io.PrintStream out = new java.io.PrintStream(new java.io.FileOutputStream(htmlname), false);

		out.print("<html><head><title>game state</title>\n");
		out.print("<style type=\"text/css\">\n");
		out.print("table.state {border-collapse: collapse; }\n");
		out.print("table.state tr.vertices { color: white; background-color: #333333;}\n");
		out.print("table.state th, table.state td { border: thin solid black ; }\n");
		out.print("table.state tr.vehicles { font-style: italic; color: #333333;}\n");
		out.print("</style></head><body>\n\n");

		out.close();
	}

    /**
	 * prints state info to an html file
	 */
	public void htmlPrinter(String htmlname) throws java.io.IOException {
		java.io.PrintStream out = new java.io.PrintStream(new java.io.FileOutputStream(htmlname, true));

		out.print("<table class=\"state\">\n");
		out.print("  <tr class=\"vertices\"><th>Vertices:</th>");
		for(int iv = 1; iv <= nv; ++iv) {
			out.print("<th>"+iv+"</th>");
		}
		out.print("</tr>\n");
		for(int ia = 0; ia < this.agents_state.size(); ia++)
		{
			AgentState as = this.agents_state.elementAt(ia);
			out.print("  <tr class=\"agent\"><td>Agent " + ia + "</td>");
			for(int iv = 1; iv<=nv; ++iv) {
				if(as.getAgentPosition()==iv)
					out.print("<td>"+as.getAgentVehicleId()+"</td>");
				else
					out.print("<td>&nbsp;</td>");
			}
			out.print("</tr>\n");
		}

		out.print("  <tr class=\"vehicles\"><td>Vehicles</td>");
		for(int i = 1; i < this.vehicles_positions.size(); i++)
		{
			out.print("<td>");
			Iterator<ATPvehicle> init = this.vehicles_positions.get(i).iterator();
			while (init.hasNext())
			{
				ATPvehicle vehicle = init.next();
				out.print(vehicle.getVehicleId()+" ");
			}
			out.print("</td>");
		}
		out.print("</tr>\n");
		out.print("</table>\n");
		out.print("<p/>");
		out.close();
	}
}
