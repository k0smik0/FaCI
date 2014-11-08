package net.iubris.facri.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Map;
import java.util.Scanner;

import javax.inject.Inject;
import javax.inject.Named;

import net.iubris.facri.model.User;

public class MutualFriendsParser implements Parser {
	
	private final String mutualFriendsFilename;
	
	@Inject
	public MutualFriendsParser(@Named("mutual_friends_filename") String mutualFriendsFilename) {
		this.mutualFriendsFilename = mutualFriendsFilename;
	}

	public void parse(File userDir, String owningWallUserId, Map<String, User> useridToUserMap) {
		File[] files = userDir.listFiles( new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.equals(mutualFriendsFilename)) {
					return true;
				}
				return false;
			}
		});
		if (files.length>0) {
			File mutualFriendsFile = files[0];
			try {
				Scanner sc = new Scanner(mutualFriendsFile);
				if (sc.hasNextLine())
					sc.nextLine(); // first line is header, so skip
				User user = ParsingUtils.isExistentUserOrCreateEmpty(owningWallUserId, useridToUserMap);
				while (sc.hasNextLine()) {
					String[] splitted = split(sc);
					if (splitted.length>0) {
						String friendId = splitted[0];
						user.addMutualFriend(friendId);
					}
				}			
				sc.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String[] split(Scanner sc) {
		return sc.nextLine().split("\t");
	}
}
