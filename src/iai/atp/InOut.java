package iai.atp;

import java.util.LinkedList;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintStream;
import iai.lib.Graph;

/**
 * Static routines for input and output
 */
class InOut {
	/** when true, debugging output is printed */
	static boolean DEBUG = false;

	/** read ahead limit for mark/reset */
	private static final int readAheadLimit = 1024;

	/**
	 * Reads the world map, agent types and tasks
	 * @param mapfn map file name
	 * @param taskfn task file name
	 * @return World object
	 * @see World
	 */
	static World readWorld(String mapfn, String taskfn) throws IOException {
		/* read the map */
		BufferedReader mapr = new BufferedReader(new FileReader(mapfn));
		Graph<World.Road> roads = readGraph(mapr);
		World.Vehicle[] vehicles = readVehicles(mapr);
		mapr.close();

		/* read the task */
		BufferedReader taskr = new BufferedReader(new FileReader(taskfn));
		World.Traveller[] travellers = readTravellers(taskr);
		taskr.close();
		
		/* create the world */
		World world = new World(roads, vehicles, travellers);
		return world;
	}

	/** checks whether the line is a comment (empty or starts with %)
	 * @param line the line
	 * @return true when comment
	 */
	private static boolean isCommentLine(String line) {
		line = line.trim();
		return line.length()==0 || line.startsWith("%");
	}
	
	/** reads the graph of roads 
	 * @param mapr open BufferedReader
	 * @return road graph
	 */
	private static Graph<World.Road> readGraph(BufferedReader mapr) throws IOException {
		Graph<World.Road> roads;

		/* graph size */
		for(;;) {
			String line = mapr.readLine();
			if(isCommentLine(line))
				continue;
			String[] toks = line.split(" ");
			if(toks[0].compareTo("#V")==0) { /* number of nodes */
				roads = new Graph<World.Road>(Integer.parseInt(toks[1]));
				break;
			}
		}
		assert roads!=null;

		/* edges */ 
		for(;;) {
			mapr.mark(readAheadLimit); /* mark before reading so that vehicles can be read */
			String line = mapr.readLine();
			if(line==null)
				break;
			if(isCommentLine(line))
				continue;
			String[] toks = line.split(" ");
			if(toks[0].compareTo("#E")==0) {
				int u = Integer.parseInt(toks[1])-1;
				int v = Integer.parseInt(toks[2])-1;
				double weight = Double.parseDouble(toks[3].substring(1));
				boolean clear = toks[4].compareTo("C")==0;

				/* add neighbors in both directions */
				roads.edge(u, new World.Road(v, weight, clear));
				roads.edge(v, new World.Road(u, weight, clear));
			} else { /* probably vehicle specification, reset and exit */
				mapr.reset();
				break;
			}
		}

		return roads;
	}

	/** 
	 * reads of vehicles
	 * @param mapr open buffered reader positioned at the first vehicle
	 * @return the array of vehicles
	 */
	private static World.Vehicle[] readVehicles(BufferedReader mapr) throws IOException {
		LinkedList<World.Vehicle> vl = new LinkedList<World.Vehicle>();
		for(;;) {
			String line = mapr.readLine();
			if(line==null)
				break; /* end of file, all done */
			String[] toks = line.split(" ");
			if(isCommentLine(line))
				continue;
			assert toks[0].compareTo("#V")==0; /* only vl are left */
			int garage = Integer.parseInt(toks[1])-1;
			double cspeed = Double.parseDouble(toks[2]);
			double fspeed = cspeed*Double.parseDouble(toks[3]);
			vl.add(new World.Vehicle(garage, cspeed, fspeed));
		}
		World.Vehicle[] vehicles = new World.Vehicle[vl.size()];
		return vl.toArray(vehicles);
	}

	/** Read the  travellers, provided in a separate file (from the world map)
	 * @param taskr open buffered reader
	 * @return the array of travellers
	 */
	private static World.Traveller[] readTravellers(BufferedReader taskr) throws IOException {
		LinkedList<World.Traveller> tl = new LinkedList<World.Traveller>();
		for(;;) {
			String line = taskr.readLine();
			if(line==null)
				break; /* end of file, all done */
			String[] toks = line.split(" ");
			if(isCommentLine(line))
				continue;
			assert toks[0].compareTo("#T")==0;
			int s = Integer.parseInt(toks[1])-1;
			int t = Integer.parseInt(toks[2])-1;
			double cswitch = Double.parseDouble(toks[3]);
			tl.add(new World.Traveller(s, t, cswitch));
		}
		World.Traveller[] travellers = new World.Traveller[tl.size()];
		return tl.toArray(travellers);
	}

	/**
	 * dump the world description, for debugging and control 
	 * @param out output print stream, System.out for example
	 * @param world the world to dump
	 */
	static void printWorld(PrintStream out, World world) throws IOException {
		out.println("% Roads\n");
		out.println("#V "+world.roads.size+"\n");
		for(int u = 0; u!=world.roads.size; ++u) {
			for(World.Road r : world.roads.neighbors(u)) {
				if(r.v > u) 
					out.println("#E "+(u+1)+" "+(r.v+1)+" W"+r.weight+" "+(r.clear?"C":"F"));
			}
		}
		out.println("\n% Vehicles\n");
		for(World.Vehicle v: world.vehicles) {
			out.println("#V "+(v.garage+1)+" "+v.cspeed+" "+(v.fspeed/v.cspeed));
		}
		out.println("\n% Travellers\n");
		for(World.Traveller t: world.travellers) {
			out.println("#T "+(t.s+1)+" "+(t.t+1)+" "+t.cswitch);
		}
	}

	/**
	 * outputs the debugging message if the debugging is on.
	 * @param message the message to output
	 */
	static void debug(String message) {
		if(DEBUG) 
			System.err.println("DEBUG: "+message);
	}

	/** reads and dumps the world, for debugging */
	public static void main(String[] args) throws IOException {
		assert args.length==2;
		World world = readWorld(args[0], args[1]);
		printWorld(System.out, world);

		State state = new State(world);
		System.out.println("\n% initial state: "+state);
	}
}
	

