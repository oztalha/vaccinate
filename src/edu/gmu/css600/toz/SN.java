package edu.gmu.css600.toz;

import java.io.FileWriter;

import org.jgrapht.VertexFactory;
import org.jgrapht.ext.GraphMLExporter;
import org.jgrapht.generate.ScaleFreeGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import edu.gmu.css600.toz.Agent;
import sim.engine.SimState;
import sim.field.network.*;


public class SN extends SimState {

	private static final long serialVersionUID = 1L;
	public Network net;
	SimpleGraph<Agent, DefaultEdge> g;
	public int n = 100; //number of agents
	
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
	
	public static void main(String[] args) {
		SimState state = new SN(System.currentTimeMillis());
		state.start();
		do{
			if (!state.schedule.step(state))
            	break;
			exportToGraphML(state, String.valueOf(state.schedule.getSteps()));
		}while(state.schedule.getSteps() < 10);
        state.finish();
		System.exit(0);
	}
	
	private static void exportToGraphML(SimState state, String filename) {
		
	    //save network as graphml file to be read for Gephi
	    GraphMLExporter<Agent, DefaultEdge> exporter = new GraphMLExporter<Agent, DefaultEdge>();
	    FileWriter w;
		try {
			w = new FileWriter(filename+".graphml");
			exporter.export(w, ((SN) state).g);
			w.close();
		} catch (Exception e) {
			printlnSynchronized(e.getMessage());
		}
		
	}

	
}

