/**
 *
 */
package com.tree;

import java.util.Vector;

import com.ATP.ATPstate;
import com.ATP.ATPmove;
/**
 * @author ilyad
 *
 */
public interface HeuristicFunction {
	/**
	 * this function evaluates a certain state and returns the evaluation score.
	 *
	 * @param state the state we want to evaluate
	 * @return the evaluation score
	 */
	public double evaluate(ATPstate state);

	public void initHeuristic(int source, Vector<ATPmove> availableMoves);

}
