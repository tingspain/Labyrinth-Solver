package de.sim.utils;

public class PhysarumTemplates {
	private int[][] tempZO = {{1,1,-1,1,0,-2,-1,-2,-2}, // road -> gel1			 				  
			 				  {1,1,1,-1,0,-1,-2,-2,-2}};			 				  
	
	private int[][] tempTZ = {{-2,-2,-1,-2,3,3,-1,3,3}, // gel2 -> road
							  {-2,-2,-2,-1,3,-1,3,3,3},
							  {-2,-2,-2,-2,3,-2,-1,3,-1},							  
							 };
	
	private int[] rotateTemplate(int[] template) {
		int[] tmp = new int[9];
		
		tmp[0] = template[2];
		tmp[1] = template[5];
		tmp[2] = template[8];
		tmp[3] = template[1];
		tmp[4] = template[4];
		tmp[5] = template[7];
		tmp[6] = template[0];
		tmp[7] = template[3];
		tmp[8] = template[6];
		
		return tmp;
	}
	
	private boolean equalTemp(int[] moore, int[] temp, int[] wildcard) {
		for(int i = 0; i < temp.length; i++) {
			if(temp[i] == -1)
				continue;
			else if(temp[i] == -2) {
				boolean b = false;
				for(int j = 0; j < wildcard.length; j++) {
					if(moore[i] == wildcard[j]) {
						b = true;
						break;
					}
				}				
				if(!b) return b;
			} else if(moore[i] != temp[i])
				return false;							
		}
		return true;
	}
	
	public void rotateTemplates() {
		for(int i = 0; i < tempZO.length; i++) {
			tempZO[i] = rotateTemplate(tempZO[i]);
		}
		for(int i = 0; i < tempTZ.length; i++) {
			tempTZ[i] = rotateTemplate(tempTZ[i]);
		}
	}
	
	public int checkTemplate(int[] moore) {
		if(moore[4] == 0) {
			for(int i = 0; i < tempZO.length; i++) {
				if(equalTemp(moore, tempZO[i], new int[] {0,2,4})) {
					return 1;
				}
			}
			return 0;
		} else if(moore[4] == 1) {
			for(int i = 0; i < moore.length; i++) {
				if(moore[i] == 4 || moore[i] == 3)
					return 3;									
			}
			return 1;		
		} else if(moore[4] == 3) {
			for(int i = 0; i < tempTZ.length; i++) {
				if(equalTemp(moore, tempTZ[i], new int[] {0,2})) {
					return 0;
				}
			}
			return 3;
		}
		return -1;
	}
	
	public void printTest() {
		System.out.println("printTest:");
		System.out.println(equalTemp(new int[] {1,1,1,1,0,0,1,0,4}, tempZO[0], new int[] {0,2,4}));
		System.out.println("printTest finished");
	}
}
