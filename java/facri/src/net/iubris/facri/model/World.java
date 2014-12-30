package net.iubris.facri.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.BiConsumer;

import javax.inject.Singleton;

import net.iubris.facri.graph.generators.InteractionsWeigths;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.model.users.User;

@Singleton
public class World implements Serializable {

	private static final long serialVersionUID = -7112902990753202438L;
	
	private final Map<String,FriendOrAlike> myFriendsMap= new ConcurrentHashMap<>();
	private final Map<String,FriendOrAlike> otherUsersMap = new ConcurrentHashMap<>();
	
	private final Set<Integer> appreciationsRange = new ConcurrentSkipListSet<Integer>();
	private final Set<Integer> postsCountRange = new ConcurrentSkipListSet<Integer>();
	private final Set<Integer> interactionsRange = new ConcurrentSkipListSet<Integer>();
	
	private Ego myUser;

	private boolean parsingDone;
	
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
		if (myUser.getUid().equals(userId))
			return myUser;
		
		FriendOrAlike user = null;
		if (myUser.isMyFriendById(userId)) {
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
			}
		}
		
		int updatedOwnPostsCount = user.getOwnPostsCount();
		postsCountRange.add(updatedOwnPostsCount);
		
		int updatedAppreciation = user.getOwnLikedPostsCount() + InteractionsWeigths.RESHARED_OWN_POST*user.getOwnPostsResharingCount();
		appreciationsRange.add(updatedAppreciation);
		
		int updatedInteractions = user.getUserInteractionsCount();
		interactionsRange.add(updatedInteractions);
		
		return user;
	}
	
	public Optional<FriendOrAlike> searchUserByName(String name) {
		Optional<FriendOrAlike> friend = myFriendsMap.values().parallelStream().filter(f->f.getName().equals(name)).findFirst();
		if (friend.isPresent())
			return friend;
		else
			return otherUsersMap.values().parallelStream().filter(f->f.getName().equals(name)).findFirst();
	}
	public Optional<FriendOrAlike> searchUserById(String uid) {
		if (myFriendsMap.containsKey(uid)) {
			FriendOrAlike friendOrAlike = myFriendsMap.get(uid);
//			System.out.println(friendOrAlike);
			return Optional.of( friendOrAlike );
		}
		if (otherUsersMap.containsKey(uid)) {
			FriendOrAlike friendOrAlike = otherUsersMap.get(uid);
//			System.out.println(friendOrAlike);
			return Optional.of( friendOrAlike );
		}
		return Optional.empty();
	}
	public Ego searchMe() {
		return myUser;
	}
	public Optional<Ego> searchMe(String uid) {
//		System.out.println(myUser);
		if (uid.equals(myUser.getUid()))
			return Optional.of(myUser);
		return Optional.empty();
	}
	
	
	public Set<Integer> getAppreciationsRange() {
		return appreciationsRange;
	}
	public Set<Integer> getPostsCountRange() {
		return postsCountRange;
	}
	public Set<Integer> getInteractionsRange() {
		return interactionsRange;
	}
	
	public void testData() {
		BiConsumer<String, User> friendConsumer = new BiConsumer<String, User>() {
			@Override
			public void accept(String t, User u) {
				if (u instanceof FriendOrAlike) {
					FriendOrAlike f = (FriendOrAlike) u;
					if (f.getMutualFriends().size() >0)
						System.out.println(u.getUid()+" "+u.getOwnPostsCount()+","+u.getUserInteractionsCount()+","+f.getMutualFriends().size());
				}
			}
		};
		
		Ego ego = getMyUser();
		System.out.println(ego.getUid()+" "+ego.getOwnPostsCount()+","
				+ego.getUserInteractionsCount()+","+ego.getFriendsIds().size());
		System.out.println("");

		getMyFriendsMap()
			.forEach( friendConsumer );
		System.out.println("");
		
		getOtherUsersMap()
			.forEach( friendConsumer );
	}
	
	public void isParsingDone(boolean parsingDone) {
		this.parsingDone = parsingDone;
	}
	public boolean isParsingDone() {
		return parsingDone;
	};
}
