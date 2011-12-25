package iai.atp;

import iai.lib.Graph;
import iai.lib.WEdge;
import iai.lib.Dijkstra;

/** Cost heuristic, Dijkstra-based */
class Heur {
	/** 1/maximum-speed */
	private final double speedfactor;
	/** Dijkstra distance table */
	private final Dijkstra dijkstra;

	/** initializes the heuristic for an agent
	 * @param world the wolrd
	 * @param it the agent index
	 */
	Heur(World world, int it) {
		/* first, find the maximum clear speed */
		double maxcspeed = 0.0;
		for(int iv=0; iv!=world.vehicles.length; ++iv)
			if(world.vehicles[iv].cspeed > maxcspeed)
				maxcspeed = world.vehicles[iv].cspeed;
		speedfactor = 1.0/maxcspeed; /* implicit assert maxcspeed > 0 */

		/* then, compute the distances from the target node */
		dijkstra = new Dijkstra(world.roads, world.travellers[it].t);
	}

	/** returns a heuristic (optimistic) cost estimate for a node
	 * @param v the current node
	 * @return distance from v to the target
	 */
	double estimate(int v) {
		return speedfactor*dijkstra.dist(v);
	}
}