package edu.gmu.css600.toz;

import java.io.FileWriter;

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
import sim.field.SparseField2D;
import sim.field.network.*;


public class SN extends SimState {

	private static final long serialVersionUID = 1L;
	public static final double INFECTRATE = 25.0;
	public Network net;
	public static SimpleGraph<Agent, DefaultEdge> g;
	public int n = 100; //number of agents
	public MersenneTwisterFast r;
	public static int numOfInfected = 0;
	public static int numOfVaccinated = 0;
	public SN(long seed) {super(seed);}
	
	public void start() {
		super.start();
		//create undirected network field
		net = new Network(false);
		//create a scale-free network using jgrapht
		g = getGraph();
		//populate nodes of mason network by referencing Agents in jgraph network 
	    for (Agent a : g.vertexSet()){
	    	schedule.scheduleRepeating(a);
	    	net.addNode(a);	
	    }
	    //populate edges in the mason network
	    for (DefaultEdge e : g.edgeSet())
	    	net.addEdge(g.getEdgeSource(e), g.getEdgeTarget(e), null);
	    
	    //infect an agent randomly
	    r = new MersenneTwisterFast();
	    Agent a = (Agent) net.allNodes.get(r.nextInt(n));
	    //infect one randomly selected agent
	    a.infect(this, 100.0);
	    exportToGraphML(this, String.valueOf(0));
	}
	
	public static void main(String[] args) {
		SimState state = new SN(System.currentTimeMillis());
		state.start();
		do{
			if (!state.schedule.step(state))
            	break;
			exportToGraphML(state, String.valueOf(state.schedule.getSteps()));
			//System.out.println(numOfInfected);
			System.out.println(numOfVaccinated);
		}while(state.schedule.getSteps() < 10);
		
        state.finish();
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

