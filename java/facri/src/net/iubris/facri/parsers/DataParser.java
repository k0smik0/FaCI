package net.iubris.facri.parsers;

import java.io.FileNotFoundException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.model.World;
import net.iubris.facri.parsers.ego.EgoDataParser;
import net.iubris.facri.parsers.friends.FriendsDataParser;
@Singleton
public class DataParser {

private final EgoDataParser egoDataParser;
private final FriendsDataParser friendsDataParser;
private final World world;

	@Inject
	public DataParser(
			EgoDataParser egoDataParser,
			FriendsDataParser friendsDataParser,
			World world
			) {
				this.egoDataParser = egoDataParser;
				this.friendsDataParser = friendsDataParser;
				this.world = world;
	}

	public void parse() throws JAXBException, FileNotFoundException, XMLStreamException {
		egoDataParser.parse();
		friendsDataParser.parse();		
	}
	
	public World getResult() {
		return world;
	}
}
