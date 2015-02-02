/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (GephiGraphExporter.java) is part of facri.
 * 
 *     GephiGraphExporter.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     GephiGraphExporter.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.grapher.exporter.gephi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.inject.Inject;

import net.iubris.facri._di.annotations.grapher.corpus.CorpusPathPrefix;
import net.iubris.facri.grapher.exporter.AbstractGraphExporter;

import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.CharacterExporter;
import org.gephi.io.exporter.spi.Exporter;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

public class GephiGraphExporter extends AbstractGraphExporter {
	
	private final Workspace workspace;

	@Inject
	public GephiGraphExporter(Workspace workspace
			,@CorpusPathPrefix String corpusPrefixProvider
			) throws IOException {
		super(corpusPrefixProvider);
		this.workspace = workspace;
	}
	
	@Override
	public void exportGraphToGraphML(String fileName) {
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);
		Exporter exporterGraphML = ec.getExporter("graphml");    //Get GraphML exporter
		exporterGraphML.setWorkspace(workspace);
		ec.exportWriter(new StringWriter(), (CharacterExporter) exporterGraphML);
		try {
			File outputDir = new File("graphmls");
			if (!outputDir.exists())
				outputDir.mkdirs();
			FileWriter f=  new FileWriter("graphmls"+File.pathSeparator+super.corpusNameAsGraphFilesPrefix+"_-_"+fileName+".graphml");			
			ec.exportWriter(f, (CharacterExporter) exporterGraphML);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
