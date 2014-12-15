package edu.gmu.css600.toz;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.VertexFactory;
import org.jgrapht.ext.EdgeNameProvider;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.generate.ScaleFreeGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import ec.util.MersenneTwisterFast;
import edu.gmu.css600.toz.Agent;
import sim.engine.SimState;
import sim.field.network.*;


public class SN extends SimState {

	private static final long serialVersionUID = 1L;
	public static final double INFECTRATE = .20;
	public static double VACCINATIONRATE = .1 ;
	public Network net;
	public static SimpleGraph<Agent, DefaultEdge> g;
	public int n = 100; //number of agents
	public MersenneTwisterFast r;
	public static int numOfInfected = 0;
	public static int numOfVaccinated = 0;
	public SN(long seed) {super(seed);}
	
	public void start() {
		super.start();
		//random number generator
		r = new MersenneTwisterFast();
		//create undirected network field
		net = new Network(false);
		//create a scale-free network using jgrapht with agents
		g = getGraph();
		//infect a randomly selected agent
		List<Agent> asList = new ArrayList<Agent>(g.vertexSet());
	    ((Agent) asList.get(r.nextInt(n))).infect(this, 100.0);
	    asList = null;
	    
		//populate nodes of mason network by referencing Agents in jgraph network 
	    for (Agent a : g.vertexSet()){
	    	schedule.scheduleRepeating(a);
	    	net.addNode(a);
	    	//scenario 3 and 6
	    	a.vaccinate(this,SN.VACCINATIONRATE);
	    }
	    //populate edges in the mason network
	    for (DefaultEdge e : g.edgeSet())
	    	net.addEdge(g.getEdgeSource(e), g.getEdgeTarget(e), null);
	    
	    // exportToGraphML(this, String.valueOf(0));
	}
	
	public static void main(String[] args) {
		for(int i=0;i<100;i++){
			SimState state = new SN(System.currentTimeMillis());
			state.start();
			do{
				if (!state.schedule.step(state))
	            	break;
				//exportToGraphML(state, String.valueOf(state.schedule.getSteps()));
				System.out.print(numOfInfected+",");
				//System.out.print(numOfVaccinated+",");
				if(state.schedule.getSteps() %20 == 0){
					SN.numOfInfected = 0;
					SN.numOfVaccinated = 0;
					System.out.println();
				}
				// System.out.println(numOfVaccinated);
			}while(state.schedule.getSteps() < 20);			
	        state.finish();
		}
		System.exit(0);
	}
	
	private SimpleGraph<Agent, DefaultEdge> getGraph() {
		SimpleGraph<Agent, DefaultEdge> graph = new SimpleGraph<Agent, DefaultEdge>(DefaultEdge.class);
		ScaleFreeGraphGenerator<Agent, DefaultEdge> completeGenerator =
			    new ScaleFreeGraphGenerator<Agent, DefaultEdge>(n);

	    completeGenerator.generateGraph(graph, new VertexFactory<Agent>() {
	        int last = 0;

	        public Agent createVertex() {
	          return new Agent(last++);
	        }
	      }, null);
	    return graph;
	}
	
	private static void exportToGraphML(SimState state, String filename) {
		
	    //save network as graphml file to be read for Gephi
		VertexNameProvider<Agent> vertexIDProvider = new VertexNameProvider<Agent>() { 
			@Override
			public String getVertexName(Agent a) { return String.valueOf(a.getId()); }
		}; 
		VertexNameProvider<Agent> vertexNameProvider = new VertexNameProvider<Agent>() { 
			@Override
			public String getVertexName(Agent a) { return String.valueOf(a.getHS()); }
		};
		EdgeNameProvider<DefaultEdge> edgeIDProvider = new EdgeNameProvider<DefaultEdge>() {
			@Override 
			public String getEdgeName(DefaultEdge edge) {
				return g.getEdgeSource(edge) + " > " + g.getEdgeTarget(edge); } 
			}; 
        EdgeNameProvider<DefaultEdge> edgeLabelProvider = new EdgeNameProvider<DefaultEdge>() { 
            @Override 
            public String getEdgeName(DefaultEdge edge) { 
                return String.valueOf(g.getEdgeWeight(edge)); 
            } 
        }; 
	    GraphMLExporter<Agent, DefaultEdge> exporter =
	    		new GraphMLExporter<Agent, DefaultEdge>(vertexIDProvider, vertexNameProvider, edgeIDProvider, edgeLabelProvider);
	    FileWriter w;
		try {
			w = new FileWriter(filename+".graphml");
			exporter.export(w, g); // ((SN) state).g
			w.close();
		} catch (Exception e) {
			printlnSynchronized(e.getMessage());
		}
		
	}

	
}

