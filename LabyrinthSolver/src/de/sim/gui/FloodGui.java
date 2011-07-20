package de.sim.gui;

import sim.engine.*;
import sim.display.*;
import sim.portrayal.grid.*;
import java.awt.*;
import javax.swing.*;

import de.sim.state.FloodState;
import de.sim.utils.Config;

public class FloodGui extends GUIState {
    private Display2D display;
    private JFrame displayFrame;
    
    private static String labname;
    
    FastValueGridPortrayal2D gridPortrayal = new FastValueGridPortrayal2D("Maze");
    FastValueGridPortrayal2D solverPortrayal = new FastValueGridPortrayal2D("Solver");
    
	public FloodGui(Config conf) {
		super(new FloodState(System.currentTimeMillis(), conf));
		labname = conf.getString("System.labyrinth");
	}
	
	public FloodGui(SimState state) { super(state); }
	
	public static String getName() { return "Flood"; }
	
	public static Object getInfo() { 
		return "<h2>Flood</h2>"+
				"<p>Löse das folgende Labyrinth ("+labname+") mit Hilfe der <i>Flut-Methode</i></p>";
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
   				new Color[] {Color.black, Color.white, Color.green}));
   		solverPortrayal.setMap(new sim.util.gui.SimpleColorMap(
   				new Color[] {Color.black, Color.white, Color.green}, 3, 200, new Color(0,0,50,255), new Color(0,0,240,255)));   		   		
   		   		
   		gridPortrayal.setField(((FloodState) state).getGrid());   		
   		solverPortrayal.setField(((FloodState) state).getSolverSpace());
   		
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
