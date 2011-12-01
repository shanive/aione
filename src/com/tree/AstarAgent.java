package com.tree;

public class AstarAgent extends SearchAgent{

	public AstarAgent(int id, int initial, int goal){
		super(id, initial, goal, new GreedyHeuristic(id, goal), new AstarComparator(), -1);
	}


	public ATPmove nextMove(ATPstate state){

	}

}
