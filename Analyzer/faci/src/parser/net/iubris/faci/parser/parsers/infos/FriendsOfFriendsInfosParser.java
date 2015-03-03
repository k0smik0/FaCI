package net.iubris.faci.parser.parsers.infos;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import net.iubris.faci.model.world.World;
import net.iubris.faci.model.world.users.FriendOfFriend;
import net.iubris.faci.parser._di.annotations.datafiles.FriendsOfFriendsInfoFilename;
import net.iubris.faci.parser.parsers.Parser;
import net.iubris.faci.parser.parsers.utils.UserDataParser;

public class FriendsOfFriendsInfosParser implements Parser {

	// TODO use
	private final String friendsOfFriendsInfosFilename;
	private final World world;

	@Inject
	public FriendsOfFriendsInfosParser(
			@FriendsOfFriendsInfoFilename String friendsOfFriendsInfosFilename,
			World world) {
		this.friendsOfFriendsInfosFilename = friendsOfFriendsInfosFilename;
		this.world = world;
	}
	
	@Override
	public void parse(File... dummy) {
		try {
//			this.friendsIds.addAll( Files.readAllLines(Paths.get(friendsIdsFilename), Charset.defaultCharset()) );
			new UserDataParser<FriendOfFriend>(FriendOfFriend.class, friendsOfFriendsInfosFilename).parse()
			.forEach(friendOfFriend->{
				world.getFriendsOfFriendsMap().put(friendOfFriend.getUid(), friendOfFriend);
			});
					
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
