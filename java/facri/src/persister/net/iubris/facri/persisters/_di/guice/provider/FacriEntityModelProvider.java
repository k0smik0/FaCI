package net.iubris.facri.persisters._di.guice.provider;

import javax.inject.Provider;

import net.iubris.facri.persisters.base.UrlPersisterProxy;

import com.sleepycat.persist.model.AnnotationModel;
import com.sleepycat.persist.model.EntityModel;

public class FacriEntityModelProvider implements Provider<EntityModel> {

	private final EntityModel entityModel;
	
	public FacriEntityModelProvider() {
		this.entityModel = new AnnotationModel();
		entityModel.registerClass( UrlPersisterProxy.class );
	}
	
	@Override
	public EntityModel get() {
		return entityModel;
	}
}
