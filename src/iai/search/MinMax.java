package iai.search;

/** MinMax with alpha-beta pruning */
public class MinMax<State,Move> {
	private final Model<State,Move> model;

	public MinMax(Model<State,Move> model) {
		this.model = model;
	}
	
	public Move choose(State state, int it, int left) {
		double vmax = Double.NEGATIVE_INFINITY;
		Move best = null;
		for(Move m: model.moves(state, it)) {
			double v = minVal(model.succ(state, m, left), it, 1-it,
							  Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
							  left);
			if(v > vmax) {
				vmax = v; best = m;
			}
		}
		return best;
	}

	private double minVal(State state, int it, int jt,
						  double alpha, double beta, int left) {
		if(model.isGoal(state) || left==0)
			return model.reward(state, it);
		if(jt==0)
			--left;
		double vmin = Double.POSITIVE_INFINITY;
		for(Move m: model.moves(state, jt)) {
			double v = maxVal(model.succ(state, m, left), it, 1-jt,
							  alpha, beta, left);
			if(v < vmin)
				vmin = v;
			if(vmin <= alpha)
				break;
			if(v < beta)
				beta = v;
		}
		return vmin;
	}

	private double maxVal(State state, int it, int jt,
						  double alpha, double beta, int left) {
		if(model.isGoal(state) || left==0)
			return model.reward(state, it);
		if(jt==0)
			--left;
		double vmax = Double.NEGATIVE_INFINITY;
		for(Move m: model.moves(state, jt)) {
			double v = minVal(model.succ(state, m, left), it, 1-jt,
							  alpha, beta, left);
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
