package net.iubris.facri.model.parser.users;

import java.net.URL;

import com.sleepycat.persist.model.Entity;

@Entity
public class Friend extends AbstractFriend {

	private static final long serialVersionUID = 4346942933270980240L;

	public Friend() {
		super();
	}

	public Friend(String uid, String name, int friendsCount, int mutualFriendsCount, URL picSmall, URL profileURL,
			User.Sex sex, String significantOtherId) {
		super(uid, name, friendsCount, mutualFriendsCount, picSmall, profileURL, sex, significantOtherId);
	}

	public Friend(String uid) {
		super(uid);
	}
	
}
