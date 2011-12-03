package com.tree;

import java.util.Iterator;

import com.ATP.ATPedge;
import com.ATP.ATPgraph;
import com.ATP.ATPstate;
import com.ATP.ATPvehicle;
import com.ATP.ATPmove;
import com.ATP.Debug;
import com.special.dijkstra.City;
import com.special.dijkstra.DenseRoutesMap;
import com.special.dijkstra.DijkstraEngine;

public class GreedyHeuristic implements HeuristicFunction{
	private int m_agentID;
	private int m_goal;
	private DijkstraEngine m_dijkstra;

	public GreedyHeuristic(int agentID, int goal){
		this.m_agentID = agentID;
		this.m_goal = goal;
		this.m_dijkstra = null;
		this.initHeuristic();
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


	public void initHeuristic(){

		DenseRoutesMap map = new DenseRoutesMap(ATPgraph.instance().verticesNum());

		double[] best = this.findBestVehicles(ATPgraph.instance().vehiclesIter());
		//update weights
		for(int i = 1; i < ATPgraph.instance().verticesNum(); i++){
			this.updateEdgesWeightOnMap(map, ATPgraph.instance().neighboursOfIterate(i), best[0], best[1]);
		}
		
		if (Debug.instance().isDebugOn()){
			for(int idist = 0; idist!=map.distances.length; ++idist) {
				for(int jdist = 0; jdist!=map.distances[idist].length; ++jdist) {
					System.out.print(" "+map.distances[idist][jdist]);
				}
				System.out.println();
			}
		}
		this.m_dijkstra = new DijkstraEngine(map);
		this.m_dijkstra.execute(City.valueOf(this.m_goal), null);
		if (Debug.instance().isDebugOn()){
			System.out.println("SHORTEST PATH");
			for (City city = City.valueOf(1); city != null; city = this.m_dijkstra.getPredecessor(city))
		    {
				System.out.print(city.getIndex()+" ");
		    }
			System.out.println();
			System.out.println("SHORTEST DISTANCES:");
			for (int i = 1; i!=map.distances.length; ++i) {
				System.out.println("distance("+this.m_goal+", "+i+")="+this.m_dijkstra.getShortestDistance(City.valueOf(i)));
			}
		}
	}

	@Override
	public double evaluate(ATPstate state, ATPmove move) {
		//compute h(state.getAgentPosition(this.m_agentID))
		double cost = this.m_dijkstra.getShortestDistance(City.valueOf(move.getTarget()));
		return cost;
	}

}
