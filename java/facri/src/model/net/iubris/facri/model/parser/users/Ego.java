package net.iubris.facri.model.parser.users;

import java.net.URL;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.sleepycat.persist.model.Entity;

@Entity
public class Ego extends AbstractUser {

	private static final long serialVersionUID = -7587672566751011500L;

	final private Set<String> friends = 
			new ConcurrentSkipListSet<>();
//			new CopyOnWriteArraySet<String>();

	public Ego(String uid) {
		super(uid);
	}
	
	public Ego() {}
	
	public Ego(String uid, String name, int friendsCount, URL picSmall, URL profileURL, Sex sex, String significantOtherId) {
		this.uid = uid;
		this.name = name;
		this.friendsCount = friendsCount;
		this.picSmall = picSmall;
		this.profileURL = profileURL;
		this.sex = sex;
		this.significantOtherId = significantOtherId;
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
	
	@Override
	public String toString() {
		return "---\n"+super.toString()
				+"\nfriends count: "+friendsCount+"|"+friends.size()
				+"\n---";
	}
}
