package edu.gmu.css600.toz;

import sim.engine.SimState;
import sim.engine.Steppable;

public class Agent implements Steppable {

	private static final long serialVersionUID = 1L;
	private int id;
	// 0 for susceptible
	// 1 for vaccinated
	// 2 for infected
	// 3 for recovered
	private int color;
	
	// VertexNameProvider
	
	public Agent(int i) {
		this.setId(i);
	}

	@Override
	public void step(SimState state) {
		SN sn = (SN) state;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
    @Override
    public String toString() {
      return " " + id;
    }

}
