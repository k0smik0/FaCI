package net.iubris.faci.parser._di.providers.datafiles;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

public class FriendsIdsFilenameProvider extends AbstractDataFilePathProvider {

	private final String path;

	@Inject
	public FriendsIdsFilenameProvider(@Named("data_root_dir_path") String dataRootDirPath, 
			@Named("friends_ids_file") String friendsIdsFile			
			) {
		super(dataRootDirPath);
		this.path = dataRootDirPath+File.separatorChar+friendsIdsFile;
	}

	@Override
	public String get() {
		return path;
	}

}
