package net.iubris.faci.parser._di.providers.datafiles;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

public class FriendsInfoFilenameProvider extends AbstractDataFilePathProvider {

	private final String path;

	@Inject
	public FriendsInfoFilenameProvider(@Named("data_root_dir_path") String dataRootDirPath, 
			@Named("friends_info_file") String friendsFileRelativePath
			) {
		super(dataRootDirPath);
		
		this.path = dataRootDirPath+File.separatorChar+friendsFileRelativePath;
	}

	@Override
	public String get() {
		return path;
	}

}
