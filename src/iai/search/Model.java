package iai.search;

import java.util.List; 

public interface Model<State, Move> {
	public List<Move> moves(State state);
	public State succ(State state, Move move, int left);
	public double reward(State state);
}
