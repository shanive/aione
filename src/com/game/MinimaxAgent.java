package com.game;

import java.util.Iterator;

import com.ATP.ATPgraph;
import com.ATP.ATPmove;
import com.ATP.ATPstate;
import com.ATP.Agent;
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
		Node current = new Node(state, null, null, 0.0, 0.0, this.comparator);
		
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
		double value = 0.0;
		if (this.terminalState(current.getState())){
				value = this.result(current.getState());
		}
		else if (current.getDepth() == this.depth){
				value = this.EVAL(current);
		}
		else{
			double alpha = currAlpha , beta = currBeta;
			BinaryHeap<Node> queue = new BinaryHeap<Node>();
			value = Double.MIN_VALUE;
			Agent agent = null;
			HeuristicFunction hf = null;
			//if depth is even then it's my turn. else, it's my opponent turn
			if (current.getDepth() % 2 != 0){
				agent = ATPgraph.instance().getAgentByID(this.opponentID);
				hf = this.opponentHeuristic;
			}
			else{
				agent = this;
				hf = this.h;
			}
			current.expand(agent, hf, queue);
	
			while (!queue.isEmpty()){
				Node succ = queue.remove();
				value = Math.max(value, this.minValue(succ, alpha, beta));
				if (value >= beta)
					break;
				alpha = Math.max(alpha, value);
			}
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
		double value = 0.0;
		if (this.terminalState(current.getState())){
				value = this.result(current.getState());
		}
		else if (current.getDepth() == this.depth){
				value = this.EVAL(current);
		}
		else{
			double alpha = currAlpha , beta = currBeta;
			BinaryHeap<Node> queue = new BinaryHeap<Node>();
			value = Double.MAX_VALUE;
			Agent agent = null;
			HeuristicFunction hf = null;
			//if depth is even then it's the opponent turn. else, it's my turn
			if (current.getDepth() % 2 != 0){
				agent = ATPgraph.instance().getAgentByID(this.opponentID);
				hf = this.opponentHeuristic;
			}
			else{
				agent = this;
				hf = this.h;
			}
			current.expand(agent, hf, queue);
	
			while (!queue.isEmpty()){
				Node succ = queue.remove();
				value = Math.min(value, this.maxValue(succ, alpha, beta));
				if (value <= alpha)
					break;
				beta = Math.min(beta, value);
			}
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
