package iai.search;

/** MinMax with alpha-beta pruning */
public class MinMax<State,Move> {
	/** model to which the search is applied */
	private final Model<State,Move> model;

	/** initializes search (binds to the model)
	 * @param model the model
	 * @see iai.search.Model
	 */
	public MinMax(Model<State,Move> model) {
		this.model = model;
	}
	
	/** chooses move
	 * @param state current state
	 * @param ip number of player, must be 0 or 1
	 * @param depth maximum tree depth, in plies
	 * @return next move
	 */
	public Move choose(State state, int ip, int depth) {
		double vmax = Double.NEGATIVE_INFINITY;
		Move best = null;
		for(Move m: model.moves(state, ip)) {
			double v = minVal(model.succ(state, m), ip, 1-ip,
							  Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
							  depth);
			if(v > vmax) {
				vmax = v; best = m;
			}
		}
		return best;
	}

	/** minValue step of MinMax
	 * @param state current state
	 * @param ip top-level player
	 * @param jp current player
	 * @param alpha alpha, read the book
	 * @param beta beta, read the book
	 * @param depth remaining search depth
	 * @return optimum move value
	 */
	private double minVal(State state, int ip, int jp,
						  double alpha, double beta, int depth) {
		if(model.isGoal(state) || depth==0)
			return model.reward(state, ip);
		double vmin = Double.POSITIVE_INFINITY;
		for(Move m: model.moves(state, jp)) {
			double v = maxVal(model.succ(state, m), ip, 1-jp,
							  alpha, beta, depth-1);
			if(v < vmin)
				vmin = v;
			if(vmin <= alpha)
				break;
			if(v < beta)
				beta = v;
		}
		return vmin;
	}

	/** maxValue step of MinMax
	 * @param state current state
	 * @param ip top-level player
	 * @param jp current player
	 * @param alpha alpha, read the book
	 * @param beta beta, read the book
	 * @param depth remaining search depth
	 * @return optimum move value
	 */
	private double maxVal(State state, int ip, int jp,
						  double alpha, double beta, int depth) {
		if(model.isGoal(state) || depth==0)
			return model.reward(state, ip);
		double vmax = Double.NEGATIVE_INFINITY;
		for(Move m: model.moves(state, jp)) {
			double v = minVal(model.succ(state, m), ip, 1-jp,
							  alpha, beta, depth-1);
			if(v > vmax)
				vmax = v;
			if(vmax >= beta)
				break;
			if(v > alpha)
				alpha = v;
		}
		return vmax;
	}
}
