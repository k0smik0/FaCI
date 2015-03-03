package net.iubris.faci.parser._di.providers.datafiles;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

public class FeedsFriendsDataDirnameProvider extends AbstractDataFilePathProvider {

	private final String path;

	@Inject
	public FeedsFriendsDataDirnameProvider(@Named("data_root_dir_path") String dataRootDirPath,
			@Named("feeds_friends_dir_relative_path") String feedsFriendsDirRelativePath // "friends"
			) {
		super(dataRootDirPath);
		this.path = dataRootDirPath+File.separatorChar+feedsFriendsDirRelativePath;
	}

	@Override
	public String get() {
		return path;
	}

}
