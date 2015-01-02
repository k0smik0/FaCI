package net.iubris.facri.parsers;

import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.model.World;
import net.iubris.facri.model.users.FriendOrAlike;
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
	public void parse(File... dummyArgs) throws JAXBException, XMLStreamException, IOException {
		if (!parsed) {
			egoDataParser.parse();
			friendsDataParser.parse();
			
			System.out.println("World:");
			System.out.println("Ego:");
			System.out.println("\tposts: "+world.getMyUser().getOwnPostsCount() );
			world.getMyFriendsMap().forEach(
					new BiConsumer<String, FriendOrAlike>() {
						@Override
						public void accept(String t, FriendOrAlike u) {
							System.out.println(t+": "+u.getUId());
						}
					}
				);
			parsed = true;
		}
	}
	
	public World getResult() {
		return world;
	}
}
