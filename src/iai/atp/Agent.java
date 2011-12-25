package iai.atp;

/** Abstract agent (a class instead of an interface)
 * to avoid public methods
 */
abstract class Agent {
	protected final World world;
	protected final int it;

	/** Constructs the Agent */
	Agent(World world, int it) {
		this.world = world;
		this.it = it;
	}

	/** choose a move 
	 * @param state current state
	 * @param left number of moves left
	 * @return a move
	 */
	abstract Move choose(State state, int left);
}