package com.tree;

import java.util.Comparator;
import java.util.LinkedList;

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
	protected HeuristicFunction h;
	protected LinkedList<ATPmove> actions = null;
	protected Comparator<Node> comparator;
	protected int depth;
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
			this.expandCount++;
			node.expand(this, this.h, queue);
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


	private boolean goalTest(ATPstate state){
		return (state.getAgentPosition(this.ID)==this.goal);
	}

	@Override
	public boolean reachedGoal() {
		return (this.position == this.goal);
	}

}
