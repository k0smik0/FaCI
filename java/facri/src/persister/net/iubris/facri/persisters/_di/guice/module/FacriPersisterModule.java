package net.iubris.facri.persisters._di.guice.module;

import net.iubris.facri.persisters._di.guice.provider.FacriEntityModelProvider;

import com.google.inject.AbstractModule;
import com.sleepycat.persist.model.EntityModel;

public class FacriPersisterModule extends AbstractModule {

	@Override
	protected void configure() {
//		bind(BerkeleyDBHelper.class).to(FacriBerkeleyDBAutoCommittableHelper.class);
		bind(EntityModel.class).toProvider(FacriEntityModelProvider.class);
	}
}
