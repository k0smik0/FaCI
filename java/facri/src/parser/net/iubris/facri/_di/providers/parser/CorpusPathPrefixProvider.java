/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (CorpusPathPrefixProvider.java) is part of facri.
 * 
 *     CorpusPathPrefixProvider.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     CorpusPathPrefixProvider.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri._di.providers.parser;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class CorpusPathPrefixProvider implements Provider<String> {

	private final String corpusPrefix;

	@Inject
	public CorpusPathPrefixProvider(@Named("data_root_dir_path") String dataRootDirPath,
			@Named("me_id_file") String meIdFile) {
		this.corpusPrefix = dataRootDirPath+File.separatorChar+meIdFile;
	}
	
	@Override
	public String get() {
		return corpusPrefix;
	}

}
