package net.iubris.facri.tasks.parse;

import javax.inject.Inject;

import net.iubris.faci.model.world.World;
import net.iubris.ishtaran.task.taskwrapper.AbstractTaskWrapper;

public class ParseTaskWrapper extends AbstractTaskWrapper<World> {

	@Inject
	public ParseTaskWrapper(ParserAble callable) {
		super(callable);
	}
	
	@Override
	public void postStart() throws Exception {
		super.postStart();
	}
}
