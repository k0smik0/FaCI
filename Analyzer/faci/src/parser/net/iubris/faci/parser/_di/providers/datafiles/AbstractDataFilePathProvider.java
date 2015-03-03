package net.iubris.faci.parser._di.providers.datafiles;

import javax.inject.Named;
import javax.inject.Provider;

public abstract class AbstractDataFilePathProvider implements Provider<String> {
	
	protected final String dataRootDirPath;
	
	public AbstractDataFilePathProvider(@Named("data_root_dir_path") String dataRootDirPath) {
		this.dataRootDirPath = dataRootDirPath; // "output")
	}
}
