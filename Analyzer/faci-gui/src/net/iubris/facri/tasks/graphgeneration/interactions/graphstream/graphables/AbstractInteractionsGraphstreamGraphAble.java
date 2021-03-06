package net.iubris.facri.tasks.graphgeneration.interactions.graphstream.graphables;

import org.graphstream.graph.Graph;

import net.iubris.faci.grapher.generators.specialized.interactions.graphstream.GraphstreamInteractionsGraphGenerator;
import net.iubris.ishtaran.task.phasable.AbstractPhasable;

public abstract class AbstractInteractionsGraphstreamGraphAble extends AbstractPhasable<Graph> {

	protected final GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator;
	
	public AbstractInteractionsGraphstreamGraphAble(GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator) {
		this.graphstreamInteractionsGraphGenerator = graphstreamInteractionsGraphGenerator;
	}
	
	@Override
	public Graph getResult() {
		return graphstreamInteractionsGraphGenerator.getGraph();
	}
}
