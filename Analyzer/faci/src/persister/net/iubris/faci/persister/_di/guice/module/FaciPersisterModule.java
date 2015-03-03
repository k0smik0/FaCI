/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FacriPersisterModule.java) is part of facri.
 * 
 *     FacriPersisterModule.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FacriPersisterModule.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.persister._di.guice.module;

import java.io.File;

import net.iubris.faci.persister._di.annotations.PersisterPrefix;
import net.iubris.faci.persister._di.guice.providers.PersisterPrefixProvider;
import net.iubris.jbbp.config.StorageConfig;

import com.google.inject.AbstractModule;

public class FaciPersisterModule extends AbstractModule {

	@Override
	protected void configure() {
		setStorageDir(); 
		
		bind(String.class).annotatedWith(PersisterPrefix.class).toProvider(PersisterPrefixProvider.class);
	}
	
	private void setStorageDir() {
		StorageConfig.SilosStorage = "cache"+File.separatorChar+"data";
		File dir = new File(StorageConfig.SilosStorage);
		if (!dir.exists())
			dir.mkdirs();		
	}
}
