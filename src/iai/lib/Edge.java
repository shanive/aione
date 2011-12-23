package iai.lib;
  
/**
 * Edge of a node 
 */
public class Edge {
	/** the other node */
	public final int v;
	/** edge weight */
	public final double weight;

	/** Constructs a neighbor
	 * @param v the other node
	 * @param weight the edge weight 
	 */
	public Edge(int v, double weight) {
		this.v = v; this.weight = weight;
	}
}

