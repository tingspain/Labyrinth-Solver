package de.sim.state;

import sim.field.grid.*;

import java.awt.Point;
import java.util.Arrays;

import de.sim.agents.Ant;
import de.sim.agents.AntScent;
import de.sim.utils.Config;

public class AntState extends SimulationState {
	private IntGrid2D grid;
	private DoubleGrid2D scent_space;
	private DoubleGrid2D scent_home_space;
	private SparseGrid2D solver_space;
	
	private int ants = 50;
	private Ant[] agents;
	
	private AntScent disolver;
	
	private long roundtrips;
	
	public AntState(long seed, Config conf) {
		super(seed, conf);
		
		picture_name = conf.getString("System.labyrinth");
		entry = new Point(-1, -1);
		exit = new Point(-1, -1);
		wall = 0;
		path = 0;
		roundtrips = 0;
		ants = conf.getInt("Ant.AntCount");
	}
	
	public void start() {
		super.start();
		
		setLogname();
		
		wall = 0;
		path = 0;
		
		initializeGrid();				
		
		agents = new Ant[ants];
		disolver = new AntScent();
		
		schedule.scheduleRepeating(schedule.EPOCH, disolver);
		
		for(int i = 0; i< ants;i++) {
			agents[i] = new Ant(entry.x, entry.y, exit.x, exit.y);
			solver_space.setObjectLocation(agents[i], entry.x, entry.y);
		}
			
		for(int i = 0; i < ants; i++) 
			schedule.scheduleRepeating(schedule.EPOCH, agents[i]);			
	}
	
	public void finish() {		
		writeLog(grid, "ant");
	}
	
	private void initializeGrid() {
		grid = loadImage(picture_name);
		scent_space = new DoubleGrid2D(grid.getWidth(), grid.getHeight());		
		scent_space.lowerBound(1);
		scent_home_space = new DoubleGrid2D(grid.getWidth(), grid.getHeight());
		scent_home_space.lowerBound(1);
		solver_space = new SparseGrid2D(grid.getWidth(), grid.getHeight());
	}
	
	public IntGrid2D getGrid() { return grid; }
	public DoubleGrid2D getScentSpace() { return scent_space; }
	public DoubleGrid2D getScentHomeSpace() { return scent_home_space; }
	public SparseGrid2D getSolverSpace() { return solver_space; }
	
	public void incRoundTrip() { roundtrips++; }
	public long getRoundTrip() { return roundtrips; }
}
