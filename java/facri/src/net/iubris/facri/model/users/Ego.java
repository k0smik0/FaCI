package net.iubris.facri.model.users;

import java.util.HashSet;
import java.util.Set;

public class Ego extends AbstractUser {

	final private Set<String> friends = new HashSet<>();
	
	public Ego(String userId) {
		super(userId);
	}
	
	public Set<String> getFriendsIds() {
		return friends;
	}
	
	public void addFriendsIds(Set<String> friends) {
		this.friends.addAll(friends);
	}
	
	public boolean isMyFriendById(String userId) {
		return friends.contains(userId);
	}
}
