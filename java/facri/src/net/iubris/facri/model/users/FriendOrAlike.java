package net.iubris.facri.model.users;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FriendOrAlike extends AbstractUser {

	private static final long serialVersionUID = 6593382542268675220L;

	final private Set<String> mutualFriends = new HashSet<>();
	
	public FriendOrAlike(String userId) {
		super(userId);
	}
	
	public void addMutualFriend(String friendId) {
//		System.out.println("adding "+friendId);
		mutualFriends.add(friendId);		
	}
	public void addMutualFriends(Collection<String> friendsIds) {
		mutualFriends.addAll(friendsIds);		
	}
	public Set<String> getMutualFriends(){
		return mutualFriends;
	}
	public boolean isMutualFriend(String userId) {
		return mutualFriends.contains(userId);
	}
}
