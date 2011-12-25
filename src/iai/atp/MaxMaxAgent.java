package iai.atp;

import iai.search.MaxSum;

class MaxMaxAgent extends AIAgent {
	private final MaxMax<State,Move> maxmax;

	MaxMaxAgent(World world) {
		super(world);
		maxmax = new MaxSum<State,Move>(this);
	}

	/** @see iai.search.Model */
	public double reward(State state, int it) {
		return -cost(state, it);
	}

	Move choose(State state, int it) {
		return maxmax.choose(state, it, HORIZON);
	}
}