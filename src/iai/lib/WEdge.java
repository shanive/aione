package iai.lib;
  
/**
 * Weighted Edge
 */
public class WEdge extends Edge {
	/** edge weight */
	public final double weight;

	/** Constructs a neighbor
	 * @param v the other node
	 * @param weight the edge weight 
	 */
	public WEdge(int v, double weight) {
		super(v); this.weight = weight;
	}
}

