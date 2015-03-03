/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (PersisterPrefixProvider.java) is part of facri.
 * 
 *     PersisterPrefixProvider.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     PersisterPrefixProvider.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.persister._di.guice.providers;

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
