package iai.atp;


interface Agent {
	Move choose(State state, int it, int left);
}