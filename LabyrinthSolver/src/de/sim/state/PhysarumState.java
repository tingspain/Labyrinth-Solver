package de.sim.state;

import sim.field.grid.*;

import java.awt.Point;
import java.util.Arrays;

import de.sim.agents.Physarum;
import de.sim.utils.Config;

public class PhysarumState extends SimulationState {
	private IntGrid2D grid;
	private IntGrid2D solver_space;
	
	private Physarum agent;
	
	public PhysarumState(long seed, Config conf) {
		super(seed, conf);
		
		picture_name = conf.getString("System.labyrinth");
		entry = new Point(-1, -1);
		exit = new Point(-1, -1);
		wall = 0;
		path = 0;
	}
	
	public void start() {
		super.start();
		
		setLogname();
		
		initializeGrid();				
		
		agent = new Physarum(entry.x, entry.y, exit.x, exit.y);
		
		schedule.scheduleRepeating(schedule.EPOCH, agent);
	}
	
	public void finish() {
		for(int i = 0; i < grid.getHeight(); i++) {
			for(int j = 0; j < grid.getWidth(); j++) {
				if(solver_space.get(j, i) == 0)
					grid.set(j, i, 1);
				if(solver_space.get(j, i) == 2)
					grid.set(j, i, 0);
				if(solver_space.get(j, i) == 3)
					grid.set(j, i, 2);
			}
		}
		
		writeLog(grid, "physarum");
	}
	
	private void initializeGrid() {
		grid = loadImage(picture_name);
		
		for(int i = 0; i < grid.getHeight(); i++) {
			for(int j = 0; j < grid.getWidth(); j++) {
				if(grid.get(j, i) == 0)
					grid.set(j, i, 2);
				else if(grid.get(j, i) == 1)
					grid.set(j, i, 0);				
			}
		}		
		
		grid.set(entry.x, entry.y, 0);
		grid.set(exit.x, exit.y, 4);
		
		solver_space = new IntGrid2D(grid.getWidth(), grid.getHeight());
		solver_space.setTo(grid);
		
		solver_space.set(entry.x, entry.y, 1);
		solver_space.set(entry.x+1, entry.y, 1);
		solver_space.set(entry.x-1, entry.y, 1);
		solver_space.set(entry.x, entry.y+1, 1);
		solver_space.set(entry.x, entry.y-1, 1);
	}
	
	public IntGrid2D getGrid() { return grid; }
	public IntGrid2D getSolverSpace() { return solver_space; }
	public void setSolverSpace(IntGrid2D g) { solver_space.setTo(g); }
}
