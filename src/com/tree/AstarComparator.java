package com.tree;

import java.util.Comparator;

public class AstarComparator implements Comparator<Node>{

	public AstarComparator(){
	}

	@Override
	public int compare(Node o1, Node o2) {
		double diff = (o1.getHeuristicValue() + o1.getPathCost()) - (o2.getHeuristicValue() + o2.getPathCost());
		if (diff > 0) return 1;
		if (diff < 0) return -1;
		return 0;
	}

}
