package net.iubris.facri.tasks;

import java.util.Map;

import net.iubris.facri.model.User;
import net.iubris.ishtaran.task.taskwrapper.AbstractTaskWrapper;

public class ParseTaskWrapper extends AbstractTaskWrapper<Map<String, User>> {

	public ParseTaskWrapper(ParserAble callable) {
		super(callable);
	}

}
