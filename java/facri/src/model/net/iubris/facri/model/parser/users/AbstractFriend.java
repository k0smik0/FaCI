/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FriendOrAlike.java) is part of facri.
 * 
 *     FriendOrAlike.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FriendOrAlike.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.model.parser.users;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sleepycat.persist.model.Persistent;

@Persistent
public abstract class AbstractFriend extends AbstractUser {

	private static final long serialVersionUID = 6534268317862330404L;

	protected final Set<String> mutualFriendsIds = new HashSet<>();
	protected final Set<String> friendsOfFriendsIds = new HashSet<>();
	
	protected int mutualFriendsCount;

	public AbstractFriend() {}
	
	public AbstractFriend(String uid) {
		super(uid);
	}
	
	public AbstractFriend(String uid, String name, int friendsCount, int mutualFriendsCount, URL picSmall, URL profileURL, User.Sex sex, String significantOtherId) {
		this.uid = uid;
		this.name = name;
		this.friendsCount = friendsCount;
		this.mutualFriendsCount = mutualFriendsCount;
		this.picSmall = picSmall;
		this.profileURL = profileURL;
		this.sex = sex;
		this.significantOtherId = significantOtherId;
	}

	public void addMutualFriendId(String friendId) {
		mutualFriendsIds.add(friendId);		
	}
	public void addMutualFriendsIds(Collection<String> friendsIds) {
		mutualFriendsIds.addAll(friendsIds);		
	}
	public Set<String> getMutualFriendsIds(){
		return mutualFriendsIds;
	}
	public boolean isMutualFriend(String userId) {
		return mutualFriendsIds.contains(userId);
	}
	public int getMutualFriendsCount() {
		return mutualFriendsCount;
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
	public boolean isFriendOfFriend(String userId) {
		return friendsOfFriendsIds.contains(userId);
	}
	
	@Override
	public String toString() {
		return "---\n"+super.toString()
				+"\nfriends count: "+friendsCount
				+"\nmutual friends count: "+mutualFriendsCount+"|"+mutualFriendsIds.size()
				+"\n---";
	}

	public void addFriendsOfFriends(List<String> friendsOfFriendsIds) {
		this.friendsOfFriendsIds.addAll(friendsOfFriendsIds);		
	}
}
