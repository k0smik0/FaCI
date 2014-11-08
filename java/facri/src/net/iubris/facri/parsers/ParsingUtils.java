package net.iubris.facri.parsers;

import java.util.Map;

import net.iubris.facri.model.User;

public class ParsingUtils {
	
	public static User isExistentUserOrCreateEmpty(String userId, Map<String,User> useridToUserMap) {
		User user = null;
		if (useridToUserMap.containsKey(userId)) {
			user = useridToUserMap.get(userId);
		} else {
			user = new User(userId);
			useridToUserMap.put(userId, user);
		}
		return user;
	}

}
