package edu.gmu.css600.toz;

import java.io.FileWriter; 
import java.io.IOException; 
import javax.xml.transform.TransformerConfigurationException; 
import org.jgrapht.ext.GraphMLExporter; 
import org.jgrapht.graph.DefaultWeightedEdge; 
import org.jgrapht.graph.SimpleDirectedWeightedGraph; 
import org.xml.sax.SAXException; 

public class WIC { 
        
        public SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> g; 
        
        public WIC(){ 
                g = new 
SimpleDirectedWeightedGraph<String,DefaultWeightedEdge>(DefaultWeightedEdge.class); 
        } 
        
        private void test(){ 
                g.addVertex("A"); 
                g.addVertex("B"); 
                g.addVertex("C"); 
                makeEdge("A","B",1); 
                makeEdge("B","A",2); 
                makeEdge("A","C",3); 
        } 
        
        private void makeEdge(String A, String B, int w){ 
                DefaultWeightedEdge e = g.addEdge(A,B); 
                g.setEdgeWeight(e, w); 
        } 
        
        public static void main(String[] args){ 
                WIC wic = new WIC(); 
                wic.test(); 
                
                GraphMLExporter VE = new GraphMLExporter(); 
                FileWriter PS; 
                try { 
                        PS = new FileWriter("C:/cs600/graph.graphml"); 
                        VE.export(PS, wic.g); 
                } catch (IOException | TransformerConfigurationException | 
SAXException e) {e.printStackTrace();} 
        } 

} 
