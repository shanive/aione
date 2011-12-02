package com.ATP;

public class ATPmove {
	int target_vertex;
	int vehical;
	
	public ATPmove(int target, int car)
	{
		this.target_vertex = target;
		this.vehical = car;
	}
	
	public ATPmove(ATPmove action) {
		this.target_vertex = action.target_vertex;
		this.vehical = action.vehical;
	}

	public int getTarget()
	{
		return this.target_vertex;
	}
	
	public int getVehicleID()
	{
		return this.vehical;
	}
	
	public String toString()
	{
		return "move(target: "+this.target_vertex+", vehicle: "+this.vehical+")";
	}
}
