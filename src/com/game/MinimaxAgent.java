package com.game;

import java.util.Iterator;

import com.ATP.ATPgraph;
import com.ATP.ATPmove;
import com.ATP.ATPstate;
import com.ATP.BinaryHeap;
import com.tree.GreedyHeuristic;
import com.tree.HeuristicFunction;
import com.tree.Node;
import com.tree.SearchAgent;

/**
 * @author Vered
 * Simulation of minimax agent
 * This agent assumes his opponent tries to minimize his score.
 */
public class MinimaxAgent extends SearchAgent{
	protected int opponentID;
	protected GreedyHeuristic opponentHeuristic;

	public MinimaxAgent(int id, int initial, int target, int opponent, int opponentTarget,int cutoff){
		super(id, initial, target, new GreedyHeuristic(id, target), cutoff);
		this.opponentID = opponent;
		this.opponentHeuristic = new GreedyHeuristic(opponent, opponentTarget);
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
			Node succ = it.next();
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
		}else if (current.getDepth() == this.depth){
			return this.EVAL(current);
		}
		double alpha = currAlpha , beta = currBeta;
		double value = Double.MIN_VALUE;
		BinaryHeap<Node> queue = new BinaryHeap<Node>();
		expand(current, queue);

		while (!queue.isEmpty()){
			Node succ = queue.remove();
			value = Math.max(value, this.minValue(succ, alpha, beta));
			if (value >= beta)
				return value;
			alpha = Math.max(alpha, value);
		}
		current.setOptimalCost(value);
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
		}else if (current.getDepth() == this.depth){
			return this.EVAL(current);
		}
		double alpha = currAlpha , beta = currBeta;
		double value = Double.MAX_VALUE;
		BinaryHeap<Node> queue = new BinaryHeap<Node>();
		expand(current, queue);

		while (!queue.isEmpty()){
			Node succ = queue.remove();
			value = Math.min(value, this.maxValue(succ, alpha, beta));
			if (value <= alpha)
				return value;
			beta = Math.min(beta, value);
		}
		current.setOptimalCost(value);
		return value;
	}

	protected double result(ATPstate state){
		return (state.getAgentScore(this.ID) - state.getAgentScore(this.opponentID));
	}

	protected boolean terminalState(ATPstate state){
		return (state.getAgentPosition(this.ID) == this.goal);
	}
	
	
	/**
	 * evaluation function
	 * @param node Current leaf in search tree
	 * @return the estimated value this agent will get in the given leaf.
	 */
	protected double EVAL(Node node){
		ATPstate state = node.getState();
		//opponentCost is the estimated cost of the opponent to reach his goal (f)
		double opponentCost = state.getAgentScore(this.opponentID) +
									this.opponentHeuristic.getShortestDistance(
											state.getAgentPosition(opponentID));
		//myCost is my estimated cost to reach my goal (f)
		double myCost = node.getPathCost() + node.getHeuristicValue();
		return (myCost - opponentCost);
	}
								


}
