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
	private boolean hasVacced = false;
	// VertexNameProvider
	
	public Agent(int i) {
		this.setId(i);
		color = 0;
	}

	@Override
	public void step(SimState state) {
		SN sn = (SN) state;
		// if I am vaccinated, infected or recovered do nothing
		if(color > 0)
			return;
		// if none of my neighbors are infected do nothing
		Edge[] edges = sn.net.getAdjacencyList(true)[getId()];
		
//		//Scenario 2 starts here

//		int infectedNeighbors = 0;
//		for (int i = 0; i < edges.length; i++) {
//			Agent neighbor = edges[i].getFrom().equals(this) ? (Agent) edges[i].getTo() : (Agent) edges[i].getFrom();
//			if(neighbor.isInfected())
//				infectedNeighbors++;
//		}
		int vaccedNeighbors = 0;
		for (int i = 0; i < edges.length; i++) {
			Agent neighbor = edges[i].getFrom().equals(this) ? (Agent) edges[i].getTo() : (Agent) edges[i].getFrom();
			if(neighbor.hasVacced)
				vaccedNeighbors++;
		}
//		// vaccination probability is the infectedNeighbors ratio
//		if(vaccedNeighbors>0)
//			vaccinate(sn,1);
		if(sn.r.nextDouble() < vaccedNeighbors*1.0/edges.length)
			vaccinate(sn,1);
//		if(sn.r.nextDouble() < infectedNeighbors*1.0/edges.length){
//			SN.numOfVaccinated++;
//			color = 1;
//		}// Scenario 2 ends here
		
//		// Scenario 1.1 and scenario 5
		// vaccinate(sn,SN.VACCINATIONRATE);
		
		
		// for each infected neighbor, consider getting infected :-)
		for (int i = 0; i < edges.length; i++) {
			Agent neighbor = edges[i].getFrom().equals(this) ? (Agent) edges[i].getTo() : (Agent) edges[i].getFrom();
			if(neighbor.isInfected())
				infect(sn, SN.INFECTRATE);
		}
	}

	
	public void vaccinate(SN sn, double d) {
		if(color > 0)
			return;
		//scenario 5
//		if(hasConsideredVac)
//			return;
//		hasConsideredVac = true;
		if(sn.r.nextDouble() < SN.VACCINATIONRATE){
			hasVacced = true;
			SN.numOfVaccinated++;
			color = 1;
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
		if(color > 0)
			return;
		if(sn.r.nextDouble() < d){
			SN.numOfInfected++;
			color = 2;
		}	
	}
	
	// Get Health Status
	public int getHS() {
		return color;
	}

}
