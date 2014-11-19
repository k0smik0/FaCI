package net.iubris.facri.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.model.users.User;

@Singleton
public class World {

	private Ego myUser;
	private final Map<String,FriendOrAlike> myFriendsMap= new ConcurrentHashMap<>();
	private final Map<String,FriendOrAlike> otherUsersMap = new ConcurrentHashMap<>();
	private int newUser = 0;
	
	public Ego getMyUser() {
		return myUser;
	}
	public void setMyUser(Ego myUser) {
		this.myUser = myUser;
	}
	
	public void setMyFriendsMap(Map<String, FriendOrAlike> myFriendsMap) {
		this.myFriendsMap.putAll(myFriendsMap);
	}
	public Map<String, FriendOrAlike> getMyFriendsMap() {
		return myFriendsMap;
	}
	
	public void setOtherUsersMap(Map<String, FriendOrAlike> otherUsersMap) {
		this.otherUsersMap.putAll(otherUsersMap);
	}
	public Map<String, FriendOrAlike> getOtherUsersMap() {
		return otherUsersMap;
	}
	
	/**
	 * try to find an existant {@link User} from userId: if it not exists, a new one 
	 * is created, and its instance will be returned;
	 * obviously, if the user is "my user", a new {@link Ego} instance is created, 
	 * as it is a FriendOrAlike (and if it is a friend, it will be added to "friends" list)
	 * @param userId
	 * @return
	 */
	public User isExistentUserOrCreateNew(String userId) {
		
		if (myUser.getId().equals(userId))
			return myUser;
		
		FriendOrAlike user = null;
		if (myUser.isMyFriendById(userId)) {
//			System.out.println(userId+" friend!");
			if (myFriendsMap.containsKey(userId)) {
				user = myFriendsMap.get(userId);
			} else {
				user = new FriendOrAlike(userId);
				myFriendsMap.put(userId, user);
			}
		} else {
			if (otherUsersMap.containsKey(userId)) {
				user = otherUsersMap.get(userId);
			} else {
				user = new FriendOrAlike(userId);
				otherUsersMap.put(userId, user);
//				System.out.println("new user "+(++newUser ));
			}
		}
		
		return user;		
	}
}
