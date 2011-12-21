package com.ATP;

import java.util.Iterator;
import java.util.LinkedList;

public abstract class Agent {

	protected int ID;
	protected int goal;
	protected int position;
	protected int vehicleId;

	public LinkedList<ATPstate> repeatedStates = new LinkedList<ATPstate>();


	public int getID()
	{
		return this.ID;
	}

	public int getPosition(){
		return this.position;
	}

	public int getVehicleID()
	{
		return this.vehicleId;
	}

	public boolean hasVehicle()
	{
		return (this.vehicleId != -1);
	}

	public abstract ATPmove nextMove(ATPstate state);

	public abstract boolean reachedGoal();

	public void setPosition(int i){
		this.position = i;
	}

	public void setVehicleID(int id){
		this.vehicleId = id;
	}
	
	public boolean isRepeatedState(ATPstate state){
		Iterator<ATPstate> it = this.repeatedStates.iterator();
		while (it.hasNext()){
			if (state.isEqual(it.next()))
				return true;
		}
		return false;
	}
}
