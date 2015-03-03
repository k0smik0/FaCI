/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (World.java) is part of facri.
 * 
 *     World.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     World.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.model.world;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import net.iubris.faci.grapher.generators.base.InteractionsWeigths.Interactions;
import net.iubris.faci.model.world.users.Ego;
import net.iubris.faci.model.world.users.Friend;
import net.iubris.faci.model.world.users.FriendOfFriend;
import net.iubris.faci.model.world.users.GenericUser;
import net.iubris.faci.model.world.users.User;

import com.sleepycat.persist.model.Entity;

@Singleton
@Entity
public class World implements Serializable {

	private static final long serialVersionUID = -7112902990753202438L;
	
	private final Map<String,Friend> friendsMap= new ConcurrentHashMap<>();
	private final Map<String,FriendOfFriend> friendsOfFriendsMap = new ConcurrentHashMap<>();
	private final Map<String,GenericUser> otherUsersMap = new ConcurrentHashMap<>();
	
	private final Set<Float> appreciationsRange = new ConcurrentSkipListSet<Float>();
	private final Set<Integer> postsCountRange = new ConcurrentSkipListSet<Integer>();
	private final Set<Integer> interactionsRange = new ConcurrentSkipListSet<Integer>();
	
	private Ego myUser;

	private boolean parsingDone = false;

	
	
	public Ego getMyUser() {
		return myUser;
	}
	public void setMyUser(Ego myUser) {
		this.myUser = myUser;
	}
	
//	public void populateFriendsMap(Map<String, Friend> friendsMap) {
//		this.friendsMap.putAll(friendsMap);
//	}
	public Map<String, Friend> getFriendsMap() {
		return friendsMap;
	}
	
//	public void populateFriendsOfFriendsMap(Map<String, FriendOfFriend> friendsOfFriendsMap) {
//		this.friendsOfFriendsMap.putAll(friendsOfFriendsMap);
//	}
	public Map<String, FriendOfFriend> getFriendsOfFriendsMap() {
		return friendsOfFriendsMap;
	}
	
//	public void populateOtherUsersMap(Map<String, GenericUser> otherUsersMap) {
//		this.otherUsersMap.putAll(otherUsersMap);
//	}
	public Map<String, GenericUser> getOtherUsersMap() {
		return otherUsersMap;
	}
	
	/**
	 * try to find an existant {@link User} from userId:<br/>
	 * if it not exists, a new one is created, and its instance will be returned;<br/>
	 * obviously, if the user is "my user", a new {@link Ego} instance is created, 
	 * as it is a FriendOrAlike<br/>
	 * (and if it is a friend, it will be added to "friends" list)
	 * @param userId
	 * @return User which uid is equal to userId
	 */
	public User isExistentUserOrCreateNew(String userId) {
		if (myUser.getUid().equals(userId))
			return myUser;
		
		if (myUser.isMyFriendById(userId)) {
			return isExistentFriendOrCreateNew(userId);
		}
		
		return isExistentFriendOfFriendOrCreateNew(userId);
	}
	/**
	 * try to find an existant {@link User} from userId:<br/>
	 * if it not exists, a new one is created, and its instance will be returned;<br/>
	 * obviously, if the user is "my user", a new {@link Ego} instance is created, 
	 * as it is a FriendOrAlike<br/>
	 * (and if it is a friend, it will be added to "friends" list)
	 * <br/><br/>
	 * In addition, if subjectUserId is not a Ego's friend 
	 * (and, obviously, he is not mutual friend for my friends) and targetUserId is a friend,
	 * subjectUserId will be necessarily him friend (that is: a friend of friend), so he will
	 * be added as friend for my friend (the target), and my friend will be added as mutual 
	 * for friend of friend (the subject)   
	 * @param subjectUserId
	 * @param targetUserId
	 * @return the subject User
	 */
	/*public User isExistentUserOrCreateNewFromPost(String subjectUserId, String targetUserId) {
		// subjectUserId is Ego, so targetUserId is necessarily Ego or Friend - go further
		if (myUser.getUid().equals(subjectUserId))
			return myUser;
		
		// subjectUserId is friend, so targetUserId is necessarily Ego or Friend - go further
		if (myUser.isMyFriendById(subjectUserId)) {
			return isExistentFriendOrCreateNew(subjectUserId);
		}
		
		FriendOfFriend friendOfFriend = isExistentFriendOfFriendOrCreateNew(subjectUserId);
		isExistentFriendOrCreateNew(targetUserId).addFriendsOfFriendsId(subjectUserId);
		friendOfFriend.addMutualFriendId(targetUserId);
		
		return friendOfFriend;
	}*/
	
