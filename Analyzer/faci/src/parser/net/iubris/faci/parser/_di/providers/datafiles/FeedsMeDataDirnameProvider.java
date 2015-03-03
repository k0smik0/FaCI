package net.iubris.faci.parser._di.providers.datafiles;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

public class FeedsMeDataDirnameProvider extends AbstractDataFilePathProvider {

	private final String path;

	@Inject
	public FeedsMeDataDirnameProvider(@Named("data_root_dir_path") String dataRootDirPath, @Named("feeds_me_dir_relative_path") String feedsMeDirRelativePath) {
		super(dataRootDirPath);
		
		this.path = dataRootDirPath+File.separatorChar+feedsMeDirRelativePath;
	}

	@Override
	public String get() {
		return path;
	}

}
