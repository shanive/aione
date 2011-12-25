package iai.atp;

import iai.lib.WEdge;
import iai.lib.Graph;

class World {
	/** added to the move cost when goal is not reached and no moves left */
	static double PENALTY = 100.0;

	/** Road, an edge that can be either clear or flooded */
	static class Road extends WEdge {
		/** true when clear */
		final boolean clear;
		/** constructs a Road
		 * @param v the other end of the road
		 * @param weight the road length
		 * @param clear true when the road is clear
		 */
		Road(int v, double weight, boolean clear) {
			super(v, weight); this.clear = clear;
		}
	}

	/** Vehicle */
	static class Vehicle {
		/** vehicle garage location */
		final int garage;
		/** vehicle speeds on C)lear and F)looded roards */
		final double cspeed, fspeed;

		/**
		 * Constructs a Vehicle
		 * @param garage the node in which the vehicle is parked
		 * @param cspeed speed on clear road
		 * @param fspeed speed on flooded road
		 */
		Vehicle(int garage, double cspeed, double fspeed) {
			this.garage = garage;
			this.cspeed = cspeed; this.fspeed = fspeed;
		}
	}

	/** Traveller */
	static class Traveller {
		/* source and target */
		final int s, t;
		/* switch cost */
		final double cswitch;

		/** constructs Traveller, see class members */
		Traveller(int s, int t, double cswitch) {
			this.s = s; this.t = t;
			this.cswitch = cswitch;
		}
	}

	/** road graph */
	final Graph<Road> roads;
	/**  vehicles, in declaration order */
	final Vehicle[] vehicles;
	/** travellers, in declaration order */
	final Traveller[] travellers;

	/** constructs World, see class members */
	World(Graph<Road> roads, Vehicle[] vehicles, Traveller[] travellers) {
		this.roads = roads;
		this.vehicles = vehicles;
		this.travellers = travellers;
	}
}