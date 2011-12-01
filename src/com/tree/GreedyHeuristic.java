package com.tree;

import java.util.Iterator;
import java.util.Vector;

import com.ATP.ATPedge;
import com.ATP.ATPgraph;
import com.ATP.ATPstate;
import com.ATP.ATPvehicle;
import com.ATP.ATPmove;
import com.special.dijkstra.City;
import com.special.dijkstra.DenseRoutesMap;
import com.special.dijkstra.DijkstraEngine;

public class GreedyHeuristic implements HeuristicFunction{
	private int m_agentID;
	private int m_goal;
	private DijkstraEngine m_dijkstra;
	private DenseRoutesMap map;

	public GreedyHeuristic(int agentID, int goal){
		this.m_agentID = agentID;
		this.m_goal = goal;
		this.m_dijkstra = null;
		this.map = null;
	}

	private double[] findBestVehicles(Iterator<ATPvehicle> it){
		double bestSpeed = 0.0;
		double bestFloatSpeed = 0.0;
		while (it.hasNext()){
			ATPvehicle vehicle = it.next();
			if ((vehicle.getEff() > 0) && (vehicle.speedFlooded() > bestFloatSpeed)){
				bestFloatSpeed = vehicle.speedFlooded();
			}
			if (vehicle.speedUnflooded() > bestSpeed){
				bestSpeed = vehicle.speedUnflooded();
			}
		}
		return new double[]{bestSpeed, bestFloatSpeed};
	}

	private void updateEdgesWeightOnMap(DenseRoutesMap map, Iterator<ATPedge> it, double bestSpeed, double bestFloatSpeed){
		while(it.hasNext()){
			ATPedge edge = it.next();
			double weight = 0;
			if (edge.isFlooded() && (bestFloatSpeed != 0.0)){
				weight = edge.getWeight() / bestFloatSpeed;
			}
			else if (!edge.isFlooded() && (bestSpeed != 0)){
				weight = edge.getWeight() / bestSpeed;
			}
			map.addDirectRoute(City.valueOf(edge.getSource()), City.valueOf(edge.getTarget()), weight);
		}
	}


	public void initHeuristic(int source, Vector<ATPmove> availableMoves){
		this.map = new DenseRoutesMap(ATPgraph.instance().verticesNum());
		//for every possible move, if it has the lower price, update it
		Iterator<ATPmove> it = availableMoves.iterator();
		while(it.hasNext()){
			ATPmove move = it.next();
			ATPedge edge = ATPgraph.instance().getEdge(source, move.getTarget());
			ATPvehicle vehicle = ATPgraph.instance().getVehicle(move.getVehicleID());
			double weight = this.map.getDistance(City.valueOf(source), City.valueOf(move.getTarget()));

			if (edge.isFlooded() && (vehicle.getEff()>0) && (edge.getWeight() / vehicle.speedFlooded() < weight))
				this.map.addDirectRoute(City.valueOf(source), City.valueOf(move.getTarget()), edge.getWeight() / vehicle.speedFlooded());
			else if (!edge.isFlooded() && (edge.getWeight() / vehicle.speedUnflooded() < weight))
				this.map.addDirectRoute(City.valueOf(source), City.valueOf(move.getTarget()), edge.getWeight() / vehicle.speedUnflooded());
		}
		//update weights of other edges with the assumption that all vehicle are-
		//available in every vertex, that is why we'll choose the best vehicles
		double[] speed = this.findBestVehicles(ATPgraph.instance().vehiclesIter());
		for(int i = 0; i < ATPgraph.instance().verticesNum(); i++){
			this.updateEdgesWeightOnMap(this.map, ATPgraph.instance().neighboursOfIterate(i), speed[0], speed[1]);
		}

		for(int idist = 0; idist!=map.distances.length; ++idist) {
			for(int jdist = 0; jdist!=map.distances[idist].length; ++jdist) {
				System.out.print(" "+map.distances[idist][jdist]);
			}
			System.out.println();
		}
		this.m_dijkstra = new DijkstraEngine(this.map);
		this.m_dijkstra.execute(City.valueOf(source), City.valueOf(this.m_goal));
		System.out.println("SHORTEST PATH:");
		for (City city = City.valueOf(this.m_goal); city != null; city = this.m_dijkstra.getPredecessor(city))
		{
			System.out.print(" "+city.getName());
		}
		System.out.println();
		System.out.println("SHORTEST DISTANCES:");
		for (int i = 0; i!=map.distances.length; ++i) {
			System.out.println("distance("+source+", "+i+")="+this.m_dijkstra.getShortestDistance(City.valueOf(i)));
		}
	}

	@Override
	public double evaluate(ATPstate state) {
		int pos = state.getAgentPosition(this.m_agentID);
		return this.m_dijkstra.getShortestDistance(City.valueOf(pos));
	}

}
