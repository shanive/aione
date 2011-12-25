package iai.atp;

import java.util.List;
import iai.search.Model;

abstract class AIAgent extends Agent implements Model<State,Move> {
	/** distance heuristic, so that the search is informed */
	private final Heur heur;

	/** constructor, also initializes the heuristic */
	AIAgent(World world, int it) {
		super(world, it);
		heur = new Heur(world, it);
	}
	
	/* partial implementation of Model */
	public List<Move> moves(State state) {
		return state.moves(it);
	}
	
	public State succ(State state, Move move, int left) {
		return new State(state, move, left);
	}
	
	protected double cost(State state, int jt) {
		double cost = state.texp[jt];
		if(state.tloc[jt]!=world.travellers[jt].t)
			cost+= heur.estimate(state.tloc[jt]);
		return cost;
	}
	abstract public double reward(State state);
	/* end of Model */	
	
	abstract Move choose(State state, int left);
}