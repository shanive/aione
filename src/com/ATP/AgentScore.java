package src.com.ATP;

public class AgentScore {
	int steps;
	double totaltime;
	
	public AgentScore()
	{
		this.steps = 0;
		this.totaltime = 0.0;
	}
	
	public void addStep()
	{
		this.steps += 1;
	}
	
	public void addTime(double time)
	{
		this.totaltime += time;
	}
	
	public int getSteps()
	{
		return this.steps;
	}
	
	public double getTime(){
		return this.totaltime;
	}
	
	
}
