package net.iubris.facri.tasks.graphgeneration.interactions.graphstream.graphables;

import net.iubris.facri.graph.generator.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.ishtaran.task.phasable.AbstractPhasable;

import org.graphstream.graph.Graph;

public abstract class AbstractInteractionsGraphstreamGraphAble extends AbstractPhasable<Graph> {

	protected final GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator;
	
	public AbstractInteractionsGraphstreamGraphAble(GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator) {
		this.graphstreamInteractionsGraphGenerator = graphstreamInteractionsGraphGenerator;
	}
	
//	protected void prepareForDisplay() {
//		graphstreamInteractionsGraphGenerator.prepareForDisplay();
//	}
	
	@Override
	public Graph getResult() {
		return graphstreamInteractionsGraphGenerator.getGraph();
	}
}
