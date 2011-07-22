package de.sim.wrapper;

import java.io.PrintStream;

import sim.display.Console;

import de.sim.state.PhysarumState;
import de.sim.gui.PhysarumGui;
import de.sim.utils.Config;

public class PhysarumWrapper implements Wrapper {

	@Override
	public void startConsole(PrintStream ps, Config conf) {
		PhysarumState fs = new PhysarumState(System.currentTimeMillis(), conf);
		
		for(int i=0; i < conf.getInt("System.runs"); i++) {
			ps.println("Running LabyrinthSolver in console mode...");
			ps.println("Using "+toString());
			fs.start();			
			while(fs.schedule.step(fs)) {

			}
			ps.println("Round "+i+" finished.");
			fs.finish();
		}
	}

	@Override
	public void startGui(Config conf) {
		PhysarumGui fg = new PhysarumGui(conf);
		Console c = new Console(fg);
		c.setVisible(true);
	}
	
	@Override
	public String toString() {
		return "Physarum polycephalum";
	}

}
