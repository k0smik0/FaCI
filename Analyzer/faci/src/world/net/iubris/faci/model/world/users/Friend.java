package net.iubris.faci.model.world.users;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sleepycat.persist.model.Entity;

@Entity
public class Friend extends AbstractFriend {

	private static final long serialVersionUID = 4346942933270980240L;
	
	protected final Set<String> friendsOfFriendsIds = new HashSet<>();

	Friend() {}

	public Friend(String uid, String name, int friendsCount, int mutualFriendsCount, URL picSmall, URL profileURL,
			User.Sex sex, String significantOtherId) {
		super(uid, name, friendsCount, mutualFriendsCount, picSmall, profileURL, sex, significantOtherId);
	}

	public Friend(String uid) {
		super(uid);
	}
	
	public Set<String> getFriendsOfFriendsIds() {
		return friendsOfFriendsIds;
	}
	public void addFriendsOfFriendsId(String friendOfFriendId) {
		friendsOfFriendsIds.add(friendOfFriendId);
	}
	public void addFriendsOfFriendsIds(Set<String> friendsOfFriendsIds) {
		friendsOfFriendsIds.addAll(friendsOfFriendsIds);
	}
	public void addFriendsOfFriendsIds(List<String> friendsOfFriendsIds) {
		this.friendsOfFriendsIds.addAll( friendsOfFriendsIds );
	}
	public boolean isFriendOfFriend(String userId) {
		return friendsOfFriendsIds.contains(userId);
	}
}
