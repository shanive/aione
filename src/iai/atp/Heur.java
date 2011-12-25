package iai.atp;

import iai.lib.Graph;
import iai.lib.WEdge;
import iai.lib.Dijkstra;

/** Cost heuristic, Dijkstra-based */
class Heur {
	/** Dijkstra distance table */
	private final Dijkstra dijkstra;

	/** initializes the heuristic for an agent
	 * @param world the wolrd
	 * @param it the agent index
	 */
	Heur(World world, int it) {
		/* first, find the maximum clear and flooded speed */
		double maxcspeed = 0.0, maxfspeed = 0.0;
		for(int iv=0; iv!=world.vehicles.length; ++iv) {
			if(world.vehicles[iv].cspeed > maxcspeed)
				maxcspeed = world.vehicles[iv].cspeed;
			if(world.vehicles[iv].fspeed > maxfspeed)
				maxfspeed = world.vehicles[iv].fspeed;
		}

		/* now, build a optimistic weighted graph with weights computed
		   for the best vehicle for each road */
		Graph<WEdge> owg = new Graph<WEdge>(world.roads.size);
		for(int u = 0; u!=owg.size; ++u) {
			for(World.Road r: world.roads.neighbors(u)) {
				if(r.clear) {
					if(maxcspeed>0)
						owg.edge(u, new WEdge(r.v, r.weight/maxcspeed));
				} else {
					if(maxfspeed>0)
						owg.edge(u, new WEdge(r.v, r.weight/maxfspeed));
				}
			}
		}

		/* then, compute the distances from the target node */
		dijkstra = new Dijkstra(owg, world.travellers[it].t);
	}

	/** returns a heuristic (optimistic) cost estimate for a node
	 * @param v the current node
	 * @return distance from v to the target
	 */
	double estimate(int v) {
		return dijkstra.dist(v);
	}
}