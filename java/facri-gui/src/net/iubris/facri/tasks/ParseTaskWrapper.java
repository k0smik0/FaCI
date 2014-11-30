package net.iubris.facri.tasks;

import javax.inject.Inject;

import net.iubris.facri.model.World;
import net.iubris.ishtaran.task.taskwrapper.AbstractTaskWrapper;

public class ParseTaskWrapper extends AbstractTaskWrapper<World> {

	@Inject
	public ParseTaskWrapper(ParserAble callable) {
		super(callable);
	}
}
