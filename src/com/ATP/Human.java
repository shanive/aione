package com.ATP;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Simulate a human agent that receives -
 * the next move from the user.
 */
public class Human extends Agent {
	
	public Human(int id, int initial, int goalv){
		this.ID = id;
		this.position = initial;
		this.goal = goalv;
		this.vehicleId = -1;
	}
	
	public String toString(){
		return "human("+this.ID+", "+this.goal+")";
	}

	@Override
	/**
	 * @param state The current state.
	 * @return Next move
	 */
	public ATPmove nextMove(ATPstate state) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String[] tokens;
		//Display state to the user
		state.printer();
		System.out.println("Human, enter your next move: <vertex> <vehicle>"); 
		try {
			tokens = br.readLine().split(" ");
			if (tokens.length < 2){
				System.out.println("Missing Argument");
				System.exit(1);
			}
			return new ATPmove(Integer.parseInt(tokens[0]),
								Integer.parseInt(tokens[1]));
		} catch (IOException e) {
			System.out.println("Human: Exception while read from stdin.");
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean reachedGoal() {
		return (this.position == this.goal);
	}
	
	
}
