package iai.lib;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Undirected graph
 */

public class Graph {
	/**
	 * Neighbor of a node 
	 */
	public static class Neighbor {
		/** the other node */
		public final int v;
		/** edge weight */
		public final double weight;

		/** Constructs a neighbor
		 * @param v the other node
		 * @param weight the edge weight 
		 */
		Neighbor(int v, double weight) {
			this.v = v; this.weight = weight;
		}
	}

	/**
	 * Number of nodes in the graph, nodes are numbered
	 * from 0 to size-1.
	 */
	public final int size;

	private final ArrayList<LinkedList<Neighbor>> nodes;

	/**
	 * Constructs a graph of the given size
	 * with no links
	 */
	public Graph(int size) {
		this.size = size;
		nodes = new ArrayList<LinkedList<Neighbor>>(size);
		for(int u = 0; u!=size; ++u)
			nodes.add(new LinkedList<Neighbor>());
	}

	/**
	 * Adds an edge to the graph
	 * @param u first edge node
	 * @param v second edge node
	 * @param weight edge weight
	 * @return the graph, for cascading
	 */
	public Graph edge(int u, int v, double weight) {
		assert 0 <= u && u < size;
		assert 0 <= v && v < size;
		nodes.get(u).add(new Neighbor(v, weight));
		return this;
	}

	/**
	 * Returns list of neighbors of the given node 
	 * @param u the node
	 * @return the list of neighbors
	 */
	public List<Neighbor> neighbors(int u) {
		return nodes.get(u);
	}
}