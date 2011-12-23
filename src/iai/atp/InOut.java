package iai.atp;

import java.util.LinkedList;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
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
		BufferedReader mapr =
			new BufferedReader(new FileReader(mapfn));
		World world = new World(readGraph(mapr), readVehicles(mapr));
		mapr.close();
		return world;
	}

	static Graph<World.Road> readGraph(BufferedReader mapr) throws IOException {
		Graph<World.Road> graph;

		/* graph size */
		for(;;) {
			String[] toks = mapr.readLine().split(" ");
			if(toks.length==0) /* empty line */
				continue;
			if(toks[0].compareTo("#V")==0) { /* number of nodes */
				graph = new Graph<World.Road>(Integer.parseInt(toks[1]));
				break;
			}
		}
		assert graph!=null;

		/* edges */ 
		for(;;) {
			mapr.mark(readAheadLimit); /* mark before reading so that vehicles can be read */
			String line = mapr.readLine();
			if(line==null)
				break;
			String[] toks = line.split(" ");
			if(toks.length==0) /* empty line */
				continue;
			if(toks[0].compareTo("#E")==0) {
				int u = Integer.parseInt(toks[1]);
				int v = Integer.parseInt(toks[2]);
				double weight = Double.parseDouble(toks[3].substring(1));
				boolean clear = toks[3].compareTo("C")==0;

				/* add neighbors in both directions */
				graph.edge(u, new World.Road(v, weight, clear));
				graph.edge(v, new World.Road(u, weight, clear));
			} else { /* probably vehicle specification, reset and exit */
				mapr.reset();
				break;
			}
		}

		return graph;
	}

	static World.Vehicle[] readVehicles(BufferedReader mapr) throws IOException {
		LinkedList<World.Vehicle> vehicles = new LinkedList<World.Vehicle>();
		for(;;) {
			String line = mapr.readLine();
			if(line==null)
				break; /* end of file, all done */
			String[] toks = line.split(" ");
			if(toks.length==0) /* empty line */
				continue;
			assert toks[0].compareTo("#V")==0; /* only vehicles are left */
			int garage = Integer.parseInt(toks[1]);
			double cspeed = Double.parseDouble(toks[2]);
			double fspeed = cspeed*Double.parseDouble(toks[3]);
			vehicles.add(new World.Vehicle(garage, cspeed, fspeed));
		}
		return (World.Vehicle[])vehicles.toArray();
	}

	/**
	 * outputs the debugging message, if the debugging is on 
	 * @param message the message to output
	 */
	static void debug(String message) {
		if(DEBUG) 
			System.err.println("DEBUG: "+message);
	}
}
	

