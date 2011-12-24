package iai.lib;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Undirected graph
 */

public class Graph<E extends Edge> {
	/** Number of nodes, nodes are numbered from 0 to size-1. */
	public final int size;
	/**  Neighborhood list. */
	private final LinkedList<E>[] nodes;

	/**
	 * Constructs a graph of the given size
	 * with no links
	 */
	@SuppressWarnings({"unchecked"})
	public Graph(int size) {
		this.size = size;
		nodes = new LinkedList[size];
		for(int u = 0; u!=size; ++u)
			nodes[u] = new LinkedList<E>();
	}

	/**
	 * Adds an edge to the graph
	 * @param u the node
	 * @param e the edge
	 * @return the graph, for cascading
	 */
	public Graph<E> edge(int u, E e) {
		assert 0 <= u && u < size;
		assert 0 <= e.v && e.v < size;
		nodes[u].add(e);
		return this;
	}

	/**
	 * Returns list of neighbors of the given node 
	 * @param u the node
	 * @return the list of neighbors
	 */
	public List<E> neighbors(int u) {
		return nodes[u];
	}
}