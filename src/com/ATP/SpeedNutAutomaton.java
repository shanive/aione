package com.ATP;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;


public class SpeedNutAutomaton extends Agent{
	private int prev_position;
	private HashMap<Integer,Integer> past_positions;
	private boolean reachedGoal;

	public SpeedNutAutomaton(int id, int initial, int goalv){
		this.ID = id;
		this.goal = goalv;
		this.position = initial;
		this.vehicleId = -1;
		this.prev_position = -1;
		this.reachedGoal = false; //this is true if we reached the same vertex 3 times
		this.past_positions = new HashMap<Integer, Integer>();
	}

	public String toString(){
		return "SpeedNutAutomated("+this.ID+", "+this.goal+")";
	}

	@Override
	public ATPmove nextMove(ATPstate state) {
		ATPmove nextMove = null;
		//update the number of times visited this vertex
		if(this.past_positions.containsKey(this.position)){
			Integer count = this.past_positions.get(this.position);
			this.past_positions.remove(this.position);
			this.past_positions.put(this.position, count+1);
		} else {
			this.past_positions.put(this.position, 1);
		}

		if(this.past_positions.get(this.position)==3){
			this.reachedGoal = true;
		}

		if(!reachedGoal()){
			Vector<ATPedge> neighbours =
				ATPgraph.instance().neighboursOf(this.position);
			//sort neighbours by weight value
			Collections.sort(neighbours, new Comparator<ATPedge>(){
					public int compare(ATPedge o1, ATPedge o2){
						double diff = o1.getWeight() - o2.getWeight();
						if ((diff > 0) ||
							((diff == 0) && (o1.getTarget() < o2.getSource())))
							return -1;
						else return 1;
					}});
			//Vector<ATPvehicle> vehicles = state.getVehiclesAt(this.position);

			//gets the vehicleID of the fastest available water vehicle (at [0]) and regular car (at [1]) or -1 if there is no such vehicles
			int[] cars = getWaterAndFastestVehicle(state);
			//go through the sorted edges and choose the heaviest can be crossed
			//if there's no cars available theres no point going through the edges
			if(!(cars[0]==-1 & cars[1]==-1)){
				Iterator<ATPedge> it = neighbours.iterator();
				while(it.hasNext()){
					ATPedge e = it.next();
					int target;
					if (e.getSource()==this.position) target = e.getTarget();
					else target = e.getSource();
					if(target!=this.prev_position){
						if (e.isFlooded()){
							if (cars[0]!= -1){ nextMove = new ATPmove(target, cars[0]); break;}
						}
						else if (cars[1]!= -1){nextMove = new ATPmove(target, cars[1]); break;}
					}
				}
			}
		}
		return nextMove;
	}

	/*
	 * finds the fastest water and regular vehicle and returns them in int[2]
	 * where int[0] is the water and int[1] is the regular.
	 */
	private int[] getWaterAndFastestVehicle(ATPstate state) {
		Vector<ATPvehicle> vehicles = state.getVehiclesAt(this.position);
		int[] ans = {-1,-1};
		double fastestWater = 0;
		double fastestRegular = 0.0;
		Iterator<ATPvehicle> it = vehicles.iterator();
		while(it.hasNext()){
			ATPvehicle car = it.next();
			if(state.isVehicleAvailable(car.getVehicleId()) || state.getVehicleOwner(car.getVehicleId())==this.ID){
				double floodedSpeed = car.speedFlooded();
				double regularSpeed = car.speedUnflooded();
				int vehID = car.getVehicleId();
				if(floodedSpeed>fastestWater){
					ans[0] = vehID;
					fastestWater = floodedSpeed;
				}
				if(regularSpeed>fastestRegular){
					ans[1] = vehID;
					fastestRegular = regularSpeed;
				}
			}
		}
		return ans;
	}

	@Override
	public boolean reachedGoal() {
		return ((this.goal == this.position)|(this.reachedGoal));
	}

	@Override
	public void setPosition(int i){
		this.prev_position = this.position;
		this.position = i;
	}

}
