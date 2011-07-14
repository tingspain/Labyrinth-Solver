package de.sim.gui;

import sim.engine.*;
import sim.display.*;
import sim.portrayal.grid.*;
import java.awt.*;
import javax.swing.*;

import de.sim.state.AntState;
import de.sim.utils.Config;

public class AntGui extends GUIState {
    private Display2D display;
    private JFrame displayFrame;
    
    private static String labname;
    
    FastValueGridPortrayal2D gridPortrayal = new FastValueGridPortrayal2D("Maze");
    FastValueGridPortrayal2D scentPortrayal = new FastValueGridPortrayal2D("Scent Food");
    FastValueGridPortrayal2D scentHomePortrayal = new FastValueGridPortrayal2D("Scent Home");
    SparseGridPortrayal2D solverPortrayal = new SparseGridPortrayal2D();
    
	public AntGui(Config conf) {
		super(new AntState(System.currentTimeMillis(), conf));
		labname = conf.getString("System.labyrinth");
	}
	
	public AntGui(SimState state) { super(state); }
	
	public static String getName() { return "Ameisen Algorithmus"; }
	
	public static Object getInfo() { 
		return "<h2>Ameisen Algorithmus</h2>"+
				"<p>Löse das folgende Labyrinth ("+labname+") mit Hilfe des <i>Ameisen Algorithmus</i></p>";
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
		display.attach(scentPortrayal, "Scent Food");
		display.attach(scentHomePortrayal, "Scent Home");
		display.attach(solverPortrayal, "Solver");
		display.setBackdrop(Color.black);
	}
	
	private void setupPortrayals() {
   		gridPortrayal.setMap(new sim.util.gui.SimpleColorMap(
   				new Color[] {Color.black, Color.white, Color.green, new Color(200, 200, 150, 255)}));
   		scentPortrayal.setMap(new sim.util.gui.SimpleColorMap(
   				new Color[] {new Color(0,0,0,0), new Color(0,0,0,0)},
   				3, 200, new Color(0,0,50,255), new Color(0,0,200,255)));
   		scentHomePortrayal.setMap(new sim.util.gui.SimpleColorMap(
   				new Color[] {new Color(0,0,0,0), new Color(0,0,0,0)},
   				3, 200, new Color(20,50,20,255), new Color(180,200,180,255)));
   		solverPortrayal.setPortrayalForAll( new sim.portrayal.simple.OvalPortrayal2D(Color.red));
   		   		
   		gridPortrayal.setField(((AntState) state).getGrid());
   		scentPortrayal.setField(((AntState) state).getScentSpace());
   		scentHomePortrayal.setField(((AntState) state).getScentHomeSpace());
   		solverPortrayal.setField(((AntState) state).getSolverSpace());
   		
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
