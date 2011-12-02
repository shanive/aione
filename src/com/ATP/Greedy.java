package com.ATP;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import com.special.dijkstra.City;
import com.special.dijkstra.DijkstraEngine;


public class Greedy extends Agent {

	private Vector<Integer> moves;

	public Greedy(int id, int initial, int goalv){
		this.ID = id;
		this.goal = goalv;
		this.position = initial;
		this.vehicleId = -1;
		this.moves = null;
	}

	public String toString(){
		return "Greedy("+this.ID+", "+this.goal+")";
	}

	public boolean reachedGoal(){
		return (this.goal == this.position);
	}

	@Override
	public ATPmove nextMove(ATPstate state) {

		if(moves == null){
			DijkstraEngine.instance().execute(City.valueOf(state.getAgentPosition(this.ID)), City.valueOf(this.goal));
			Vector<Integer> l = new Vector<Integer>();

			 for (City city = City.valueOf(this.goal); city != null; city = DijkstraEngine.instance().getPredecessor(city))
			 {
			     l.add(city.getName());
			 }

			 Collections.reverse(l);
			 this.moves = l;
			 this.moves.remove(0);
		}

		if(this.goal != this.position){
			boolean isFlooded;
			int car = this.vehicleId;
			try {
				isFlooded = checkIfNextEdgeIsFlooded(this.position, moves.firstElement().intValue());
				//search for new car only if the edge is flooded and our current car is incompatible
				if (isFlooded )
					if (((car!= -1) && (state.getVehicleAt(this.vehicleId, this.position).getEff() == 0))
							|| (car==-1)){
						car = FindFloatingVehicle(this.position, state);
					} else {}
				else if (car==-1) car = FindRegularVehicle(this.position, state);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (car== -1) return null;
			return new ATPmove(moves.remove(0).intValue(), car);
		} else return null;
	}

	private boolean checkIfNextEdgeIsFlooded(int v, int u){
		return ATPgraph.instance().getEdge(v, u).isFlooded();
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
