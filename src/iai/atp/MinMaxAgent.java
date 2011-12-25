package iai.atp;

class MinMaxAgent extends AIAgent {

	MinMaxAgent(World world, int it) {
		super(world, it);
	}

	public double reward(State state) {
		return cost(state, 1-it)-cost(state, it);
	}

	Move choose(State state, int left) {
		return null;
	}
}