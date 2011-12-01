package com.tree;

import java.util.Comparator;

public class GreedyComparator implements Comparator<Node>{
	
	public GreedyComparator(){
		
	}

	@Override
	public int compare(Node o1, Node o2) {
		double diff = o1.getHeuristicValue() - o2.getHeuristicValue() ;
		if (diff > 0) return 1;//used if and not casting for accuracy 
		if (diff < 0) return -1;
		return 0;
	}

}
