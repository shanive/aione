package iai.atp;

import java.util.List;
import iai.search.Model;

/** Artificial Intelligence-based search Agent */

abstract class AIAgent extends Agent implements Model<State,Move> {
	/** distance heuristic, so that the search is informed */
	private final Heur[] heur;

	/** constructor, also initializes the heuristic */
	AIAgent(World world) {
		super(world);
		heur = new Heur[world.travellers.length];
		for(int it=0; it!=heur.length; ++it) {
			heur[it] = new Heur(world, it);
		}
	}
	
	/* partial implementation of Model */
	/** see @iai.search.Model */
	public List<Move> moves(State state, int it) {
		return state.moves(it);
	}

	/** see @iai.search.Model */
	public State succ(State state, Move move, int left) {
		return new State(state, move, left);
	}

	/** see @iai.search.Model */
	public boolean isGoal(State state) {
		return state.isGoal();
	}

	/** state cost; if the state is a terminal state
	 * for the agent, then the cost is the accumulated
	 * expenses; otherwise, an heuristic estimate of
	 * remaining expenses is added
	 * @param state the state
	 * @param jt traveller index (need to estimate for both)
	 * @return cost or cost estimate
	 */
	protected double cost(State state, int jt) {
		double cost = state.texp[jt];
		if(state.tloc[jt]!=world.travellers[jt].t)
			cost+= heur[jt].estimate(state.tloc[jt]);
		return cost;
	}

	/** see @iai.search.Model */
	abstract public double reward(State state, int it);
	/* end of Model */	

	/** still abstract, depends on the search algorithm */
	abstract Move choose(State state, int it, int left);
}