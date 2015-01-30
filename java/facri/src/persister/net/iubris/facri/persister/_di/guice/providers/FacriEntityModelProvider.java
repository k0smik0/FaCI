package net.iubris.facri.persister._di.guice.providers;

import javax.inject.Provider;

import net.iubris.berkeley_persister.core.entitymodel.EntityModelFactory;

import com.sleepycat.persist.model.EntityModel;

public class FacriEntityModelProvider implements Provider<EntityModel> {

	private final EntityModel entityModel;
	
	public FacriEntityModelProvider() {
		this.entityModel = EntityModelFactory.getEnhancedEntityModel(); 
//		= new AnnotationModel();
//		entityModel.registerClass( UrlPersisterProxy.class );
//		entityModel.registerClass( ConcurrentSkipListSetPersisterProxy.class );
//		entityModel.registerClass( ConcurrentHashMapPersisterProxy.class);
	}
	
	@Override
	public EntityModel get() {
		return entityModel;
	}
}
