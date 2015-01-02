package net.iubris.facri.grapher.generators.graphstream;

import javax.inject.Inject;
import javax.inject.Singleton;

import net.iubris.facri.grapher.generators.interactions.graphstream.GraphstreamInteractionsGraphGenerator;

@Singleton
public class GraphstreamGeneratorsHolder {
	@Inject public static GraphstreamInteractionsGraphGenerator graphstreamInteractionsGraphGenerator;
}
