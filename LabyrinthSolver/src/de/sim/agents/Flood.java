package de.sim.agents;

import sim.engine.*;
import sim.field.grid.IntGrid2D;

import java.awt.Point;

import de.sim.state.FloodState;

public class Flood implements Steppable {
	private Point exit, entry;
	
	public Flood(int enx, int eny, int exx, int exy) {
		entry = new Point(enx, eny);
		exit = new Point(exx, exy);
	}
	
	public void step(SimState state) {
		FloodState st = (FloodState) state;

		move(st);
	}
	
	private void move(FloodState state) {
		IntGrid2D tmp = new IntGrid2D(state.getSolverSpace().getWidth(), state.getSolverSpace().getHeight());
		tmp.setTo(state.getGrid());
		
		boolean changed = false;
		
		for(int i = 0; i < state.getSolverSpace().getHeight(); i++) {
			for(int j = 0; j < state.getSolverSpace().getWidth(); j++) {
				if(state.getSolverSpace().get(j, i) == 1) {
					int[] m = getMoore(state, j, i);
					
					if(allowGrow(m)) {
						tmp.set(j, i, getMin(m)+1);
						changed = true;
					}					
				} else
					tmp.set(j, i, state.getSolverSpace().get(j,i));
			}
		}
		
		state.setSolverSpace(tmp);
		
		if(!changed)
			state.kill();
	}
	
	private int getMin(int[] moore) {
		int r = Integer.MAX_VALUE;
		
		for(int i = 0; i < moore.length; i++) {
			if(moore[i] >= 3 && moore[i] < r)
				r = moore[i];
		}
		
		if(r == Integer.MAX_VALUE)
			return 1;
		else
			return r;
	}
	
	private boolean allowGrow(int[] moore) {
		for(int i = 0; i < moore.length; i++) {
			if(moore[i] >= 3)
				return true;
		}
		return false;
	}
	
	private int[] getMoore(FloodState state, int x, int y) {
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
