package net.iubris.facri.persisters._di.guice.module;

import java.io.File;

import net.iubris.berkeley_persister.config.StorageConfig;
import net.iubris.facri.persisters._di.annotations.PersisterPrefix;
import net.iubris.facri.persisters._di.guice.providers.FacriEntityModelProvider;
import net.iubris.facri.persisters._di.guice.providers.PersisterPrefixProvider;

import com.google.inject.AbstractModule;
import com.sleepycat.persist.model.EntityModel;

public class FacriPersisterModule extends AbstractModule {

	@Override
	protected void configure() {
//		bind(BerkeleyDBHelper.class).to(FacriBerkeleyDBAutoCommittableHelper.class);
		setStorageDir(); 
		bind(EntityModel.class).toProvider(FacriEntityModelProvider.class);
		
		bind(String.class).annotatedWith(PersisterPrefix.class).toProvider(PersisterPrefixProvider.class);
	}
	
	private void setStorageDir() {
		StorageConfig.SilosStorage = "cache"+File.separatorChar+"data";
		File dir = new File(StorageConfig.SilosStorage);
		if (!dir.exists())
			dir.mkdirs();		
	}
}
