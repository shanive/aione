package iai.atp;

import iai.search.MinMax;

class MinMaxAgent extends AIAgent {
	private final MinMax<State,Move> minmax;

	MinMaxAgent(World world) {
		super(world);
		minmax = new MinMax<State,Move>(this);
	}

	/** @see iai.search.Model */
	public double reward(State state, int it) {
		return cost(state, 1-it)-cost(state, it);
	}

	Move choose(State state, int it, int left) {
		return minmax.choose(state, it, left);
	}
}