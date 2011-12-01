package com.tree;

import java.util.ArrayList;
import java.util.Comparator;

import com.ATP.ATPmove;
import com.ATP.ATPstate;

public class Node implements Comparable<Node> {
	ATPstate state;
	Node parent;
	ATPmove action;
	double pathCost;
	double heuristicValue;
	int depth;
	ArrayList<Node> successors;
	Comparator<Node> comparator;
	
	public Node(ATPstate currState,ATPmove act, Node faja, double pathcost, double heuristic, Comparator<Node> compare){
		this.state = currState;
		this.action = act;
		this.parent = faja;
		this.pathCost = pathcost;
		this.heuristicValue = heuristic;
		this.depth = (parent == null)? 0 : parent.getDepth()+1;
		this.successors = new ArrayList<Node>();
		this.comparator = compare;
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
		return action;
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
}
