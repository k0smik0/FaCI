package net.iubris.faci.parser._di.providers.datafiles;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

public class MeInfoFilenameProvider extends AbstractDataFilePathProvider {

	private final String path;

	@Inject
	public MeInfoFilenameProvider(@Named("data_root_dir_path") String dataRootDirPath, 
			@Named("me_info_file") String meFileRelativePath) {
		super(dataRootDirPath);
		
		this.path = dataRootDirPath+File.separatorChar+meFileRelativePath;
	}

	@Override
	public String get() {
		return path;
	}

}
