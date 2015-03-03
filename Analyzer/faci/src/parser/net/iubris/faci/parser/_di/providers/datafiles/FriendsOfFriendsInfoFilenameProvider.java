package net.iubris.faci.parser._di.providers.datafiles;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

public class FriendsOfFriendsInfoFilenameProvider extends AbstractDataFilePathProvider {

	private final String path;

	@Inject
	public FriendsOfFriendsInfoFilenameProvider(@Named("data_root_dir_path") String dataRootDirPath, 
			@Named("friends_info_file") String friendsInfosFileRelativePath
			) {
		super(dataRootDirPath);
		
		this.path = dataRootDirPath+File.separatorChar+friendsInfosFileRelativePath;
	}

	@Override
	public String get() {
		return path;
	}

}
