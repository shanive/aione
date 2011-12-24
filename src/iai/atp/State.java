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

	/** Successor state constructor
	 * @param orig original state
	 * @param m move
	 * @param left number of moves left
	 */
	State(State orig, Move m, int left) {
		/* initialize new state from the original state */
		world = orig.world;
		vloc = orig.vloc.clone();
		tloc = orig.tloc.clone();
		tvcl = orig.tvcl.clone();
		texp = orig.texp.clone();

		/* switching vehicle? */
		if(m.iv!=tvcl[m.it]) {
			if(tvcl[m.it]!=-1)
				vloc[tvcl[m.it]] = tloc[m.it];  /* drop vehicle */
			vloc[m.iv] = -1; tvcl[m.it] = m.iv; /* pick up new vehicle */
		}
		
		/* move to the new location */
		tloc[m.it] = m.v;
		texp[m.it]+= cost(m, left);
	}
		
	/** Computes the list of available moves 
	 * @param it traveller for which to compute the moves
	 * @return List of moves
	 * @see Move
	 */
	List<Move> moves(int it) {
		LinkedList<Move> moves = new LinkedList<Move>();
		for(int iv = 0; iv!=vloc.length; ++iv)
			if(iv==tvcl[it] || vloc[iv]==tloc[it]) /* either mine or free & here */
				for(World.Road r : world.roads.neighbors(tloc[it])) 
					if(r.clear || world.vehicles[iv].fspeed!=0.0)
						moves.add(new Move(it, r.v, iv));
		return moves;
	}

	/** Checks whether the move is legal
	 * @param m the move
	 * @return true when the move is legal 
	 */
	boolean isLegal(Move m) {
		/* is the vehicle valid? */
		if(tvcl[m.it]!=m.iv   /* vehicle changed */
   		   && vloc[m.iv]==-1) /* but was not available */
			return false;
		/* is the node reachable? */
		for(World.Road r: world.roads.neighbors(tloc[m.it]))
			if(r.v==m.v && (r.clear || world.vehicles[m.iv].fspeed>0.0))
				return true;
		return false;
	}

	/** Computes move cost in the current state, 
	 * assumes the move is legal
	 * @param m move
	 * @return cost 
	 */
	double cost(Move m, int left) {
		double cost = 0.0;
		if(m.iv!=tvcl[m.it])
			cost+= world.travellers[m.it].cswitch;
		for(World.Road r: world.roads.neighbors(tloc[m.it])) {
			if(r.v==m.v) {
				cost+= r.weight/(r.clear? world.vehicles[m.iv].cspeed
								 : world.vehicles[m.iv].fspeed);
				break;
			}
		}
		if(left==1 && m.v!=world.travellers[m.it].t)
			cost+= World.LATE_FINE;
		return cost;
	}

	/** Checks whether the state is a goal state 
	 * @return true when the state is a goal state
	 */
	boolean isGoal() {
		/* all travellers must be at their targets */
		for(int it=0; it!=tloc.length; ++it)
			if(tloc[it]!=world.travellers[it].t)
				return false;
		return true;
	}

	/** Checks whether the state is like another state
	 * (i.e. does not worth being revisited)
	 * @param state the state to compare with
	 * @return true when the states are alike
	 */
	boolean isLike(State state) {
		/** all travellers are at the same nodes in the same vehicles */
		for(int it=0; it!=tloc.length; ++it)
			if(tloc[it]!=state.tloc[it] || tvcl[it]!=state.tvcl[it])
				return false;
		/** all free vehicles are at the same locations */
		for(int iv=0; iv!=vloc.length; ++iv)
			if(vloc[iv]!=state.vloc[iv])
				return false;
		return true;
	}

	/** Human-readable representation of state. For example:
	 * <pre>
	 *    t1=1(2)/5.0 t2=2(_)/2.0 v1=1 v2=_
	 * </pre>
	 *  means
	 *  <ul>
	 *    <li>traveller 1 is at node 1 in vehcile 2, spent 5 cost units</li>
	 *    <li>traveller 2 is at node 2 with no vehicle</li>
	 *    <li>vehicle 1 is at node 1</li>
	 *    <li>vehicle 2 is taken</li>
	 *  </ul>
	 */
	public String toString() {
		String s = "";
		for(int it=0; it!=tloc.length;++it)
			s+= " t"+(it+1)+"="+(tloc[it]+1)
				+"("+(tvcl[it]==-1?"_":tvcl[it]+1)+")"
				+ "/"+texp[it];
		for(int iv=0; iv!=vloc.length; ++iv)
			s+= " v"+(iv+1)+"="+(vloc[iv]==-1?"_":vloc[iv]+1);
		return s;
	}
}