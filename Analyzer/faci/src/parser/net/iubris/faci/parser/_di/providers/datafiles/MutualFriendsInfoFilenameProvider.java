package net.iubris.faci.parser._di.providers.datafiles;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class MutualFriendsInfoFilenameProvider implements Provider<String> {

	private final String filename;

	@Inject
	public MutualFriendsInfoFilenameProvider(@Named("mutual_friends_info_filename") String mutualFriendsInfoFilename) {
		this.filename = mutualFriendsInfoFilename;
	}

	@Override
	public String get() {
		return filename;
	}

}
