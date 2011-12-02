package com.ATP;
public class ATPvehicle {
	int vehicleID;
	int agentID;
	double Vel;
	double Eff;

	public ATPvehicle(double vel, double eff, int id){
		this.agentID = -1;
		this.Vel = vel;
		this.Eff = eff;
		this.vehicleID = id;
	}

	@Override
	public int hashCode(){
		int hash = 7;
		hash = 31 * hash + this.vehicleID;
		hash = 31 * hash + this.agentID;
		hash = 31 * hash + (int)this.Vel;
		return hash;
	}

	@Override
	public boolean equals(Object other){
		if (other == null)
			return false;
		ATPvehicle vehicle = (ATPvehicle)other;
		return ((this.vehicleID == vehicle.vehicleID) &&
				(this.agentID == vehicle.agentID) &&
				(this.Eff == vehicle.Eff) &&
				(this.Vel == vehicle.Vel));
	}

	@Override
	public Object clone(){
		ATPvehicle car = new ATPvehicle(Vel, Eff, vehicleID);
		car.agentID = this.agentID;
		return car;
	}

	public boolean isTaken()
	{
		return (this.agentID > -1);
	}

	public int getVehicleId()
	{
		return this.vehicleID;
	}

	public double getVel()
	{
		return this.Vel;
	}

	public double speedFlooded()
	{
		//warning: if this.Eff == 0 then this will return 0!!!
		return this.Vel*this.Eff;
	}

	public double speedUnflooded()
	{
		return this.Vel;
	}

	public void setAgent(int id){
		this.agentID = id;
	}

	public int getAgent(){
		return this.agentID;
	}

	public double getEff()
	{
		return this.Eff;
	}
}