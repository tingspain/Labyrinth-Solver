package de.sim.gui;

import sim.engine.*;
import sim.display.*;
import sim.portrayal.grid.*;
import java.awt.*;
import javax.swing.*;

import de.sim.state.PhysarumState;
import de.sim.utils.Config;

public class PhysarumGui extends GUIState {
    private Display2D display;
    private JFrame displayFrame;
    
    private static String labname;
    
    FastValueGridPortrayal2D gridPortrayal = new FastValueGridPortrayal2D("Maze");
    FastValueGridPortrayal2D solverPortrayal = new FastValueGridPortrayal2D("Solver");
    
	public PhysarumGui(Config conf) {
		super(new PhysarumState(System.currentTimeMillis(), conf));
		labname = conf.getString("System.labyrinth");
	}
	
	public PhysarumGui(SimState state) { super(state); }
	
	public static String getName() { return "Physarum polycephalum"; }
	
	public static Object getInfo() { 
		return "<h2>Physarum polycephalum</h2>"+
				"<p>Löse das folgende Labyrinth ("+labname+") mit Hilfe von <i>Physarum polycephalum</i></p>";
	}
	
	public void start() {
		super.start();
		
		setupPortrayals();
	}
	
	public void load(SimState state) {
		super.load(state);
		
		setupPortrayals();
	}
	
	public void init(Controller c) {
		super.init(c);
		
		display = new Display2D(600,600, this, 1);
		
		displayFrame = display.createFrame();
		c.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		display.attach(gridPortrayal, "Maze");
		display.attach(solverPortrayal, "Solver");
		display.setBackdrop(Color.black);
	}
	
	private void setupPortrayals() {
   		gridPortrayal.setMap(new sim.util.gui.SimpleColorMap(
   				new Color[] {Color.white, new Color(0,0,0,0), Color.black, new Color(0,0,0,0), Color.green}));
   		solverPortrayal.setMap(new sim.util.gui.SimpleColorMap(
   				new Color[] {Color.white, new Color(180, 180, 40, 255), Color.black, new Color(200, 200, 50, 255), Color.green}));   		   		
   		   		
   		gridPortrayal.setField(((PhysarumState) state).getGrid());   		
   		solverPortrayal.setField(((PhysarumState) state).getSolverSpace());
   		
		display.reset();
		display.repaint();
	}
	
	public void quit() {
		super.quit();

		if (displayFrame!=null) displayFrame.dispose();
		displayFrame = null;
		display = null;
	}
}
