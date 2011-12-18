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

	public AgentState(int initial){
		this.position = initial; //initial state
		this.vehicle = -1; //has no vehicle at the beginning
        this.pathCost  = 0.0;
	}

	public AgentState(AgentState other){
		this.position = other.getAgentPosition();
		this.vehicle = other.getAgentVehicleId();
	}

	public int getAgentPosition(){
		return this.position;
	}

	public int getAgentVehicleId(){
		return this.vehicle;
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
	}

	@Override
	public int hashCode(){
		int hash = 7;
		hash = 31 * hash + this.position;
		hash = 31 * hash + this.vehicle;
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
