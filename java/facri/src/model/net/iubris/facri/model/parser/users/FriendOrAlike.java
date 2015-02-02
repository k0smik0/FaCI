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
import java.util.Set;

import com.sleepycat.persist.model.Entity;

@Entity
public class FriendOrAlike extends AbstractUser {

	private static final long serialVersionUID = 6593382542268675220L;

	final private Set<String> mutualFriends = new HashSet<>();
	
	private int mutualFriendsCount;
	
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
	
//	public void setMutualFriendsCount(int mutualFriendsCount) {
//		this.mutualFriendsCount = mutualFriendsCount;
//	}
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
