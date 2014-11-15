package net.iubris.facri.tasks;

import net.iubris.facri.model.World;
import net.iubris.ishtaran.task.taskwrapper.AbstractTaskWrapper;

public class ParseTaskWrapper extends AbstractTaskWrapper<World> {

	public ParseTaskWrapper(ParserAble callable) {
		super(callable);
	}

}
