package iai.search;

import java.util.List; 

/** Search Model abstraction */
public interface Model<State, Move> {
	/** Compute the list of moves in current state
	 * @param state the current state
	 * @param ip player index
	 * @return the list of moves 
	 */
	public List<Move> moves(State state, int ip);

	/** generate the successor state
	 * @param state the current state
	 * @param move the move
	 * @return the successor state, newly allocated
	 */
	public State succ(State state, Move move);

	/** goal test
	 * @param state current state
	 * @return true when goal state
	 */
	public boolean isGoal(State state);

	/** get the state reward, the objective is to maximizes the reward
	 * @param state the state
	 * @param ip the player for which the reward is computed
	 * @return the reward
	 */
	public double reward(State state, int ip);
}
