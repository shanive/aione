package com.game;

import java.util.Comparator;

import com.ATP.ATPmove;
import com.ATP.ATPstate;
import com.tree.Node;

public class GameNode extends Node{
private double optimalCost;

	public GameNode(ATPstate currState, ATPmove act, Node faja,
			double pathcost, double heuristic, Comparator<Node> compare) {
		super(currState, act, faja, pathcost, heuristic, compare);
		this.optimalCost = 0.0;
	}

	public double getOptimalCost(){
		return this.optimalCost;
	}

	public void setOptimalCost(double cost){
		this.optimalCost = cost;
	}

}
