package net.iubris.facri._di.providers.corpusprefix;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class CorpusPrefixProvider implements Provider<String> {

	private final String corpusPrefix;

	@Inject
	public CorpusPrefixProvider(@Named("data_root_dir_path") String dataRootDirPath,
			@Named("me_id_file") String meIdFile) {
		this.corpusPrefix = dataRootDirPath+File.separatorChar+meIdFile;
	}
	
	@Override
	public String get() {
		return corpusPrefix;
	}

}