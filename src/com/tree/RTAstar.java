package com.tree;

public class RTAstar extends SearchAgent{

	public RTAstar(int id, int initial, int goal, int depth){
		super(id, initial, goal, new GreedyHeuristic(id, goal), depth);
	}

}
