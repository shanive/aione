package iai.atp;

import iai.search.MaxSum;

class MaxSumAgent extends AIAgent {
	private final MaxSum<State,Move> maxsum;

	MaxSumAgent(World world) {
		super(world);
		maxsum = new MaxSum<State,Move>(this);
	}

	/** @see iai.search.Model */
	public double reward(State state, int it) {
		return -(cost(state, it)+cost(state, 1-it));
	}

	Move choose(State state, int it) {
		return maxsum.choose(state, it, HORIZON);
	}
}
