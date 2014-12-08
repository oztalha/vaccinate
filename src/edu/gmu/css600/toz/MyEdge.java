package edu.gmu.css600.toz;

import org.jgrapht.graph.DefaultEdge;


public final class MyEdge extends DefaultEdge {

	private static final long serialVersionUID = 1L;

	@Override
    public Agent getSource() {
      return (Agent) super.getSource();
    }

    @Override
    public Agent getTarget() {
      return (Agent) super.getTarget();
    }

  }