package de.sim.state;

import sim.engine.*;
import sim.field.grid.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.Point;
import java.util.Date;
import java.text.DateFormat;

import de.sim.utils.BMPFile;
import de.sim.utils.Config;

public abstract class SimulationState extends SimState {
	protected Point entry, exit;
	protected int path, wall;	
	protected String picture_name;
	protected String logname;
	protected Config config;
	
	public SimulationState(long seed, Config c) {
		super(seed);
		
		setLogname();
		config = c;
	}

	public Config getConfig() { return config; }
	
	protected void setLogname() {
		logname = "log/log_"+DateFormat.getTimeInstance().format(new Date())+"_"+
			Long.toString(System.currentTimeMillis())+
			"_"+DateFormat.getDateInstance().format(new Date());
	}
	
	protected IntGrid2D loadImage(String file) {
		try {
			BufferedImage img = ImageIO.read(new File(file));
			
			int x = img.getWidth();
			int y = img.getHeight();
			
			IntGrid2D grid = new IntGrid2D(x,y);
			grid.lowerBound(0);			
			
			for(int i = 0; i < y; i++) {
				for(int j = 0; j < x; j++) {
					int p = img.getRGB(j, i);
					
					int newp = 0;
					if(p == -1)
						newp = 1;
					else if(p == -65536)
						newp = 2;
					
					if(newp == 0) 
						wall++;
					else
						path++;
					
					grid.set(j, i, newp);
				}
			}
			
			for(int i = 0; i < y; i++) {
				for(int j = 0; j < x; j++) {
					if(grid.get(j, i) == 2) {
						if(entry.x == -1) {
							entry.x = j;
							entry.y = i;
						} else {
							exit.x = j;
							exit.y = i;
						}							
					}
				}
			}
			
			return grid;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}			
	}	
	
	protected void writeLog(IntGrid2D grid, String algo) {
		int found = 0;
		for(int i = 0; i < grid.getHeight(); i++) {
			for(int j = 0; j < grid.getWidth(); j++) {
				if(grid.get(j, i) > 1)
					found++;
			}
		}
		
		try {
			FileWriter fstream = new FileWriter(logname+".txt");
			BufferedWriter out = new BufferedWriter(fstream);
			
			out.write("algorithm: "+algo); out.newLine();
			out.write("labyrinth: "+picture_name); out.newLine();
			out.write("steps: "+schedule.getSteps()); out.newLine();
			out.write("entry: ("+entry.x+","+entry.y+")"); out.newLine(); 
			out.write("exit: ("+exit.x+","+exit.y+")"); out.newLine();
			out.write("walls: "+wall+", %: "+(1.0 * wall)/(grid.getWidth()*grid.getHeight())); out.newLine();
			out.write("paths: "+path+", %: "+(1.0 * path)/(grid.getWidth()*grid.getHeight())); out.newLine();
			out.write("found: "+found+", %: "+(1.0 * found)/path); out.newLine();
			
			out.close();
			
			BMPFile bmpwriter = new BMPFile();
			bmpwriter.saveArrayToBitmap(logname+".bmp", grid.field);
			
			
		} catch (Exception e) {
			System.err.println("Error while writing log: "+e.getMessage());
		}
	}
	
}
