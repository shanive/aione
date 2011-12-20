package com.game;

import java.util.Iterator;
import com.ATP.ATPmove;
import com.ATP.ATPstate;
import com.ATP.BinaryHeap;
import com.tree.GreedyHeuristic;
import com.tree.Node;
import com.tree.SearchAgent;

/**
 * @author Vered
 * Simulation of minimax agent
 * This agent assumes his opponent tries to minimize his score.
 */
public class MinimaxAgent extends SearchAgent{
	private int opponentID;

	public MinimaxAgent(int id, int initial, int target, int opponent, int cutoff){
		super(id, initial, target, new GreedyHeuristic(id, target), cutoff);
		this.opponentID = opponent;
	}

	@Override
	public ATPmove nextMove(ATPstate state) {
		return this.alphaBetaSearch(state);
	}

	/**
	 * performs an alpha beta search from a given state.
	 * @param state the current state of the game
	 * @return the best next move
	 */
	protected ATPmove alphaBetaSearch(ATPstate state){
		double value = 0;
		ATPmove move = null;
		Node current = new Node(state, null, null, 0.0, 0.0, null);
		value = this.maxValue(current, Double.MIN_VALUE, Double.MAX_VALUE);
		Iterator<Node> it = current.getSuccessors().iterator();
		while (it.hasNext()){
			Node succ = (GameNode)it.next();
			if (succ.getOptimalCost() == value){
				move = succ.getAction();
				break;
			}
		}
		return move;
	}
	/**
	 * @param current Current state of the game
	 * @param currAlpha current known minimal value this agent can get
	 * @param currBeta current known maximal value this agent can get
	 * @return maximal value this agent can get from a given state.
	 */
	protected double maxValue(Node current, double currAlpha, double currBeta){
		if (this.terminalState(current.getState())){
				return current.getHeuristicValue();
		}
		double alpha = currAlpha , beta = currBeta;
		double value = Double.MIN_VALUE;
		BinaryHeap<Node> queue = new BinaryHeap<Node>();
		expand(current, queue);

		while (!queue.isEmpty()){
			Node succ = queue.remove();
			value = Math.max(value, this.minValue(succ, alpha, beta));
			current.setOptimalCost(value);
			if (value >= beta)
				return value;
			alpha = Math.max(alpha, value);
		}
		return value;
	}
	/**
	 * @param current Current state of the game
	 * @param currAlpha current known minimal value this agent can get
	 * @param currBeta current known maximal value this agent can get
	 * @return minimal value this agent can get from a given state.
	 */
	protected double minValue(Node current, double currAlpha, double currBeta){
		if (this.terminalState(current.getState())){
				return this.result(current.getState());
		}
		double alpha = currAlpha , beta = currBeta;
		double value = Double.MAX_VALUE;
		BinaryHeap<Node> queue = new BinaryHeap<Node>();
		expand(current, queue);

		while (!queue.isEmpty()){
			Node succ = queue.remove();
			value = Math.min(value, this.maxValue(succ, alpha, beta));
			current.setOptimalCost(value);
			if (value <= alpha)
				return value;
			beta = Math.min(beta, value);
		}
		return value;
	}

	protected double result(ATPstate state){
		return (state.getAgentScore(this.ID) - state.getAgentScore(this.opponentID));
	}

	protected boolean terminalState(ATPstate state){
		return (state.getAgentPosition(this.ID) == this.goal);
	}


}
