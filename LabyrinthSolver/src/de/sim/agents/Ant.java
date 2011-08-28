package de.sim.agents;

import sim.engine.*;
import sim.util.Int2D;

import java.awt.Point;

import de.sim.state.AntState;

public class Ant implements Steppable {			
	private Point exit, entry;
	
	private final int[][] directions = {{-1,-1}, {0,-1}, {1, -1}, {1,0}, {1,1}, {0,1}, {-1,1},{-1,0}};
	
	private int dir;
	
	private boolean goBack;
	
	private int steps;
	
	public Ant(int entryx, int entryy, int exitx, int exity) {
		entry = new Point(entryx, entryy);
		exit = new Point(exitx, exity);
		goBack = false;
		
		dir = (int) Math.random()*8;
		steps = 0;
	}
	
	public void step(SimState state) {
		AntState st = (AntState) state;
	
		steps++;
		move(st);
	}
	
	private void move(AntState state) {
		Int2D pos = state.getSolverSpace().getObjectLocation(this);
		
		int l = dir - 1;
		if(l < 0) l += directions.length; 
		int r = (dir + 1) % directions.length; 
		
		double[] scent_values = new double[3];
		
		if(goBack) {			
			scent_values[0] = state.getScentHomeSpace().get(pos.x+directions[l][0], pos.y+directions[l][1]);
			scent_values[1] = state.getScentHomeSpace().get(pos.x+directions[dir][0], pos.y+directions[dir][1]);
			scent_values[2] = state.getScentHomeSpace().get(pos.x+directions[r][0], pos.y+directions[r][1]);
		} else {
			scent_values[0] = state.getScentSpace().get(pos.x+directions[l][0], pos.y+directions[l][1]);
			scent_values[1] = state.getScentSpace().get(pos.x+directions[dir][0], pos.y+directions[dir][1]);
			scent_values[2] = state.getScentSpace().get(pos.x+directions[r][0], pos.y+directions[r][1]);
		}						
		
		double sum = scent_values[0]+scent_values[1]+scent_values[2];
		
		double[] scent_p = {scent_values[0]/sum, scent_values[1]/sum, scent_values[2]/sum};
		
		int max = 0;
		
		for(int i = 1; i < scent_p.length; i++) {
			if(scent_p[max] < scent_p[i])
				max = i;
		}
		
		if(scent_p[max] > 0.5) {
			double resthalved = (scent_p[max] - 0.5) / 2;
			int up = (max + 1) % scent_p.length;
			scent_p[up] += resthalved;
			int dw = (max - 1 + scent_p.length) % scent_p.length;
			scent_p[dw] += resthalved;
			scent_p[max] = 0.5;
		}
		
		double rand = Math.random();
		
		int newdir;
		
		if(state.getGrid().get(pos.x+directions[l][0], pos.y+directions[l][1]) == 2) {
			newdir = l;
		} else if (state.getGrid().get(pos.x+directions[dir][0], pos.y+directions[dir][1]) == 2) {
			newdir = dir;
		} else if (state.getGrid().get(pos.x+directions[r][0], pos.y+directions[r][1]) == 2) {
			newdir = r;
		} else {
			if(rand <= scent_p[0]) {
				newdir = l;
			} else if (rand > scent_p[0] && rand <= scent_p[0]+scent_p[1]) {
				newdir = dir;
			} else {
				newdir = r;
			}
		}
		
		int x = pos.x+directions[newdir][0];
		int y = pos.y+directions[newdir][1];

		if(state.getGrid().get(x, y) > 0) {
			state.getSolverSpace().setObjectLocation(this, x, y);
			dir = newdir;
			if(goBack)
				state.getScentSpace().set(pos.x, pos.y, state.getScentSpace().get(pos.x, pos.y)+state.getConfig().getDouble("Ant.FoodTrail"));
			else
				state.getScentHomeSpace().set(pos.x, pos.y, state.getScentHomeSpace().get(pos.x, pos.y)+state.getConfig().getDouble("Ant.HomeTrail"));
		} else {
			int t = (int) (Math.random()*3)+1;
			if(Math.random() < 0.5) t *= -1;				
			dir = (dir + t + directions.length) % directions.length;			
		}
		
		if(x == exit.x && y == exit.y && !goBack) {
			goBack = true;
			dir = (dir + directions.length/2) % directions.length;
			state.setMinSteps(steps);
			steps = 0;
		} else if(x == entry.x && y == entry.y && goBack) {
			goBack = false;
			dir = (dir + directions.length/2) % directions.length;
			state.incRoundTrip();
			state.setMinSteps(steps);
			steps = 0;
		}
	}
}
