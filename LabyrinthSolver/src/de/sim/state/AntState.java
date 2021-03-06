package de.sim.state;

import sim.field.grid.*;

import java.awt.Point;
import java.util.Arrays;

import de.sim.agents.Ant;
import de.sim.agents.AntRight;
import de.sim.agents.AntScent;
import de.sim.utils.Config;

public class AntState extends SimulationState {
	private IntGrid2D grid;
	private DoubleGrid2D scent_space;
	private DoubleGrid2D scent_home_space;
	private SparseGrid2D solver_space;
	
	private int ants = 50;
	private int rants = 0;
	private Ant[] agents;
	private AntRight[] ragents;
	
	private AntScent disolver;
	
	private long roundtrips;
	
	private int minsteps;
	
	public AntState(long seed, Config conf) {
		super(seed, conf);
		
		picture_name = conf.getString("System.labyrinth");
		entry = new Point(-1, -1);
		exit = new Point(-1, -1);
		wall = 0;
		path = 0;
		roundtrips = 0;
		ants = conf.getInt("Ant.AntCount");
		rants = conf.getInt("Ant.AntRightCount");
		minsteps = Integer.MAX_VALUE;
	}
	
	public void start() {
		super.start();
		
		setLogname();
		
		wall = 0;
		path = 0;
		
		initializeGrid();				
		
		agents = new Ant[ants];
		ragents = new AntRight[rants];
		disolver = new AntScent();
		
		schedule.scheduleRepeating(schedule.EPOCH, disolver);
		
		for(int i = 0; i< ants;i++) {
			agents[i] = new Ant(entry.x, entry.y, exit.x, exit.y);
			solver_space.setObjectLocation(agents[i], entry.x, entry.y);
			schedule.scheduleRepeating(schedule.EPOCH, agents[i]);
		}
		
		for(int i = 0; i< rants;i++) {
			ragents[i] = new AntRight(entry.x, entry.y, exit.x, exit.y);
			solver_space.setObjectLocation(ragents[i], entry.x, entry.y);
			schedule.scheduleRepeating(schedule.EPOCH, ragents[i]);
		}					 
	}
	
	public void finish() {		
		writeLog(grid, "ant");
		System.out.println("min steps: "+minsteps);
	}
	
	private void initializeGrid() {
		grid = loadImage(picture_name);
		scent_space = new DoubleGrid2D(grid.getWidth(), grid.getHeight());		
		scent_space.lowerBound(1);
		scent_home_space = new DoubleGrid2D(grid.getWidth(), grid.getHeight());
		scent_home_space.lowerBound(1);
		solver_space = new SparseGrid2D(grid.getWidth(), grid.getHeight());
	}
	
	public void setMinSteps(int steps) {
		if(steps < minsteps)
			minsteps = steps;
	}
	
	public IntGrid2D getGrid() { return grid; }
	public DoubleGrid2D getScentSpace() { return scent_space; }
	public DoubleGrid2D getScentHomeSpace() { return scent_home_space; }
	public SparseGrid2D getSolverSpace() { return solver_space; }
	
	public void incRoundTrip() { roundtrips++; }
	public long getRoundTrip() { return roundtrips; }
}
