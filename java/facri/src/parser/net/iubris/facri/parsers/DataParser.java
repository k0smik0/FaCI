package net.iubris.facri.parsers;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.world.World;
import net.iubris.facri.parsers.ego.EgoDataParser;
import net.iubris.facri.parsers.friends.FriendsDataParser;

@Singleton
public class DataParser implements Parser {

private final EgoDataParser egoDataParser;
private final FriendsDataParser friendsDataParser;
private final World world;
private boolean parsed = false;

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

	@Override
	public void parse(File... dummy) throws JAXBException, XMLStreamException, IOException {
		if (!parsed) {
			egoDataParser.parse();
			friendsDataParser.parse();
			parsed = true;
			world.setParsingDone(true);
			Ego myUser = world.getMyUser();
			System.out.println( 
				myUser.getUid()+": "
				+myUser.getOwnPostsCount()+" + "
				+myUser.getToOtherUsersInteractions().size()
			);
			/*world.getMyFriendsMap().values().stream().forEach(f->{
				System.out.println(
						f.getUid()+": "
						+f.getOwnPostsCount()+"+"
						+f.getToOtherUsersInteractions().size()
				);
			});*/
		}
	}
	
	public World getResult() {
		return world;
	}
}
