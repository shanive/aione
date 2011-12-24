package iai.atp;

/** a move in the current state */
final class Move {
	/** traveller index */
	final int it;
	/** target node */
	final int v;
	/** vehicle index */
	final int iv;

	Move(int it, int v, int iv) {
		this.it = it; this.v = v; this.iv = iv;
	}
}