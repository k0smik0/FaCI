/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (User.java) is part of facri.
 * 
 *     User.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     User.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.model.world.users;

import java.util.Map;
import java.util.Set;

import net.iubris.faci.parser.model.posts.Post;

public interface User {
	
	public void setUid(String userId);
	public String getUid();
	
	public boolean addOwnPost(Post post);	
	public Set<Post> getOwnPosts();	
	public int getOwnPostsCount();
	
	public boolean addOwnPostOnOwnWall(Post post);
	public Set<Post> getOwnPostsOnOwnWall();	
	public int getOwnPostsOnOwnWallCount();
	
	public void incrementOwnPostResharing(int shareCount);	
	public int getOwnPostsResharingCount();

	public Map<String,Interactions> getToOtherUsersInteractions();	
	public Interactions getToOtherUserInteractions(String targetUserId);
	public int getToOtherUserInteractionsCount(String targetUserId);
	
	public int getToOtherUsersInteractionsCount();
	
	public void incrementOwnLikedPosts(int likesCount);	
	public int getOwnLikedPostsCount();
	
	public int getFriendsCount();
	
	public enum Sex {
		male,
		female,
		unknown;
	}
}
