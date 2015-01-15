package net.iubris.facri.parsers.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.model.users.User;
import net.iubris.facri.model.world.World;

public class ParsingUtils {
	
	private World world;
	
	@Inject	
	public ParsingUtils(World world) {
		this.world = world;
	}

	public User isExistentFriendUserOrCreateEmpty(String userId/*, Map<String,User> useridToUserMap*/) {
		Ego myUser = world.getMyUser();
		if (myUser.getUid().equals(userId))
			return myUser;
		
		FriendOrAlike user = null;
		if (myUser.isMyFriendById(userId)) {
			Map<String, FriendOrAlike> myFriendsMap = world.getMyFriendsMap();
			if (myFriendsMap.containsKey(userId)) {
				user = myFriendsMap.get(userId);
			} else {
				user = new FriendOrAlike(userId);
				myFriendsMap.put(userId, user);
			}
		} else {
			Map<String, FriendOrAlike> otherUsersMap = world.getOtherUsersMap();
			if (otherUsersMap.containsKey(userId)) {
				user = otherUsersMap.get(userId);
			} else {
				user = new FriendOrAlike(userId);
				otherUsersMap.put(userId, user);
			}
		}
		
		return user;
	}
	
	public static List<File> getDirectories(String dirName) {
		List<File> dataDirs = Arrays.asList( new File(dirName).listFiles() );
		return dataDirs;
	}
	
	public static boolean isDirContainingPostsDataFile(File userDir) {
//		incrementUserCounter();
		// userId is Post.sourceId, that is the id of user which wall contains the post
//		String owningWallUserId = userDir.getName();
//System.out.print("["+userCounter+"/"+usersTotal+"] user "+owningWallUserId+": ");
		
		File[] files = userDir.listFiles();
		if (files.length==0) {
			System.out.println("empty");
			return false;
		}
		return true;
	}

}
