package iai.atp;


abstract class Agent {
	abstract Move choose(State state, int it, int left);
}