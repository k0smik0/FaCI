package net.iubris.facri.tasks;

import java.util.Map;

import javax.inject.Inject;

import net.iubris.facri.model.User;
import net.iubris.facri.parsers.GlobalParser;
import net.iubris.ishtaran.task.phasable.AbstractPhasable;
import net.iubris.ishtaran.task.phasable.CallState;

public class ParserAble extends AbstractPhasable<Map<String, User>> {

	private final GlobalParser globalParser;
	
	@Inject
	public ParserAble(GlobalParser globalParser) {
		this.globalParser = globalParser;
	}

	@Override
	public CallState call() throws Exception {
		globalParser.parse();
		callState = CallState.COMPLETED;
		return callState;
	}

	@Override
	public Map<String, User> getPartialResult() {
		return globalParser.getUseridToUserMap();
	}

}
