package iai.atp;

/** Abstract agent (a class instead of an interface)
 * to avoid public methods
 */
abstract class Agent {
	protected final World world;

	/** Constructs the Agent */
	Agent(World world) {
		this.world = world;
	}

	/** choose a move 
	 * @param state current state
	 * @param left number of moves left
	 * @return a move
	 */
	abstract Move choose(State state, int it, int left);
}