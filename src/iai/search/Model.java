package iai.search;

import java.util.List; 

/** Search Model abstraction */
public interface Model<State, Move> {
	/** Compute the list of moves in current state
	 * @param state the current state
	 * @return the list of moves 
	 */
	public List<Move> moves(State state, int it);

	/** generate the successor state
	 * @param state the current state
	 * @param move the move
	 * @return the successor state, new allocated
	 */
	public State succ(State state, Move move, int left);

	/** get the state reward, the objective is to maximizes the reward
	 * @param state the state
	 * @return the reward
	 */
	public double reward(State state, int it);
}
