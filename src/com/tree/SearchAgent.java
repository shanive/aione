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
	LinkedList<ATPmove> actions = null;
	Comparator<Node> comparator;
	int depth;
	public int expandCount;

	public SearchAgent(int id, int initial, int target,
			HeuristicFunction heuristic, int depthSearch){
			this.ID = id;
			this.goal = target;
			this.h = heuristic;//initialized
			this.position = initial;
			this.actions = null;
			this.comparator = new NodesComparator();
			this.depth = depthSearch;
			this.expandCount = 0;
	}



	@Override
	public ATPmove nextMove(ATPstate state) {
		ATPmove move = this.Search(new ATPstate(state));
		return move;
	}

	private ATPmove Search(ATPstate state){
		BinaryHeap<Node> queue = new BinaryHeap<Node>(); //the constructor should get an array Node[] with all the nodes, and then he'll sort them in O(n).

		if(this.depth==-1 && this.actions != null) {
			try {
				return actions.remove(0);
			} catch(IndexOutOfBoundsException e) {
				return null;
			}
		}

		//initialization:
		Node root = new Node(state, null, null, 0.0, 0.0, this.comparator);//the heuristic value is not important because at first iteration this node is pulled out of queue
		queue.add(root);
		int iteration = 0;
		//start search
		while(true){
			if (queue.isEmpty()) return null; //fail
			Node node = queue.remove();
			if (Debug.instance().isDebugOn()){
				System.out.println("entering state with node="+node.getState().getAgentPosition(this.ID)+" into repeated states");
			}
				if (iteration == this.depth || this.goalTest(node.getState())){
				return this.firstAction(node); //backtracking
			}
			this.expand(node, queue);
			iteration += 1;
		}
	}

	private ATPmove firstAction(Node node){
		this.actions = new LinkedList<ATPmove>();
		while ((node != null) && (node.getAction() != null)){
			this.actions.add(0, node.getAction());
			node = node.getParent();
		}
		return this.actions.remove(0);
	}

	protected boolean repeated(ATPstate state){
		Iterator<ATPstate> it = this.repeatedStates.iterator();
		while (it.hasNext()){
			if (state.isEqual(it.next()))
				return true;
		}
		return false;
	}

	protected Vector<ATPmove> availableMoves(ATPstate state)
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

	protected void expand(Node node, BinaryHeap<Node> queue) {
		ArrayList<Node> successors = new ArrayList<Node>();
		this.expandCount += 1; 
		//update heuristic
		Vector<ATPmove> availableMoves = this.availableMoves(node.getState());
		//start expanding
		while (!availableMoves.isEmpty())
		{
			ATPmove move = availableMoves.remove(0);
			ATPstate state = new ATPstate(node.getState());
			double h_value = this.h.evaluate(state, move);
			double price = this.simulateMove(state , move) + node.getPathCost();
			if (Debug.instance().isDebugOn()){
				System.out.println("target="+move.getTarget()+" price="+price+" h_value="+h_value);
			}
			if (!this.repeated(state)){
				Node child = new Node(state, move, node, price, h_value, this.comparator);
				successors.add(0, child);
				queue.add(child);
			} else if (Debug.instance().isDebugOn()){
				
				System.out.println("repeated state on move to "+move.getTarget());
			}
		}
		node.setSuccessors(successors);
	}

	//Assumption: move is legal in state.
	//return move price
	protected double simulateMove(ATPstate state, ATPmove move)
	{
		double cost = 0.0;
		double cost_switch = 0.0;
		double cost_move = 0.0;
		int agent_pos = state.getAgentPosition(this.getID());
		ATPedge edge = ATPgraph.instance().getEdge(agent_pos, move.getTarget());
		ATPvehicle vehicle = state.getVehicleAt(move.getVehicleID(), agent_pos);
		int myVehId = state.agentVehicle(this.ID);
		//release old vehicle
		if ((myVehId != -1) && (myVehId != move.getVehicleID())){
			state.setVehicleAvailable(myVehId, this.getID());
		}
		//switch
		if (myVehId != move.getVehicleID()){
			cost_switch += ATPgraph.instance().getTswitch();
			state.setAgentVehicle(this.getID(), move.getVehicleID());//update vehicle's owner
		}
		//even if agent didn't change vehicle, the vehicle moves with agent
		state.moveVeh(agent_pos, move.getTarget(), move.getVehicleID());//update vehicle's position
		state.setAgentPosition(this.getID(), move.getTarget());//update agent's position on state

		double speed = 0.0;
		if (edge.isFlooded())
			speed = vehicle.speedFlooded();
		else
			speed = vehicle.speedUnflooded();

		cost_move += ((double)edge.getWeight() / speed);

		cost = cost_switch + cost_move;
		
		if (Debug.instance().isDebugOn()){
			System.out.println("target="+move.getTarget()+
				" edge_weight(raw)="+edge.getWeight()+" speed_flooded="+vehicle.speedFlooded()+" cost="+cost+" cost_switch="+cost_switch+" cost_move="+cost_move);
		}
		return cost;
	}

	private boolean goalTest(ATPstate state){
		return (state.getAgentPosition(this.ID)==this.goal);
	}

	@Override
	public boolean reachedGoal() {
		return (this.position == this.goal);
	}

}
