package net.iubris.faci.grapher.holder._di.providers.graph;

import javax.inject.Singleton;

@Singleton
public class InteractionsGraphProvider extends AbstractGraphProvider {

	public InteractionsGraphProvider() {
		super("Interactions");
	}
}