	public Friend isExistentFriendOrCreateNew(String friendId) {		
		Friend friend = null;
		if (myUser.isMyFriendById(friendId)) {
			if (friendsMap.containsKey(friendId)) {
				friend = friendsMap.get(friendId);
			} else {
				friend = new Friend(friendId);
				friendsMap.put(friendId, friend);
			}
		}
		
		int updatedOwnPostsCount = friend.getOwnPostsCount();
		postsCountRange.add(updatedOwnPostsCount);
		
		float updatedAppreciation = friend.getOwnLikedPostsCount() + Interactions.RESHARED_OWN_POST*friend.getOwnPostsResharingCount();
		appreciationsRange.add(updatedAppreciation);
		
		int updatedInteractions = friend.getToOtherUsersInteractionsCount();
		interactionsRange.add(updatedInteractions);
		
		return friend;
	}
	
	public FriendOfFriend isExistentFriendOfFriendOrCreateNew(String userId) {
		FriendOfFriend user = null;
		if (friendsOfFriendsMap.containsKey(userId)) {
			user = friendsOfFriendsMap.get(userId);
		} else {
			user = new FriendOfFriend(userId);
			friendsOfFriendsMap.put(userId, user);
		}
		
		int updatedOwnPostsCount = user.getOwnPostsCount();
		postsCountRange.add(updatedOwnPostsCount);
		
		float updatedAppreciation = user.getOwnLikedPostsCount() + Interactions.RESHARED_OWN_POST*user.getOwnPostsResharingCount();
		appreciationsRange.add(updatedAppreciation);
		
		int updatedInteractions = user.getToOtherUsersInteractionsCount();
		interactionsRange.add(updatedInteractions);
		
		return user;
	}
	
	public User isExistantGenericUserOrCreateNew(String subjectUserId) {
		if (otherUsersMap.containsKey(subjectUserId))
			return otherUsersMap.get(subjectUserId);
		else {
			GenericUser gu = new GenericUser(subjectUserId);
			otherUsersMap.put(subjectUserId, gu);
			return gu;
		}
	};
	
	
	public Optional<? extends User> searchUserByName(String name) {
		Optional<Friend> friend = Optional.of(
				friendsMap
				.values()
				.stream()
				.parallel()
				.filter( f-> name.equals(f.getName()) )
				.collect( Collectors.toList() ).get(0) 
			);
		if (friend.isPresent())
			return friend;
		else 
			return Optional.of(
					friendsOfFriendsMap
					.values()
					.stream()
					.parallel()
					.filter( f-> name.equals(f.getName()) )
					.collect( Collectors.toList() ).get(0) );
	}
	public Optional<? extends User> searchUserById(String uid) {
		if (friendsMap.containsKey(uid)) {
			Friend friend = friendsMap.get(uid);
			return Optional.of( friend );
		}
		if (friendsOfFriendsMap.containsKey(uid)) {
			FriendOfFriend friendOfFriend = friendsOfFriendsMap.get(uid);
			return Optional.of( friendOfFriend );
		}
		return Optional.empty();
	}
	public Ego searchMe() {
		return myUser;
	}
	public Optional<Ego> searchMe(String uid) {
		if (uid.equals(myUser.getUid()))
			return Optional.of(myUser);
		return Optional.empty();
	}
	
	
	public Set<Float> getAppreciationsRange() {
		return appreciationsRange;
	}
	public Set<Integer> getPostsCountRange() {
		return postsCountRange;
	}
	public Set<Integer> getInteractionsRange() {
		return interactionsRange;
	}
	
	public void setParsingDone(boolean parsingDone) {
		this.parsingDone = parsingDone;
	}
	public boolean isParsingDone() {
		return parsingDone;
	}
}
