package de.sim.wrapper;

import java.io.PrintStream;

import sim.display.Console;

import de.sim.state.AntState;
import de.sim.gui.AntGui;
import de.sim.utils.Config;

public class AntWrapper implements Wrapper {

	@Override
	public void startConsole(PrintStream ps, Config conf) {
		AntState fs = new AntState(System.currentTimeMillis(), conf);
		
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
		AntGui fg = new AntGui(conf);
		Console c = new Console(fg);
		c.setVisible(true);
	}
	
	@Override
	public String toString() {
		return "Ameisen Algorithmus";
	}

}
