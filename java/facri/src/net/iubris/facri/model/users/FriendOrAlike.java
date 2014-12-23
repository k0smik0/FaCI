package net.iubris.facri.model.users;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.googlecode.jcsv.annotations.MapToColumn;

public class FriendOrAlike extends AbstractUser {

	private static final long serialVersionUID = 6593382542268675220L;

	final private Set<String> mutualFriends = new HashSet<>();
	
	public FriendOrAlike(String uid) {
		super(uid);
	}
	
//	public FriendOrAlike(String uid, String name, )
	
	public FriendOrAlike() {}
	
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
//	@Override
	public int getMutualFriendCount() {
		return mutualFriends.size();
	}
	public boolean isMutualFriend(String userId) {
		return mutualFriends.contains(userId);
	}
	
//	@MapToColumn(column=0)
//	private String uid;
//	
//	@MapToColumn(column=1)
//	private String name;
//	
	@MapToColumn(column=2)
	private int friendsCount;
		
	public int getFriendsCount() {
		return friendsCount;
	}
	
//	@MapToColumn(column=3)
//	private int mutualFriendCount;
//	
//	@MapToColumn(column=4)
//	private URL picSmallURL;
//	
//	@MapToColumn(column=5)
//	private URL profileURL;
//	
//	@MapToColumn(column=6)
//	private Sex sex;
//	
//	@MapToColumn(column=7)
//	private String significant_other_id;
	
}
