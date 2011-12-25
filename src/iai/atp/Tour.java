package iai.atp;

class Tour {
	private final World world;

	/** Creates a tour `option' around the world
	 * @param world the world
	 */
	Tour(World world) {
		this.world = world;
	}

	/** Runs the tour in the world for at most limit steps
	 * @param agents array of agents, one agent per traveller
	 * @param limit maximum number of steps
	 * @return final state
	 */
	State go(Agent[] agents, int limit) {
		State state = new State(world, limit);
		while(limit--!=0) {
			for(int it = 0; it!=world.travellers.length; ++it) {
					/* mostly for convenience with interactive agents:
					   ask for a move only if not yet
					   in the target node */
				if(state.tloc[it]!=world.travellers[it].t) {
					System.out.println("state: "+state);
					Move m = agents[it].choose(state, it);
					assert state.isLegal(m);
					System.out.println("move: "+m);
					state = new State(state, m);
					if(state.isGoal())
						return state;
				}
			}
		}
		return state;
	}
}
