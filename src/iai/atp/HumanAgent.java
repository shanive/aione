package iai.atp;

import java.util.List;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/** Human agent */
class HumanAgent extends Agent {
	HumanAgent() {}

	Move choose(State state, int it, int left) {
		/* retrieve all available moves */
		List<Move> ml = state.moves(it);
		Move[] moves = new Move[ml.size()];
		moves = ml.toArray(moves);

		/* print current state and moves */
		System.out.println("state: "+state);
		System.out.println("moves:");
		for(int im=0; im!=moves.length; ++im) {
			System.out.println((im+1)+". "+moves[im]+"/"+state.cost(moves[im], left));
		}

		/* let the human choose a move by the number */
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			for(;;) {
				System.out.print("choose move: ");
				int im = Integer.parseInt(in.readLine());
				if(0<im || im<=moves.length)
					return moves[im-1];
				System.out.print("illegal choice, ");
			}
		} catch(Exception e) {
			/* this should not really happen, but if the user
			   closes the input forcefully, select the first available
			   move */
			return moves[0];
		}
	}
}