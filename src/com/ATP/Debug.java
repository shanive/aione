
package com.ATP;

public class Debug {
	private static Debug _instance = null;
	private boolean _debug;
	
	private Debug(boolean debug){
		_debug = debug;
	}
	
	public static void initDebug(boolean debug){
		if (_instance == null)
			_instance = new Debug(debug);
	}
	
	public static Debug instance(){
		return _instance;
	}
	
	public boolean isDebugOn(){
		return this._debug;
	}
}

