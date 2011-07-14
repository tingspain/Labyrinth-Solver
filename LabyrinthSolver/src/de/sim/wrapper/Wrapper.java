package de.sim.wrapper;

import java.io.PrintStream;

import de.sim.utils.Config; 

public interface Wrapper {
	public void startConsole(PrintStream ps, Config conf);
	
	public void startGui(Config conf);
	
	public String toString();
}
