package com.ATP;

import java.util.Iterator;
import java.util.Vector;

public class ATPstate {
	/* agents_positions[i] = The number of vertex where the i agent is. */ 
	Vector<Integer> agents_positions;
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
		this.agents_positions = (Vector<Integer>) state.agents_positions.clone();
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
	
	public int agentVehicle(int agentId)
	{
		for(int i = 0; i< this.vehicle_owner.size(); i++)
		{
			if (this.vehicle_owner.get(i) == agentId)
				return i;
		}
		return -1;
	}
	
	private void removeVehicle(int from, ATPvehicle veh){
		this.vehicles_positions.get(from).remove(veh);
	}
	
	public void setAgents(Vector<Integer> vec){
		this.agents_positions = vec;
	}
	
	public void setAgentPosition(int agent_id, int pos)
	{
		this.agents_positions.set(agent_id, pos);
	}
	
	/**
	 * @param id Agent's id.
	 * @return the agent's position in the state.
	 */
	public int getAgentPosition(int id)
	{
		return this.agents_positions.get(id);
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
	
	public Vector<ATPvehicle> getVehiclesAt(int vertex)
	{
		return this.vehicles_positions.get(vertex);
	}
	
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
	
	public int getVehicleOwner(int vehID)
	{
		return this.vehicle_owner.get(vehID);
	}
	
	public void setVehicleOwner(int vehID, int agentID)
	{
		this.vehicle_owner.set(vehID, agentID);
	}
	
	public void setVehicleAvailable(int vehID)
	{
		this.vehicle_owner.set(vehID, -1);
	}
	
	public boolean isVehicleAvailable(int vehID){
		return (this.vehicle_owner.get(vehID) == -1);
	}
	
	public boolean isEqual(ATPstate state){
		//check agents positions
		if (!this.agents_positions.equals(state.agents_positions))
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
	
	public void printer()
	{
		System.out.println("Current State:\nPlayers Positions:");
		for(int i = 0; i < this.agents_positions.size(); i++)
		{
			System.out.println("Agent "+i+": vertex "+this.agents_positions.get(i)+", ");
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
				String owner = "none";
				if (this.vehicle_owner.get(vehicle.getVehicleId()) != -1)
					owner = Integer.toString(this.vehicle_owner.get(vehicle.getVehicleId()));
				System.out.print("Vehicle ID: "+vehicle.getVehicleId()+
						", Owner ID: "+owner+", ");
			}
			System.out.println("");
		}
	}
}
