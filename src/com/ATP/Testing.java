package com.ATP;
//import java.util.ArrayList;
//import java.util.Collections;
//
//import com.waldura.tw.City;
//import com.waldura.tw.DijkstraEngine;
//

public class Testing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ATPenv env = new ATPenv(args[0], args.length==2);
//		DijkstraEngine.execute(City.valueOf(1), City.valueOf(4));
//		ArrayList<City> l = new ArrayList<City>();
//
//		 for (City city = City.valueOf(4); city != null; city = DijkstraEngine.getPredecessor(city))
//		 {
//		     l.add(city);
//		 }
//
//		 Collections.reverse(l);
		//System.out.println(env);
		 env.RunEnv();
	}

}
