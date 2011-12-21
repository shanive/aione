package com.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Vector;

import com.ATP.ATPmove;
import com.ATP.ATPstate;
import com.ATP.Agent;
import com.ATP.BinaryHeap;
import com.ATP.Debug;

public class Node implements Comparable<Node> {
	ATPstate state;
	Node parent;
	ATPmove action;
	double pathCost;
	double heuristicValue;
	int depth;
	ArrayList<Node> successors;
	Comparator<Node> comparator;
	double optimalCost; //for games

	public Node(ATPstate currState,ATPmove act, Node faja, double pathcost, double heuristic, Comparator<Node> compare){
		this.state = currState;
		this.action = act;
		this.parent = faja;
		this.pathCost = pathcost;
		this.heuristicValue = heuristic;
		this.depth = (parent == null)? 0 : parent.getDepth()+1;
		this.successors = new ArrayList<Node>();
		this.comparator = compare;
		this.optimalCost = 0.0;
	}

	/**
	 * @return the state
	 */
	public ATPstate getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(ATPstate state) {
		this.state = state;
	}
	/**
	 * @return the parent
	 */
	public Node getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}
	/**
	 * @return the action
	 */
	public ATPmove getAction() {
		return this.action;
	}
	/**
	 *
	 * @return the optimal cost of the path from initial vertex
	 * to goal vertex through this for optimal game (minimax)
	 */
	public double getOptimalCost(){
		return this.optimalCost;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(ATPmove action) {
		this.action = action;
	}
	/**
	 * @return the pathCost
	 */
	public double getPathCost() {
		return pathCost;
	}
	/**
	 * @param pathCost the pathCost to set
	 */
	public void setPathCost(double pathCost) {
		this.pathCost = pathCost;
	}
	/**
	 * set the optimal cost of the path from initial to goal through this.
	 * @param optCost the new optimal cost.
	 */
	public void setOptimalCost(double optCost){
		this.optimalCost = optCost;
	}
	/**
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}
	/**
	 * @param depth the depth to set
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}
	/**
	 * @return the successors
	 */
	public ArrayList<Node> getSuccessors() {
		return successors;
	}
	/**
	 * @param successors the successors to set
	 */
	public void setSuccessors(ArrayList<Node> successors) {
		this.successors = successors;
	}

	public double getHeuristicValue(){
		return this.heuristicValue;
	}

	public void setHeuristicValue(double score){
		this.heuristicValue = score;
	}

	@Override
	public int compareTo(Node o) {
		return this.comparator.compare(this, o);
	}
	
	/**
	 * expand this node.
	 * @param agentid
	 * @param node
	 * @param queue
	 */
	public void expand(Agent agent, HeuristicFunction func, BinaryHeap<Node> queue) {
		int agentid = agent.getID(); 
		ArrayList<Node> successors = new ArrayList<Node>();
		//update heuristic
		Vector<ATPmove> availableMoves = this.state.availableMovesOf(agentid);
		//start expanding
		while (!availableMoves.isEmpty())
		{
			ATPmove move = availableMoves.remove(0);
			ATPstate nextstate = new ATPstate(this.state);
			double h_value = func.evaluate(this.state, move);
			nextstate.agentMove(agent, move); //move is legal
			if (Debug.instance().isDebugOn()){
				System.out.println("target="+move.getTarget()+
						" price="+nextstate.getAgentScore(agentid)+" h_value="+h_value);
			}
			if (!agent.isRepeatedState(nextstate)){
				Node child = new Node(nextstate, move, this, nextstate.getAgentScore(agentid), 
						h_value, this.comparator);
				successors.add(0, child);
				queue.add(child);
			} else if (Debug.instance().isDebugOn()){
				System.out.println("repeated state on move to "+move.getTarget());
			}
		}
		this.setSuccessors(successors);
	}
}
