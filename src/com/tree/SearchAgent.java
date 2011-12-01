package com.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import com.ATP.*;

/**
 *
 * this class to implement the greedy agent. which means we'll pass to him
 * a heuristic function and then calculate f = h(n).
 *
 * @author ilyad & vered
 *
 */
public class SearchAgent extends Agent {
	HeuristicFunction h;
	LinkedList<ATPmove> actions;
	LinkedList<ATPstate> repeatedStates;
	Comparator<Node> comparator;
	int depth;

	public SearchAgent(int id, int initial, int target,
			HeuristicFunction heuristic, Comparator<Node> compare, int depthSearch){
			this.ID = id;
			this.goal = target;
			this.h = heuristic;
			this.position = initial;
			this.actions = new LinkedList<ATPmove>();
			this.repeatedStates = new LinkedList<ATPstate>();
			this.comparator = compare;
			this.depth = depthSearch;
	}



	@Override
	public ATPmove nextMove(ATPstate state) {
		ATPmove move = this.Search(new ATPstate(state));
		return move;
	}

	private ATPmove Search(ATPstate state){
		BinaryHeap<Node> queue = new BinaryHeap<Node>(); //the constructor should get an array Node[] with all the nodes, and then he'll sort them in O(n).

		//initialization:
		this.actions.clear();
		Node root = new Node(state, null, null, 0.0, 0.0, this.comparator);//the heuristic value is not important because at first iteration this node is pulled out of queue
		this.repeatedStates.add(state);
		queue.add(root);
		int iteration = 0;
		//start search
		while(true){
			if (queue.isEmpty()) return null; //fail
			Node node = queue.remove();
			if ((iteration == this.depth) && (this.depth == 1)) return node.getAction(); //greedy
			//else if (iteration == this.depth) return this.bestMove(node);//TODO rta*
			if (this.goalTest(node.getState())){
				return this.firstAction(node); //backtracking
			}
			this.expand(node, queue);
			iteration += 1;
		}
	}

	private ATPmove firstAction(Node node){
		while ((node != null) && (node.getAction() != null)){
			this.actions.add(0, node.getAction());
			node = node.getParent();
		}
		return this.actions.getFirst();
	}

	private boolean repeated(ATPstate state){
		Iterator<ATPstate> it = this.repeatedStates.iterator();
		while (it.hasNext()){
			if (state.isEqual(it.next()))
				return true;
		}
		return false;
	}

	private Vector<ATPmove> availableMoves(ATPstate state)
	{
		int mypos = state.getAgentPosition(this.ID);
		Vector<ATPmove> avails = new Vector<ATPmove>();
		Iterator<ATPedge> it = ATPgraph.instance().neighboursOfIterate(
												mypos);
		while(it.hasNext()){
			ATPedge edge = it.next();
			Iterator<ATPvehicle> ve = state.iterateVehicleAt(mypos);
			while (ve.hasNext()){
				ATPvehicle vehicle = ve.next();
				if ((!edge.isFlooded()) || (edge.isFlooded() && (vehicle.getEff() > 0)))
					avails.add(new ATPmove(edge.getTarget(), vehicle.getVehicleId()));
			}
		}
		return avails;
	}

	private void expand(Node node, BinaryHeap<Node> queue) {
		ArrayList<Node> successors = new ArrayList<Node>();

		//update heuristic
		Vector<ATPmove> availableMoves = this.availableMoves(node.getState());
		this.h.initHeuristic(node.getState().getAgentPosition(this.ID), availableMoves);
		//start expanding
		while (!availableMoves.isEmpty())
		{
			ATPmove move = availableMoves.remove(0);
			ATPstate state = new ATPstate(node.getState());
			double price = this.simulateMove(state , move) + node.getPathCost();
			if (!this.repeated(state)){
				double h_value = this.h.evaluate(state);
				Node child = new Node(state, move, node, price, h_value, this.comparator);
				successors.add(0, child);
				queue.add(child);
				this.repeatedStates.add(state);
			}
		}
		node.setSuccessors(successors);
	}

	//Assumption: move is legal in state.
	//return move price
	private double simulateMove(ATPstate state, ATPmove move)
	{
		double price = 0.0;
		int agent_pos = state.getAgentPosition(this.getID());
		ATPedge edge = ATPgraph.instance().getEdge(agent_pos, move.getTarget());
		ATPvehicle vehicle = state.getVehicleAt(move.getVehicleID(), agent_pos);
		//if agent switch vehicle, release the old one
		int myVehId = state.agentVehicle(this.ID);
		//change vehicle
		if ((myVehId != -1) && (myVehId != move.getVehicleID())){
			state.setVehicleAvailable(myVehId);
		}
		if (myVehId != move.getVehicleID()){
			price += ATPgraph.instance().getTswitch();
			state.setVehicleOwner(move.getVehicleID(), this.getID());//update vehicle's owner
		}
		//even if agent didn't change vehicle, the vehicle moves with agent
		state.moveVeh(agent_pos, move.getTarget(), move.getVehicleID());//update vehicle's position
		double speed = 0.0;
		if (edge.isFlooded())
			speed = vehicle.speedFlooded();
		else
			speed = vehicle.speedUnflooded();
		state.setAgentPosition(this.getID(), move.getTarget());//update agent's position on state
		price += (edge.getWeight() / speed);
		return price;
	}

	private boolean goalTest(ATPstate state){
		return (state.getAgentPosition(this.ID)==this.goal);
	}

	@Override
	public boolean reachedGoal() {
		return (this.position == this.goal);
	}

}
