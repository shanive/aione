package iai.lib;
import java.util.PriorityQueue;
import java.util.Comparator;

/** implementation of the Dijkstra algorithm */

public class Dijkstra {
	/** vector of distances */
	private final double[] d;
	/** vector of predecessors in the shortest path */
	private final int[] pi;
	/** node queue */
	private PriorityQueue<Integer> queue;

	/** 
	 * establishes computation of distances from v
	 * to all nodes in graph
	 * @param g the graph
	 * @param s the source node
	 */
	public Dijkstra(Graph<? extends Edge> g, int s) {
		/* allocate tables */
		queue = new PriorityQueue<Integer>
			(g.size,
			 new Comparator<Integer>() {
				public int compare(Integer u, Integer v) {
					return Double.compare(d[u], d[v]);
				}
			});
		d = new double[g.size];	pi = new int[g.size];

		/* initialize */
		for(int u=0; u!=d.length; ++u)
			d[u] = Double.MAX_VALUE;
		d[s] = 0.0;	pi[s] = -1; queue.add(s);

		/* loop */
		Integer u;
		while((u = queue.poll()) != null) {
			queue.remove(u); /* for buggy java versions */
			for(Edge nb: g.neighbors(u)) {
				if(d[nb.v] > d[u]+nb.weight) {
					d[nb.v] = d[u]+nb.weight; pi[nb.v] = u;
					/* re-insert with new distance */
					queue.remove(nb.v); queue.offer(nb.v);
				}
			}
		}
		/* all distances from s are computed */
	}

	/**
	 * computes distance between s and v
	 * @param v node to compute distance to
	 * @return the distance
	 */
	public double dist(int v) {return d[v];}

	/**
	 * returns the predecessor in the shortest past to u
	 * @param u the node 
	 * @return the predecessor
	 */
	public int pred(int u) {return pi[u];}

	/**
	 * a basic test for the Dijkstra algorithm
	 * @see Test
	 */
	public static void test() {
		Graph<Edge> g = new Graph<Edge>(4)
			.edge(0, new Edge(1, 2.0))
			.edge(1, new Edge(2, 1.0))
			.edge(0, new Edge(2, 2.0))
			.edge(0, new Edge(3, 5.0))
			.edge(2, new Edge(3, 1.0));
		Dijkstra d = new Dijkstra(g, 0);
		assert d.dist(0) == 0;
		assert d.dist(1) == 2.0;
		assert d.dist(2) == 2.0;
		assert d.dist(3) == 3.0;
		assert d.pred(2) == 0;
	}
}