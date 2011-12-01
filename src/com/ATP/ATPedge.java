package com.ATP;

public class ATPedge {
	
	int source;
	int target;
	int weight;
	boolean flood;
	
	public ATPedge(int s, int t, int w, boolean f){
		this.source = s;
		this.target = t;
		this.weight = w;
		this.flood = f;
	}
	
	public boolean isFlooded()
	{
		return this.flood;
	}
	
	public int getWeight()
	{
		return this.weight;
	}
	
	public int getSource(){
		return this.source;
	}
	
	public int getTarget(){
		return this.target;
	}
	
	public boolean isVertex(int vertex)
	{
		return ((this.source == vertex) || (this.target == vertex));
	}
	
	public String toString(){
		return "("+this.source+", "+this.target+", "+this.weight+", "+this.flood+")";
	}

}
