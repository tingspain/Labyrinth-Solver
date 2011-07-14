package de.sim.utils;

import java.util.HashMap;
import java.io.*;

public class Config {
	private HashMap<String, String> config;
	
	public Config(String file) {
		config = new HashMap<String, String>();
		
		loadConfig(file);
	}
	
	private void loadConfig(String file) {
		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line;
			while((line = br.readLine()) != null) {
				if(line.indexOf('#') == 0 || line.trim().equals(""))
					continue;
				
				String[] s = parseLine(line.trim());
	
				config.put(s[0], s[1]);
			}
			in.close();
		} catch(Exception e) {
			System.err.println("Error: Reading config file "+file+" ->\n"+e.getMessage());
			System.err.println("Bailing out!");
			System.exit(1);
		}
	}
	
	private String[] parseLine(String l) {
		String[] s = new String[2];
		
		int eqsignpos = l.indexOf('=');
		
		if(eqsignpos == -1)
			return s;
		
		s[0] = l.substring(0, eqsignpos).trim();
		s[1] = l.substring(eqsignpos+1).trim();
			
		return s;
	}
	
	public int getInt(String key) {
		String r = config.get(key);
		
		if(r == null)
			return 0;
		else
			return Integer.parseInt(r);
	}
	
	public double getDouble(String key) {
		String r = config.get(key);
		
		if(r == null)
			return 0;
		else
			return Double.parseDouble(r);
	}
	
	public String getString(String key) {
		String r = config.get(key);

		if(r == null)
			return "NA";
		else
			return r;
	}
	
	public boolean getBool(String key) {
		String r = config.get(key);
		
		if(r == null)
			return false;
		else
			return r.equals("t");
	}
}
