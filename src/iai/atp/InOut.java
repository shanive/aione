package iai.atp;

/**
 * Static routines for input and output
 */
class InOut {
	static boolean DEBUG = false;

	/**
	 * Reads the world map, agent types and tasks
	 */
	static World readWorld(String mapfn, String taskfn) {
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
	

