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
		State state = new State(world);
		for(int left=limit; left!=0; --left) {
			for(int it = 0; it!=world.travellers.length; ++it) {
					/* mostly for convenience with interactive agents:
					   ask for a move only if not yet
					   in the target node */
				if(state.tloc[it]!=world.travellers[it].t) {
					Move m = agents[it].choose(state, it, left);
					assert state.isLegal(m);
					state = new State(state, m, left);
					if(state.isGoal())
						return state;
				}
			}
		}
		return state;
	}
}
