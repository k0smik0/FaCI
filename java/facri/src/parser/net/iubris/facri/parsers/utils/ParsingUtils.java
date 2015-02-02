/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (ParsingUtils.java) is part of facri.
 * 
 *     ParsingUtils.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     ParsingUtils.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.parsers.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.iubris.facri.model.parser.users.Ego;
import net.iubris.facri.model.parser.users.FriendOrAlike;
import net.iubris.facri.model.parser.users.User;
import net.iubris.facri.model.world.World;
import net.iubris.facri.utils.Printer;

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
			Printer.println("empty");
			return false;
		}
		return true;
	}

}
