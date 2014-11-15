package net.iubris.facri.tasks;

import javax.inject.Inject;

import net.iubris.facri.model.World;
import net.iubris.facri.parsers.AllDataParser;
import net.iubris.ishtaran.task.phasable.AbstractPhasable;
import net.iubris.ishtaran.task.phasable.CallState;

public class ParserAble extends AbstractPhasable<World> {

	private final AllDataParser allDataParser;
	
	@Inject
	public ParserAble(AllDataParser allDataParser) {
		this.allDataParser = allDataParser;
	}

	@Override
	public CallState call() throws Exception {
		allDataParser.parse();
		callState = CallState.COMPLETED;
		return callState;
	}

	@Override
	public World getPartialResult() {
		return allDataParser.getResult();
	}
}
