package iai.atp;

enum Alg { HUMAN, MINMAX, MAXMAX, SUMMAX }

/** Game playing main function */
class Game {
	/** create specific agent
	 * @param alg algorithm
	 * @param world the world
	 * @return an implementation of Agent
	 * @see Agent
	 */
	private static Agent makeAgent(Alg alg, World world) {
		switch(alg) {
		case HUMAN:
			return new HumanAgent(world);
		case MINMAX:
			return null;
		case MAXMAX:
			return null;
		case SUMMAX:
			return null;
		default:
			return null;
		}
	}

	/** print usage information on the standard error */
	private static void usage() {
		System.err.print("Usage:\n"+
						 "\tjava iai.atp.Game [options] map.txt task.txt\n"+
						 "Options:\n"+
						 "\t-h print usage and exit\n"+
						 "\t-d turn on debugging output\n"+
						 "\t-l <nnn> set game length\n"+
						 "\t-1 [HUMAN|MINMAX|MAXMAX|SUMMAX] set first agent\n"+
						 "\t-2 [HUMAN|MINMAX|MAXMAX|SUMMAX] set second agent\n");
	}
						   
    /** command-line driver for the game player */
	public static void main(String[] args) throws java.io.IOException {
		int limit = 10;
		Alg afirst = Alg.HUMAN, asecond = Alg.HUMAN;

		/* parse command line arguments */
		int iarg = 0;
		while(iarg!=args.length) {
			if(args[iarg].compareTo("-h")==0) {
				usage();
				return;
			} else if(args[iarg].compareTo("-d")==0) {
				InOut.DEBUG = true;
			} else if(args[iarg].compareTo("-l")==0) {
				++iarg;
				limit = Integer.parseInt(args[iarg]);
			} else if(args[iarg].compareTo("-1")==0) {
				++iarg;
				afirst = Alg.valueOf(args[iarg]);
			} else if(args[iarg].compareTo("-2")==0) {
				++iarg;
				asecond = Alg.valueOf(args[iarg]);
			} else
				break;
			++iarg;
		}
		if(args.length-iarg!=2) {
			System.err.println("Error: two positional arguments required, "
							   +(args.length-iarg)+" provided.");
			usage();
			return;
		}

		String mapfn = args[args.length-2], taskfn = args[args.length-1];
		World world = InOut.readWorld(mapfn, taskfn);

		/* it takes two to play a game */
		if(world.travellers.length!=2) {
			InOut.printWorld(System.err, world);
			System.err.println("\nError: exactly 2 travellers are required, "
							   +world.travellers.length+" provided.");
			return;
		}

		/* create agents, agents are reusable */
		Agent first = makeAgent(afirst, world);
		Agent second = afirst==asecond? first : makeAgent(asecond, world);
		Agent[] agents = {first, second};

		Tour tour = new Tour(world);
		State state = tour.go(agents, limit);
		System.out.println("final state: "+state);
	}
		
}