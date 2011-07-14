package de.sim.agents;

import sim.engine.*;

import de.sim.state.AntState;

public class AntScent implements Steppable {				
	public void step(SimState state) {
		AntState st = (AntState) state;
		
		if(state.schedule.getSteps() >= st.getConfig().getInt("Ant.MaxSteps"))
			state.kill();
		
		double ftd = st.getConfig().getDouble("Ant.FoodTrailDecrease");
		double htd = st.getConfig().getDouble("Ant.HomeTrailDecrease");

		for(int i = 0; i < st.getScentSpace().getHeight(); i++) {
			for(int j = 0; j < st.getScentSpace().getWidth(); j++) {
				double v = st.getScentSpace().get(j, i);
				double vh = st.getScentHomeSpace().get(j, i);
				if(v > 1) 
					st.getScentSpace().set(j, i, v-ftd);									
				if(vh > 1)
					st.getScentHomeSpace().set(j, i, vh-htd);
			}
		}
	}
}
