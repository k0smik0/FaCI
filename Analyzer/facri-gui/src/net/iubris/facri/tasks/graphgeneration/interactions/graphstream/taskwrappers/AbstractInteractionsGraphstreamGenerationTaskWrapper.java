package net.iubris.facri.tasks.graphgeneration.interactions.graphstream.taskwrappers;

import net.iubris.facri.tasks.graphgeneration.interactions.graphstream.graphables.AbstractInteractionsGraphstreamGraphAble;
import net.iubris.ishtaran.task.taskwrapper.AbstractTaskWrapper;

import org.graphstream.graph.Graph;

public abstract class AbstractInteractionsGraphstreamGenerationTaskWrapper extends AbstractTaskWrapper<Graph> {

	public AbstractInteractionsGraphstreamGenerationTaskWrapper(AbstractInteractionsGraphstreamGraphAble graphable) {
		super(graphable);
	}
}
