package net.iubris.facri.model.users;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.sleepycat.persist.model.Entity;

@Entity
public class FriendOrAlike extends AbstractUser {

	private static final long serialVersionUID = 6593382542268675220L;

	final private Set<String> mutualFriends = new HashSet<>();
	
	public FriendOrAlike(String uid) {
		super(uid);
	}
	
	public FriendOrAlike() {}
	
	public FriendOrAlike(String uid, String name, int friendsCount, int mutualFriendsCount, URL picSmall, URL profileURL, Sex sex, String significantOtherId) {
		this.uid = uid;
		this.name = name;
		this.friendsCount = friendsCount;
		this.mutualFriendsCount = mutualFriendsCount;
		this.picSmall = picSmall;
		this.profileURL = profileURL;
		this.sex = sex;
		this.significantOtherId = significantOtherId;
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
	
	
//	@MapToColumn(column=3,type=Integer.class)
	private int mutualFriendsCount;
	
	public void setMutualFriendsCount(int mutualFriendsCount) {
		this.mutualFriendsCount = mutualFriendsCount;
	}
	public int getMutualFriendsCount() {
		return mutualFriendsCount;
	}
	
	@Override
	public String toString() {
		return "---\n"+super.toString()
				+"\nfriends count: "+friendsCount
				+"\nmutual friends count: "+mutualFriendsCount+"|"+mutualFriends.size()
				+"\n---";
	}
}
