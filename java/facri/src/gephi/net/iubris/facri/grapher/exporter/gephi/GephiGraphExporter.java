package net.iubris.facri.grapher.exporter.gephi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.inject.Inject;

import net.iubris.facri._di.annotations.grapher.corpus.CorpusPrefix;
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
			,@CorpusPrefix String corpusPrefixProvider
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
