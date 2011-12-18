package com.game;

import java.util.Iterator;
import com.ATP.ATPmove;
import com.ATP.ATPstate;
import com.ATP.BinaryHeap;
import com.tree.Node;
import com.tree.SearchAgent;

/**
 * @author Vered
 * Simulation of minimax agent 
 * This agent assumes his opponent tries to minimize his score.
 */
public class MinimaxAgent extends SearchAgent{
	
	public MinimaxAgent(int id, int initial, int target, GameHeuristic func, int depth){
		super(id, initial, target, func, depth);
	}

	@Override
	public ATPmove nextMove(ATPstate state) {
		return this.alphaBetaSearch(state);
	}
	
	protected ATPmove alphaBetaSearch(ATPstate state){
		double value = 0;
		ATPmove move = null;
		Node current = new Node(state, null, null, 0.0, 0.0, null);
		value = this.maxValue(current, Double.MIN_VALUE, Double.MAX_VALUE);
		Iterator<Node> it = current.getSuccessors().iterator();
		while (it.hasNext()){
			Node succ = it.next();
			if (succ.getHeuristicValue() == value){
				move = succ.getAction();
				break;
			}
		}
		return move;
	}
	
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
			if (value >= beta)
				return value;
			alpha = Math.max(alpha, value);
		}
		return value;
	}
	
	protected double minValue(Node current, double currAlpha, double currBeta){
		if (this.terminalState(current.getState())){
				return current.getHeuristicValue();
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
		return value;
	}
	
	protected boolean terminalState(ATPstate state){
		return (state.getAgentPosition(this.ID) == this.goal);
	}

	@Override
	public boolean reachedGoal() {
		// TODO Auto-generated method stub
		return false;
	}

}
