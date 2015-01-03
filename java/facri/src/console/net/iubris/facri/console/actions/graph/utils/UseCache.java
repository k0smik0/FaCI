package net.iubris.facri.console.actions.graph.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.parsers.DataParser;
import net.iubris.heimdall.command.ConsoleCommand;

import org.graphstream.graph.Graph;
import org.graphstream.stream.file.FileSink;
import org.graphstream.stream.file.FileSinkGraphML;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceGraphML;

public class UseCache {
	public boolean read;
	public boolean write;
	public UseCache(boolean cacheRead, boolean cacheWrite) {
		this.read = cacheRead;
		this.write = cacheWrite;
	}
	public void read(String filename, Graph graph) throws IOException {
		String filenamePath = "cache"+File.separatorChar+filename;
		FileSource fileSource =  
//					FileSourceFactory.sourceFor(filenamePath);
				new FileSourceGraphML();
		fileSource.addSink(graph);
//			fileSource.readAll(path); // read one-shot
		try {
			fileSource.begin(filenamePath);
			while (fileSource.nextEvents()) {
				// Optionally some code here ...
			}
			fileSource.end();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fileSource.removeSink(graph);
		}
	}
	public void write(String filename, Graph graph) throws IOException {
		FileSink fileSink = 
				new FileSinkGraphML();
//					new FileSinkDGS();
		File cacheDir = new File("cache");
		if (!cacheDir.exists())
			cacheDir.mkdir();
		fileSink.writeAll(graph, "cache"+File.separatorChar+filename);
	}
	public String getCacheFileExtension() {
		return "graphml";
	}
	
	public static UseCache handleUseCache(String[] actionParams, DataParser dataParser) throws JAXBException, XMLStreamException, IOException {
		boolean cacheRead = false, cacheWrite = false;

		if (actionParams.length<3) {
			dataParser.parse();
		}
		else /*if (actionParams.length==3)*/ {
			String useCacheString = actionParams[2];		
			UseCacheArguments useCacheArguments = UseCacheArguments.valueOf(useCacheString);
			try {
				switch(useCacheArguments) {
					// if read from cache, do not parse
					case cr:
						cacheRead = true;
						break;
					// write a cache, so parse for new data
					case cw:
						dataParser.parse();
						cacheWrite = true;
						break;
					default:
						break;
				}
			} catch (IllegalArgumentException e) {
				System.out.println("wrong argument");
			}
		}
		return new UseCache(cacheRead, cacheWrite);
	}
	
	public static UseCache noCache() {
		return new UseCache(false, false);
	}
	
	public enum UseCacheArguments implements ConsoleCommand {
		cw("cache read: (try to) import previous generated graph from a cached file") {},
		cr("cache write: export generated graph on a cache file") {};
		UseCacheArguments(String helpMessageCore) {
			this.helpMessage = 
//					ConsoleCommand.Utils.
					getPrefix(this,3)+helpMessageCore+"\n";
		}
		@Override
		public String getHelpMessage() {
			return helpMessage;
		}
		private final String helpMessage;
	}
}