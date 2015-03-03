package net.iubris.faci.parser.parsers.data.friends;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import net.iubris.faci.model.world.World;
import net.iubris.faci.model.world.users.Friend;
import net.iubris.faci.parser._di.annotations.datafiles.FriendsOfFriendIdsFilename;
import net.iubris.faci.parser.parsers.Parser;

/**
 * this class parse 'his_friends_ids_from_posts_on_his_wall.txt' in each friend directory:
 * the file contains friends of friends ids obtained parsing posts json files, with Retriever 
 */
public class FriendsOfFriendIdsParser implements Parser {
	
	private final String friendsOfFriendIdsFilename;
	private final World world;

	@Inject
	public FriendsOfFriendIdsParser(@FriendsOfFriendIdsFilename String friendOfFriendsIdsFilename,
			World world) {
		this.friendsOfFriendIdsFilename = friendOfFriendsIdsFilename;
		this.world = world;
	}

	@Override
	public void parse(File... friendsDir) {
		File friendDir = friendsDir[0];
		
		String friendId = friendDir.getName();
		Friend friend = world.getFriendsMap().get(friendId);
		try {
			Files.list( FileSystems.getDefault().getPath(friendDir.getPath() ) )
				.filter(toFilterFilePath->toFilterFilePath.getFileName().toString().equals(friendsOfFriendIdsFilename))
				.forEach(
						filePath->{
							try {
								Files.readAllLines(filePath)
								.stream()
								.parallel()
								.forEach(
									friends_of_friends_ids_in_one_line->{ 
										List<String> friendsOfFriendsIds = Arrays.asList( friends_of_friends_ids_in_one_line.split(" ") );
										friend.addFriendsOfFriendsIds(friendsOfFriendsIds);
									}
								);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
