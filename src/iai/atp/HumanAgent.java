package iai.atp;

import java.util.List;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/** Human agent */
class HumanAgent extends Agent {
	HumanAgent(World world) {
		super(world);
	}

	Move choose(State state, int it, int left) {
		/* retrieve all available moves */
		List<Move> ml = state.moves(it);
		Move[] moves = new Move[ml.size()];
		moves = ml.toArray(moves);

		/* print current state and moves */
		System.out.println("moves for t"+(it+1)+":");
		for(int im=0; im!=moves.length; ++im) {
			System.out.println((im+1)+". "+moves[im]+"/"+state.cost(moves[im], left));
		}

		/* let the human choose a move by the number */
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			for(;;) {
				System.out.print("choose move: ");
				int im;
				try {
					im = Integer.parseInt(in.readLine());
				} catch(NumberFormatException e) {
					im = 0;
				}
				if(0<im && im<=moves.length)
					return moves[im-1];
				System.out.print("illegal choice, ");
			}
		} catch(IOException e) {
			/* this should not really happen, but if the user
			   closes the input forcefully, select the first available
			   move */
			return moves[0];
		}
	}
}