package com.ATP;

import java.util.Iterator;
import java.util.Vector;

public class ATPstate {
	/* agents_positions[i] = The number of vertex where the i agent is. */
	Vector<AgentState> agents_state;
	/* vehicles_positions[i] = All The vehicles at vertex number i. */
	Vector<Vector<ATPvehicle>> vehicles_positions;
	/* vehicle_owner[i] = the ID of the agent who currently owns the i vehicle. */
	Vector<Integer> vehicle_owner;

	public ATPstate(int n){
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
		this.vehicles_positions.get(to).add(veh);
	}

	public void moveVeh(int from, int to, int id){
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

	private void removeVehicle(int from, ATPvehicle veh){
		this.vehicles_positions.get(from).remove(veh);
	}

	/**
	 * make agent's move
	 * @pre move is legal
	 * @param id Agent's id
	 * @param target Vertex to move to
	 * @param vehicle Agent's vehicle
	 * @param cost Cost of move
	 * @return move cost
	 */
	public void moveAgent(int id, ATPmove move, double cost){
		double price = 0.0;

		ATPedge edge = ATPgraph.instance().getEdge(this.getAgentPosition(id), move.getTarget());
		ATPvehicle vehicle = ATPgraph.

		int currentVeh = this.agentVehicle(id);
		//if agent switch vehicle, release the old one
		if ((currentVeh != -1) && (currentVeh != move.getVehicleID())){
			this.setVehicleAvailable(currentVeh, id);
		}
		//change vehicle if needed
		if (currentVeh != move.getVehicleID()){
			price += ATPgraph.instance().getTswitch();
			this.setAgentVehicle(id, move.getVehicleID());
		}
		//vehicle moves with agent
		this.moveVeh(this.getAgentPosition(id), move.getTarget(), move.getVehicleID());//update vehicle's position
		this.setAgentVehicle(id, move.getVehicleID());//update vehicle's owner

		this.agents_state.get(id).moveAgent(move.getTarget(), move.getVehicleID(), price);//update agent's state
		//update agent's inner state
		Agent agent = ATPgraph.instance().getAgentByID(id);
		agent.setPosition(move.getTarget());
		agent.setVehicleID(move.getVehicleID());
	}

	/**
	 * initiate agents' state,
	 * @param vec agents' states
	 */
	public void setAgents(Vector<AgentState> vec){
		this.agents_state = vec;
	}

	/**
	 * set the position of agent with id agent_id to be pos.
	 * @param agent_id
	 * @param pos
	 */
	public void setAgentPosition(int agent_id, int pos)
	{
		this.agents_state.get(agent_id).setAgentPosition(pos);
	}

	/**
	 * @param id Agent's id.
	 * @return the agent's position in the state.
	 */
	public int getAgentPosition(int id)
	{
		return this.agents_state.get(id).getAgentPosition();
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
	public void setAgentVehicle(int agentID, int vehID)
	{
		this.vehicle_owner.set(vehID, agentID);
		this.agents_state.get(agentID).setAgentVehicleId(vehID);
	}

	/**
	 * sets the vehicle to be available
	 * @param vehID vehicle id
	 * @param currentOwner current owner of the vehicle
	 */
	public void setVehicleAvailable(int vehID, int currentOwner)
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
}
