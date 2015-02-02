/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (Ego.java) is part of facri.
 * 
 *     Ego.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     Ego.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
