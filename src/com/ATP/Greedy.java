package com.ATP;
import java.util.Iterator;
import java.util.Vector;

import com.special.dijkstra.City;
import com.special.dijkstra.DenseRoutesMap;
import com.special.dijkstra.DijkstraEngine;

public class Greedy extends Agent {

	private Vector<Integer> path;

	public Greedy(int id, int initial, int goalv){
		this.ID = id;
		this.goal = goalv;
		this.position = initial;
		this.vehicleId = -1;
		this.path = null;
		this.findBestPath();
	}
	
	private void findBestPath(){
		//initiate map
		DenseRoutesMap map = new DenseRoutesMap(ATPgraph.instance().verticesNum());
		//update edges' weights
		for(int i = 0; i < ATPgraph.instance().verticesNum(); i++){
			Iterator<ATPedge> it = ATPgraph.instance().neighboursOfIterate(i);
			while (it.hasNext()){
				ATPedge edge = it.next();
				map.addDirectRoute(City.valueOf(edge.getSource()), City.valueOf(edge.getTarget()), edge.getWeight());
			}
		}
		//initial dijkstra
		DijkstraEngine engine = new DijkstraEngine(map);
		engine.execute(City.valueOf(this.position), null);
		//retrieve path from engine
		this.path = new Vector<Integer>();
		for(City city = City.valueOf(this.goal); city != City.valueOf(this.position); city = engine.getPredecessor(city)) {
			System.out.print(city.getIndex()+" ");
			this.path.add(0, city.getIndex());
		}
		System.out.println("Found Path");
	}
	

	public String toString(){
		return "Greedy("+this.ID+", "+this.goal+")";
	}

	public boolean reachedGoal(){
		return (this.goal == this.position);
	}

	@Override
	public ATPmove nextMove(ATPstate state) {
		
		if ((this.path == null) || (this.path.isEmpty())){
				System.out.println("empty path");
				return null;
		}
		boolean isFlooded;
		int car = state.agentVehicle(this.ID);
		isFlooded = ATPgraph.instance().getEdge(this.position, this.path.get(0)).isFlooded();
		//search for new car only if the edge is flooded and our current car is incompatible
		if (isFlooded )
			if (((car!= -1) && (ATPgraph.instance().getVehicle(car).getEff() == 0))
					|| (car==-1)){
				car = FindFloatingVehicle(this.position, state);
			} else {}
		else if (car==-1) car = FindRegularVehicle(this.position, state);
		if (car== -1) return null;
		return new ATPmove(this.path.remove(0).intValue(), car);
	
	}

	private int FindRegularVehicle(int pos, ATPstate state)
	{
		//we get in to this procedure only if this.vehicle == -1
		Iterator<ATPvehicle> it = state.iterateVehicleAt(pos);
		int bestVehicleId = -1;
		double bestSpeed = 0;
		while (it.hasNext())
		{
			ATPvehicle temp = it.next();
			if ((temp.speedUnflooded() > bestSpeed) && (state.isVehicleAvailable(temp.getVehicleId()))){
				bestSpeed = temp.speedUnflooded();
				bestVehicleId = temp.getVehicleId();
			}
		}
		return bestVehicleId;
	}

	private int FindFloatingVehicle(int pos, ATPstate state){
		Iterator<ATPvehicle> it = state.iterateVehicleAt(pos);
		ATPvehicle best = null;
		double bestSpeed = 0;
		ATPvehicle tmp;
		while(it.hasNext()){
			tmp = it.next();
			if(state.isVehicleAvailable(tmp.getVehicleId())){
				double speed = tmp.speedFlooded();
				if ((speed>0) & (speed>bestSpeed)){
					best = tmp;
					bestSpeed = speed;
				}
			}
		}
		if(best!=null)return best.getVehicleId();
		return -1;
	}
}
