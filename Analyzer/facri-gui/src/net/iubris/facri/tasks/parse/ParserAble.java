package net.iubris.facri.tasks.parse;

import javax.inject.Inject;

import net.iubris.facri.gui._di.providers.DataParserProvider;
import net.iubris.facri.model.World;
import net.iubris.facri.parsers.DataParser;
import net.iubris.ishtaran.task.phasable.AbstractPhasable;
import net.iubris.ishtaran.task.phasable.CallState;
import net.iubris.ishtaran.utils.printor.Printor;

public class ParserAble extends AbstractPhasable<World> {

	private DataParser dataParser;
	private final DataParserProvider dataParserProvider;
	private final Printor.Gui printorGui;
	
	@Inject
	public ParserAble(DataParserProvider dataParserProvider, Printor.Gui printorGui) {
		this.dataParserProvider = dataParserProvider;
		this.printorGui = printorGui;
	}

	@Override
	public CallState call() throws Exception {
		if (dataParser==null)
			dataParser = dataParserProvider.get();
		
		dataParser.parse();
		callState = CallState.COMPLETED;
		return callState;
	}
	
	@Override
	public void stop() {
		printorGui.println("sorry, this job can not be interrupted");
		
		super.stop();
	}

	@Override
	public World getResult() {
		return dataParser.getResult();
//		return null;
	}
}
