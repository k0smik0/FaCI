/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FacriEntityModelProvider.java) is part of facri.
 * 
 *     FacriEntityModelProvider.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FacriEntityModelProvider.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
