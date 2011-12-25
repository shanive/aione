package iai.atp;

class MinMaxAgent extends AIAgent {

	MinMaxAgent(World world) {
		super(world);
	}

	public double reward(State state, int it) {
		return cost(state, 1-it)-cost(state, it);
	}

	Move choose(State state, int it, int left) {
		return null;
	}
}