package iai.atp;

import java.util.List;
import java.util.LinkedList;

/** World state, gets copied a lot, make it final */
final class State {
	private final World world;
	/** vehicle locations for free vehicles, or -1 if the vehicle is taken */
	final int[] vloc;
	/** traveller locations */
	final int[] tloc;
	/** traveller vehicles, or -1 if no vehicle */
	final int[] tvcl; 
	/** traveller expenses so far */
	final double[] texp;

	/** constructs initial state
	 * @param world the world
	 */
	State(World world) {
		this.world = world;
		vloc = new int[world.vehicles.length];
		tloc = new int[world.travellers.length];
		tvcl = new int[world.travellers.length];
		texp = new double[world.travellers.length];
		for(int iv=0; iv!=world.vehicles.length; ++iv) {
			vloc[iv] = world.vehicles[iv].garage;
		}
		for(int it=0; it!=world.travellers.length; ++it) {
			tloc[it] = world.travellers[it].s;
			tvcl[it] = -1; texp[it] = 0.0;
		}
	}

	/** Copy constructor */
	State(State orig) {
		world = orig.world;
		vloc = orig.vloc.clone();
		tloc = orig.tloc.clone();
		tvcl = orig.tvcl.clone();
		texp = orig.texp.clone();
	}

	/** Computes the list of available moves 
	 * @param it traveller for which to compute the moves
	 * @return List of moves
	 * @see Move
	 */
	List moves(int it) {
		LinkedList moves = new LinkedList();
		return moves;
	}

	/** human-readable representation of state */
	String toString() {
		String s = "";
		return s;
	}
}