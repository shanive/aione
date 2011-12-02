<<<<<<< HEAD
package com.tree;

import java.util.Comparator;

public class NodesComparator implements Comparator<Node>{

	public NodesComparator(){
	}

	@Override
	public int compare(Node o1, Node o2) {
		double diff = (o1.getHeuristicValue() + o1.getPathCost()) - (o2.getHeuristicValue() + o2.getPathCost());
		if (diff > 0) return 1;
		if (diff < 0) return -1;
		return 0;
	}

}
=======
package com.tree;

import java.util.Comparator;

public class NodesComparator implements Comparator<Node>{

	public NodesComparator(){
	}

	@Override
	public int compare(Node o1, Node o2) {
		double diff = (o1.getHeuristicValue() + o1.getPathCost()) - (o2.getHeuristicValue() + o2.getPathCost());
		if (diff > 0) return 1;
		if (diff < 0) return -1;
		return 0;
	}

}
>>>>>>> a74a0ad6e0b1ca0b129e3d365d054f479d6e24a2
