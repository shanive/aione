package com.tree;

public class AstarAgent extends SearchAgent{

	public AstarAgent(int id, int initial, int goal){
		super(id, initial, goal, new GreedyHeuristic(id, goal), -1);
	}

}
