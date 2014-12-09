package edu.gmu.css600.toz;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.network.Edge;

public class Agent implements Steppable{

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
		color = 0;
	}

	@Override
	public void step(SimState state) {
		SN sn = (SN) state;
		// if I am infected or recovered do nothing
		if(color == 1 || color == 3)
			return;
		// if none of my neighbors are infected do nothing
		Edge[] edges = sn.net.getAdjacencyList(true)[getId()];
		for (int i = 0; i < edges.length; i++) {
			Agent neighbor = edges[i].getFrom().equals(this) ? (Agent) edges[i].getTo() : (Agent) edges[i].getFrom();
			if(neighbor.isInfected())
				infect(sn, SN.INFECTRATE);
		}
	}

	private boolean isInfected() {
		if (color == 2)
			return true;
		return false;
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

	public void infect(SN sn, double d) {
		if(color == 1 || color == 3)
			return;
		if(sn.r.nextDouble()*100 < d)
			color = 2;
	}
	// Get Health Status
	public int getHS() {
		return color;
	}

}
