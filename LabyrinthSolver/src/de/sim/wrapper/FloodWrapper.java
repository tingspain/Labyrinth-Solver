package de.sim.wrapper;

import java.io.PrintStream;

import sim.display.Console;

import de.sim.state.FloodState;
import de.sim.gui.FloodGui;
import de.sim.utils.Config;

public class FloodWrapper implements Wrapper {

	@Override
	public void startConsole(PrintStream ps, Config conf) {
		FloodState fs = new FloodState(System.currentTimeMillis(), conf);
		
		for(int i=0; i < conf.getInt("System.runs"); i++) {
			ps.println("Running LabyrinthSolver in console mode...");			
			fs.start();			
			while(fs.schedule.step(fs)) {

			}
			ps.println("Round "+i+" finished.");
			fs.finish();
		}
	}

	@Override
	public void startGui(Config conf) {
		FloodGui fg = new FloodGui(conf);
		Console c = new Console(fg);
		c.setVisible(true);
	}
	
	@Override
	public String toString() {
		return "Flood";
	}

}
