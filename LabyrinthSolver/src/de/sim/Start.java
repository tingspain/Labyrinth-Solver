package de.sim;

import de.sim.wrapper.*;
import de.sim.utils.Config;

public class Start {	
	public static Wrapper[] sims;
	
	public static void main(String[] args) {
		String file = "config";
		
		if(args.length > 0) {
			file = args[0];
		}
		
		Config c = new Config(file);
		
		sims = new Wrapper[] {new AntWrapper(), new PhysarumWrapper(), new FloodWrapper()};
		
		if(c.getBool("System.console")) {
			sims[c.getInt("System.algorithm")].startConsole(System.out, c);
		} else {
			sims[c.getInt("System.algorithm")].startGui(c);
		}
	}

}
