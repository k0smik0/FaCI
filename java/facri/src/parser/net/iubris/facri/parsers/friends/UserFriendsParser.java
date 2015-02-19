package net.iubris.facri.parsers.friends;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import net.iubris.facri.model.parser.users.Friend;
import net.iubris.facri.model.world.World;
import net.iubris.facri.parsers.Parser;

public class UserFriendsParser implements Parser {
	
	private final String userFriendsIdsFilename;
	private final World world;

	@Inject
	public UserFriendsParser(@Named("user_friends_of_friends_ids_filename") String userFriendsIdsFilename,
			World world) {
		this.userFriendsIdsFilename = userFriendsIdsFilename;
		this.world = world;
	}

	@Override
	public void parse(File... friendsDir) {
		File friendDir = friendsDir[0];
		
		String friendId = friendDir.getName();
		Friend friend = world.getMyFriendsMap().get(friendId);
		try {
			Files.list( FileSystems.getDefault().getPath(friendDir.getPath() ) )
				.filter(toFilterFilePath->toFilterFilePath.getFileName().toString().equals(userFriendsIdsFilename))
				.forEach(
						filePath->{
							try {
								Files.readAllLines(filePath)
								.stream()
								.parallel()
								.forEach(
									friends_of_friends_ids_in_one_line->{ 
										List<String> friendsOfFriendsIds = Arrays.asList( friends_of_friends_ids_in_one_line.split(" ") );
										friend.addFriendsOfFriends(friendsOfFriendsIds);
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
