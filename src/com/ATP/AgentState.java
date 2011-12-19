package com.ATP;
/**
 * representation of agent's state on ATPstate
 * include agent's current position and current vehicle id
 * @author Vered
 *
 */
public class AgentState {
	private int position;
	private int vehicle;
    private double pathCost;
    private int stepsNum;

	public AgentState(int initial){
		this.position = initial; //initial state
		this.vehicle = -1; //has no vehicle at the beginning
        this.pathCost  = 0.0;
        this.stepsNum = 0;
	}

	public AgentState(AgentState other){
		this.position = other.getAgentPosition();
		this.vehicle = other.getAgentVehicleId();
		this.pathCost = other.getAgentScore();
		this.stepsNum = other.getAgentStepsNum();
	}

	public int getAgentPosition(){
		return this.position;
	}

	public int getAgentVehicleId(){
		return this.vehicle;
	}

	public double getAgentScore(){
		return this.pathCost;
	}

	public int getAgentStepsNum(){
		return this.stepsNum;
	}

	public void addTime(double time){
		this.pathCost += time;
	}

	protected void setAgentPosition(int pos){
		this.position = pos;
	}

	protected void setAgentVehicleId(int id){
		this.vehicle = id;
	}

	public void moveAgent(int posTarget, int vehTarget, double cost){
		this.setAgentPosition(posTarget);
		this.setAgentVehicleId(vehTarget);
		this.pathCost += cost;
		this.stepsNum += 1;
	}

	@Override
	public int hashCode(){
		int hash = 7;
		hash = 31 * hash + this.position;
		hash = 31 * hash + this.vehicle;
		hash = 31 * hash + this.stepsNum;
		hash = 31 * hash + (int)this.pathCost;
		return hash;
	}

	@Override
	public boolean equals(Object other){
		if (other == null)
			return false;
		AgentState state = (AgentState)other;
		return ((this.position == state.position) &&
				(this.vehicle == state.vehicle));
	}

	@Override
	public Object clone(){
		AgentState other = new AgentState(this.position);
		other.vehicle = this.vehicle;
		return other;
	}

}
