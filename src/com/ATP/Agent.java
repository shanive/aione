package com.ATP;

import java.util.LinkedList;

public abstract class Agent {

	protected int ID;
	protected int goal;
	protected int position;
	protected int vehicleId;

	public LinkedList<ATPstate> repeatedStates = new LinkedList<ATPstate>();


	protected int getID()
	{
		return this.ID;
	}

	protected int getPosition(){
		return this.position;
	}

	protected int getVehicleID()
	{
		return this.vehicleId;
	}

	protected boolean hasVehicle()
	{
		return (this.vehicleId != -1);
	}

	protected abstract ATPmove nextMove(ATPstate state);

	protected abstract boolean reachedGoal();

	protected void setPosition(int i){
		this.position = i;
	}

	protected void setVehicleID(int id){
		this.vehicleId = id;
	}
}
