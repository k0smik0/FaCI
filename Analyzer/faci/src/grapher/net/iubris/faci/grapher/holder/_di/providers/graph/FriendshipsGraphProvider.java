package net.iubris.faci.grapher.holder._di.providers.graph;

import javax.inject.Singleton;

@Singleton
public class FriendshipsGraphProvider extends AbstractGraphProvider {

	public FriendshipsGraphProvider() {
		super("Friendships");
	}
}