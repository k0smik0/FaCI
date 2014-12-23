package net.iubris.facri.model.users;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class Ego extends AbstractUser {

	private static final long serialVersionUID = -7587672566751011500L;

	final private Set<String> friends = new ConcurrentSkipListSet<>();
	
	public Ego(String userId) {
		super(userId);
	}
	
	public Set<String> getFriendsIds() {
		return friends;
	}
	@Override
	public int getFriendsCount() {
		return friends.size();
	}
	
	public void addFriendsIds(Set<String> friends) {
		this.friends.addAll(friends);
		System.out.println("friends: "+friends.size());
	}
	
	public boolean isMyFriendById(String userId) {
		return friends.contains(userId);
	}
	
//	@MapToColumn(column=1)
//	private String uid;
//	
//	@MapToColumn(column=2)
//	private int friendsCount;
//	
//	@MapToColumn(column=4)
//	protected URL picSmallURL;
//	
//	@MapToColumn(column=5)
//	protected URL profileURL;
//	
//	@MapToColumn(column=6)
//	protected Sex sex;
//	
//	@MapToColumn(column=7)
//	protected String significant_other_id;
}
