package net.iubris.facri.tasks.parse;


import javax.inject.Inject;

import net.iubris.faci.model.world.World;
import net.iubris.faci.parser.parsers.GlobalParser;
import net.iubris.facri.gui._di.providers.DataParserProvider;
import net.iubris.ishtaran.task.phasable.AbstractPhasable;
import net.iubris.ishtaran.task.phasable.CallState;
import net.iubris.ishtaran.utils.printor.Printor;

public class ParserAble extends AbstractPhasable<World> {

	private DataParser dataParser;
	private final DataParserProvider dataParserProvider;
	private final Printor.Gui printorGui;
	private final GlobalParser globalParser;
	
	@Inject
	public ParserAble(DataParserProvider dataParserProvider, /*GlobalParser globalParser,*/ Printor.Gui printorGui) {
//this.globalParser = globalParser;
		this.dataParserProvider = dataParserProvider;
		this.printorGui = printorGui;
	}

	@Override
	public CallState call() throws Exception {
		if (dataParser==null) {
			dataParser = dataParserProvider.get();
		}		
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
