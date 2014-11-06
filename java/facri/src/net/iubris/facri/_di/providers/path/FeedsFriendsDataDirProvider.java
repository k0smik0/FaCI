package net.iubris.facri._di.providers.path;

import javax.inject.Provider;

public class FeedsFriendsDataDirProvider implements Provider<String> {

	
	private String feedsFriendsDataDir;
	
	@Override
	public String get() {
		return feedsFriendsDataDir;
	}

}
