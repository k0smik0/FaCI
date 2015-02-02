/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (AbstractGraphExporter.java) is part of facri.
 * 
 *     AbstractGraphExporter.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     AbstractGraphExporter.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.grapher.exporter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.iubris.facri._di.annotations.grapher.corpus.CorpusPathPrefix;

public abstract class AbstractGraphExporter implements GraphExporter {
	
	protected final String corpusNameAsGraphFilesPrefix;

	public AbstractGraphExporter(@CorpusPathPrefix String corpusPrefixProvider) throws IOException {
		this.corpusNameAsGraphFilesPrefix = Files.readAllLines( new File(corpusPrefixProvider).toPath() ).get(0);
	}

}
