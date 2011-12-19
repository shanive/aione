package com.ATP;

import java.util.Iterator;
import java.util.Vector;

public class ATPgraph {
	private Vector<ATPvehicle> vehicles;
	private Vector<Agent> agents;
	private double tswitch;
	private int vertics;
	private Vector<Vector<ATPedge>> graph; //there is a possibility for a vector to be empty.
									//if all vertex indexes from min to max were present
									//in the file then will be no empty vector.
	private static ATPgraph _instance = null;

	private ATPgraph(Vector<Vector<ATPedge>> a_graph, Vector<ATPvehicle> vehicleList, 
			double timeToSwitch)
	{
		this.vehicles = vehicleList;
		this.tswitch = timeToSwitch;
		this.graph = a_graph;
		this.vertics = a_graph.size();
	}


	public static void initiate(Vector<Vector<ATPedge>> a_graph, Vector<ATPvehicle> vehicleList,
								 double timeToSwitch)
	{
		if(_instance == null)
			_instance = new ATPgraph(a_graph, vehicleList, timeToSwitch);
	}

	public void setAgents(Vector<Agent> agentsList){
		this.agents = agentsList;
	}

	public double getTswitch()
	{
		return this.tswitch;
	}

	public int getAgentsNum(){
		return this.agents.size();
	}

	public Agent getAgentByID(int id){
		if (this.agents == null)
			return null;
		return this.agents.get(id);
	}

	public Iterator<Agent> agentsIterator(){
		return this.agents.iterator();
	}

	public Iterator<ATPvehicle> vehiclesIter(){
		return this.vehicles.iterator();
	}

	public ATPedge getEdge(int source, int target)
	{
		Iterator<ATPedge> it = this.neighboursOfIterate(source);
		while (it.hasNext())
		{
			ATPedge edge = it.next();
			if (edge.getTarget() == target)
				return edge;
		}
		return null;
	}

	public ATPvehicle getVehicle(int id){
		return this.vehicles.get(id);
	}

	public int verticesNum()
	{
		return this.vertics;
	}

	public static ATPgraph instance()
	{
		return _instance;
	}

	public int vehiclesNum(){
		return this.vehicles.size();
	}

	public Vector<ATPedge> neighboursOf(int v){
		return this.graph.get(v);
	}

	public Iterator<ATPedge> neighboursOfIterate(int v){
		return this.graph.get(v).iterator();
	}

	public Iterator<Vector<ATPedge>> graphIter(){
		return this.graph.iterator();
	}

	public String toString2(){
		return this.graph.toString();
	}
}
