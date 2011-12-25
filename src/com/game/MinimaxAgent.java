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
	protected int opponentGoal;
	protected GreedyHeuristic opponentHeuristic;

	public MinimaxAgent(int id, int initial, int goal, int opponent, int opponentGoal,int cutoff){
		super(id, initial, goal, new GreedyHeuristic(id, goal), cutoff);
		this.opponentID = opponent;
		this.opponentGoal = opponentGoal;
		this.opponentHeuristic = new GreedyHeuristic(opponent, opponentGoal);
	}

	@Override
	public ATPmove nextMove(ATPstate state) {
		return alphaBetaSearch(state);
	}

	/**
	 * performs an alpha beta search from a given state.
	 * @param state the current state of the game
	 * @return the best next move
	 */
	protected ATPmove alphaBetaSearch(ATPstate state){
		double value = 0;
		ATPmove move = null;
		Node current = new Node(state, null, null, 0.0, 0.0, comparator);
		
		value = maxValue(current, -Double.MAX_VALUE, Double.MAX_VALUE);
	   
		Iterator<Node> it = current.getSuccessors().iterator();
		System.out.println("begin ALPHA-BETA");
		while (it.hasNext()){
			Node succ = it.next();
			succ.getState().printer();
			System.out.println("optimal-cost="+succ.getOptimalCost()+" value="+value);
			if (succ.getOptimalCost() == value){
				move = succ.getAction();
				// break;
			}
		}
		System.out.println("end ALPHA-BETA");
		return move;
	}

	/**
	 * @param current Current state of the game
	 * @param currAlpha current known minimal value this agent can get
	 * @param currBeta current known maximal value this agent can get
	 * @return maximal value this agent can get from a given state.
	 */
	private double boundValue(Node current, double currAlpha, double currBeta) {
		double value = 0.0;
		//if depth is even then it's my turn. else, it's my opponent turn
		boolean isOpponent = current.getDepth() % 2 != 0;
		System.out.println("depth="+current.getDepth()+" opponent="+isOpponent);
		if (terminalState(current.getState())){
				value = result(current.getState());
				System.out.println("value="+value+" in terminalState:");
				current.getState().printer();
		}
		else if (current.getDepth() == depth){
				value = EVAL(current);
				System.out.println("value="+value+" in leaf State:");
				current.getState().printer();
		}
		else{
			double alpha = currAlpha, beta = currBeta;
			BinaryHeap<Node> queue = new BinaryHeap<Node>();
			Agent agent = null;
			HeuristicFunction hf = null;
			if (isOpponent) {
				value = Double.MAX_VALUE;
				agent = ATPgraph.instance().getAgentByID(opponentID);
				hf = opponentHeuristic;
			}
			else{ 
				value = -Double.MAX_VALUE;
				agent = this;
				hf = this.h;
			}
			current.expand(agent, hf, queue);
	
			if(isOpponent) {	
				while (!queue.isEmpty()){
					Node succ = queue.remove();
					System.out.println("Opponent moves to state:");
					succ.getState().printer();
					value = Math.min(value, maxValue(succ, alpha, beta));
					if (value <= alpha)
						break;
					beta = Math.min(beta, value);
				}
			} else {
				while (!queue.isEmpty()){
					Node succ = queue.remove();
					System.out.println("I move to state:");
					succ.getState().printer();
					value = Math.max(value, minValue(succ, alpha, beta));
					if (value >= beta)
						break;
					alpha = Math.max(alpha, value);
				}
			}
		}
		current.setOptimalCost(value);
		return value;
	}

	/**
	 * @param current Current state of the game
	 * @param currAlpha current known minimal value this agent can get
	 * @param currBeta current known maximal value this agent can get
	 * @return maximal value this agent can get from a given state.
	 */
	protected double maxValue(Node current, double currAlpha, double currBeta){
		return boundValue(current, currAlpha, currBeta);
	}
	/**
	 * @param current Current state of the game
	 * @param currAlpha current known minimal value this agent can get
	 * @param currBeta current known maximal value this agent can get
	 * @return minimal value this agent can get from a given state.
	 */
	protected double minValue(Node current, double currAlpha, double currBeta){
		return boundValue(current, currAlpha, currBeta);
	}

	protected double result(ATPstate state){
		return (state.getAgentScore(opponentID) - state.getAgentScore(ID));
	}

	protected boolean terminalState(ATPstate state){
		return (state.getAgentPosition(ID) == goal
				&& state.getAgentPosition(opponentID)==opponentGoal);
	}
	
	
	/**
	 * evaluation function
	 * @param node Current leaf in search tree
	 * @return the estimated value this agent will get in the given leaf.
	 */
	protected double EVAL(Node node){
		ATPstate state = node.getState();
		//opponentCost is the estimated cost of the opponent to reach his goal (f)
		double opponentCost = state.getAgentScore(opponentID) +
									opponentHeuristic.getShortestDistance(
											state.getAgentPosition(opponentID));
		//myCost is my estimated cost to reach my goal (f)
		double myCost = state.getAgentScore(ID) + h.getShortestDistance(state.getAgentPosition(ID));
		System.out.println("opponentCost="+opponentCost+" myCost="+myCost);
		return (opponentCost - myCost);
	}
								


}
