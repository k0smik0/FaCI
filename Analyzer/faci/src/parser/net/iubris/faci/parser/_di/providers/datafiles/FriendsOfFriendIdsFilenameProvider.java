package net.iubris.faci.parser._di.providers.datafiles;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

public class FriendsOfFriendIdsFilenameProvider extends AbstractDataFilePathProvider {

	private final String path;

	@Inject
	public FriendsOfFriendIdsFilenameProvider(@Named("data_root_dir_path") String dataRootDirPath, 
			@Named("friends_of_friend_ids_filename") String friendsOfFriendsIdsFile			
			) {
		super(dataRootDirPath);
		this.path = dataRootDirPath+File.separatorChar+friendsOfFriendsIdsFile;
	}

	@Override
	public String get() {
		return path;
	}

}
