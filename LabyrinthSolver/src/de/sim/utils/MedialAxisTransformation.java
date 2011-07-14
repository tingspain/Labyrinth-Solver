package de.sim.utils;

import sim.field.grid.*;

public class MedialAxisTransformation {
	IntGrid2D mat;	
	
	public IntGrid2D getMAT(final IntGrid2D grid) {
		mat = new IntGrid2D(grid.getWidth(), grid.getHeight());
		mat.setTo(grid);
		IntGrid2D result = new IntGrid2D(grid.getWidth(), grid.getHeight());
		result.setTo(grid);
		
		for(int i = 0; i < mat.getHeight(); i++) {
			for(int j = 0; j < mat.getWidth(); j++) {
				if(mat.get(j, i) == 1)
					mat.set(j, i, Integer.MAX_VALUE - 1);
			}
		}		
		
		medialAxisTransformation();
		
		for(int i = 0; i < mat.getHeight(); i++) {
			for(int j = 0; j < mat.getWidth(); j++) {
				if(mat.get(j, i) > 0 && checkNeighborsForMat(j, i)) {
					result.set(j, i, 2);
				}
			}
		}
		
		return result;
	}	
	
	private void medialAxisTransformation() {
		for(int i = 1; i < mat.getHeight(); i++) {
			for(int j = 1; j < mat.getWidth(); j++) {
				int[] v = {mat.get(j-1, i) + 1, mat.get(j, i), mat.get(j, i-1) + 1};
				
				int tmp = Math.min(v[0], v[1]);
				mat.set(j, i, Math.min(tmp, v[2]));
			}
		}
		
		for(int i = mat.getHeight() - 2; i > 0; i--) {
			for(int j = mat.getWidth() - 2; j > 0; j--) {
				int[] v = {mat.get(j, i + 1) + 1, mat.get(j, i), mat.get(j + 1, i) + 1};			
				
				int tmp = Math.min(v[0], v[1]);							
				mat.set(j, i, Math.min(tmp, v[2]));
			}
		}		
	}
	
	private void smoothPath (IntGrid2D path) {
		
	}
	
	private int[] getMooreNeighbors(int x, int y, int range) {
		int[] res = new int[(4*range*range)+4*range];
		
		int lowerx, lowery, upperx, uppery, counter;
		
		counter = 0;
		lowerx = (x - range < 0?0:x);
		lowery = (y - range < 0?0:y);
		upperx = (x + range >= mat.getWidth()?mat.getWidth():x);
		uppery = (y + range >= mat.getHeight()?mat.getHeight():y);			
		
		for(int i = lowery; i < uppery; i++) {
			for(int j = lowerx; j < upperx; j++) {
				res[counter++] = mat.get(j, i);
			}
		}
		
		return res;
	}
	
	private boolean checkNeighborsForMat(int x, int y) {
		try {
			int[] v = {mat.get(x-1,y-1),mat.get(x, y-1), mat.get(x+1,y-1),mat.get(x+1, y), mat.get(x+1, y+1),mat.get(x, y+1), mat.get(x-1,y+1),mat.get(x-1,y)};
			
			for(int i = 0; i < v.length; i++) {
				if(v[i] > mat.get(x, y))
					return false;
			}			
			return true;
		} catch(Exception e) {
			return false;
		}
	}
}
