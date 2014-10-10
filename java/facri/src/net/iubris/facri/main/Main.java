package net.iubris.facri.main;

import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.config.Config;
import net.iubris.facri.parser.JSONParser;


public class Main {

	public static void main(String[] args) {
		
		String dataRootDir;
		if (args.length > 0 && !args[1].isEmpty())
			dataRootDir = args[1];
		else
			dataRootDir = Config.DATA_ROOT_DIR;
		
		JSONParser jsonParser = new JSONParser(dataRootDir);
		
		try {
			jsonParser.parseFeed();
		} catch (FileNotFoundException | JAXBException | XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
