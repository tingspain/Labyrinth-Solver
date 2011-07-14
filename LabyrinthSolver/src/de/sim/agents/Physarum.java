package de.sim.agents;

import sim.engine.*;

import java.awt.Point;

import de.sim.state.PhysarumState;
import de.sim.utils.PhysarumTemplates;

public class Physarum implements Steppable {
	private Point exit, entry;
	
	private PhysarumTemplates templates;
	
	private int counter;
	
	public Physarum(int enx, int eny, int exx, int exy) {
		entry = new Point(enx, eny);
		exit = new Point(exx, exy);
		
		templates = new PhysarumTemplates();
		
		counter = 5;
	}
	
	public void step(SimState state) {
		PhysarumState st = (PhysarumState) state;

		move(st);
		
		if(counter == 0)
			state.kill();
	}
	
	private void move(PhysarumState state) {
		boolean changed = false;
		
		for(int i = 1; i < state.getSolverSpace().getHeight()-1; i++) {
			for(int j = 1; j < state.getSolverSpace().getWidth()-1; j++) {
				if((j == entry.x && i == entry.y) || (j == exit.x && i == exit.y))
					continue;
				
				int v = state.getSolverSpace().get(j, i);
				
				if(v == 2 || v == 4)
					continue;
				
				int[] moore = getMoore(state, j, i);								
		
				int nv = templates.checkTemplate(moore);
				if(nv != v) changed = true; 
				
				state.getSolverSpace().set(j,i,nv);				
			}
		}
				
		if(!changed) counter--;
		else counter = 5;
		
		templates.rotateTemplates();
	}
	
	private int[] getMoore(PhysarumState state, int x, int y) {
		int[] moore = new int[9];
		int c = 0;
		
		for(int i = y - 1; i <= y+1; i++) {
			for(int j = x - 1; j <= x+1; j++) {				
				moore[c++] = state.getSolverSpace().get(j, i);
			}
		}
		
		return moore;
	}
}
