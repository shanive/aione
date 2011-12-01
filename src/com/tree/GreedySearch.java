package com.tree;



public class GreedySearch extends SearchAgent{

	/**
	 * constructor.
	 * @param initial The initial position
	 * @param target The goal position.
	 * @param id Agent's ID
	 */
	public GreedySearch(int id, int initial, int target)
	{
		super(id, initial, target, new GreedyHeuristic(id, target), new GreedyComparator(), 1);
	}

}
