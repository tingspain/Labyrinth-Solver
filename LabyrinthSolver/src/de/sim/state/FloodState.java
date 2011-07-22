package de.sim.state;

import sim.field.grid.*;

import java.awt.Point;
import java.util.Arrays;

import de.sim.agents.Flood;
import de.sim.utils.Config;

public class FloodState extends SimulationState {
	private IntGrid2D grid;
	private IntGrid2D solver_space;
	
	private Flood agent;
	
	public FloodState(long seed, Config conf) {
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
	
		wall = 0;
		path = 0;
		
		initializeGrid();				
		
		agent = new Flood(entry.x, entry.y, exit.x, exit.y);
		
		schedule.scheduleRepeating(schedule.EPOCH, agent);
	}
	
	public void finish() {			
		getPath();
		writeLog(grid, "flood");
	}
	
	private void getPath() {
		boolean finished = false;
		Point cur = exit;
		
		while(!finished) {
			int[] m = getMoore(cur.x, cur.y);
			cur = nextCur(m, cur);			
			
			if(cur.equals(entry))
				finished = true;
			
			grid.set(cur.x, cur.y, 2);
		}
	}
	
	private Point nextCur(int[] moore, Point c) {
		int min = Integer.MAX_VALUE;
		int p = -1;
		for(int i = 0; i < moore.length; i++) {
			if(moore[i] >= 3 && moore[i] < min) {
				min = moore[i];
				p = i;
			}
		}
		
		int[][] dir;
		if(config.getBool("Flood.Moore")) {
			int[][] t = {{-1,-1},{0,-1},{1,-1},{-1,0},{1,0},{-1,1},{0,1},{1,1}};
			dir = t;
		} else {
			int[][] t = {{0,-1},{-1,0},{1,0},{0,1}};
			dir = t;
		}
		
		return new Point(c.x+dir[p][0], c.y+dir[p][1]);
	}
	
	private int[] getMoore(int x, int y) {
		if(config.getBool("Flood.Moore")) {
			int[] moore = new int[8];
			int c = 0;

			for(int i = y - 1; i <= y+1; i++) {
				for(int j = x - 1; j <= x+1; j++) {
					if(j != x || i != y)
						moore[c++] = solver_space.get(j, i);
				}
			}

			return moore;
		} else {
			int[] neu = new int[4];
			neu[0] = solver_space.get(x, y-1);
			neu[1] = solver_space.get(x-1, y);
			neu[2] = solver_space.get(x+1, y);
			neu[3] = solver_space.get(x, y+1);
			
			return neu;
		}
	}
	
	private void initializeGrid() {
		grid = loadImage(picture_name);
		
		grid.set(entry.x, entry.y, 3);
		
		solver_space = new IntGrid2D(grid.getWidth(), grid.getHeight());
		solver_space.setTo(grid);
	}
	
	public IntGrid2D getGrid() { return grid; }
	public IntGrid2D getSolverSpace() { return solver_space; }
	public void setSolverSpace(IntGrid2D g) { solver_space.setTo(g); }
}
