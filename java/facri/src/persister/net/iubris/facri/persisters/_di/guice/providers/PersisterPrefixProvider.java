package net.iubris.facri.persisters._di.guice.providers;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class PersisterPrefixProvider implements Provider<String> {
	
	private final String prefix;

	@Inject
	public PersisterPrefixProvider(@Named("data_root_dir_path") String dataRootDirPath) {
		String[] splitted = dataRootDirPath.split("/");
		String last = splitted[splitted.length-1];
		this.prefix = last.replace("_corpus", "");
	}
	
	@Override
	public String get() {
		return prefix;
	}

}
