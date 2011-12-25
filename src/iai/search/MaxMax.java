package iai.search;

/** MaxMax (or any other function that retains the same value
	for both agents in the given state) */
public class MaxMax<State,Move> {
	/** model to which the search is applied */
	private final Model<State,Move> model;
	/** the algorithm needs some +/-infinity bound to calculate alpha, beta */
	private final static Double INF = Double.POSITIVE_INFINITY/2.0;

	/** initializes search (binds to the model)
	 * @param model the model
	 * @see iai.search.Model
	 */
	public MaxMax(Model<State,Move> model) {
		this.model = model;
	}
	
	/** chooses move
	 * @param state current state
	 * @param ip number of player, must be 0 or 1
	 * @param depth maximum tree depth, in plies
	 * @return next move
	 */
	public Move choose(State state, int ip, int depth) {
		double vmax = -INF;
		Move best = null;
		for(Move m: model.moves(state, ip)) {
			double v = maxVal(model.succ(state, m), ip, 1-ip, depth)[ip];
			if(v > vmax) {
				vmax = v; best = m;
			}
		}
		return best;
	}

	/** maxValue step of MaxMax */
	private double[] maxVal(State state, int ip, int jp, int depth) {
		--depth;
		if(model.isGoal(state) || depth==0)
			return new double[]{model.reward(state, 0), model.reward(state, 1)};
		double[] vmax = {-INF, -INF};
		for(Move m: model.moves(state, jp)) {
			double[] v = maxVal(model.succ(state, m), ip, 1-jp, depth);
			if(v[jp] > vmax[jp] 
			   || (v[jp]==vmax[jp] && v[1-jp]>vmax[1-jp]))
				vmax = v;
		}
		return vmax;
	}

}
