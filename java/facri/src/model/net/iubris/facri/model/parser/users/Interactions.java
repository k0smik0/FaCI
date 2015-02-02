/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (Interactions.java) is part of facri.
 * 
 *     Interactions.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     Interactions.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.model.parser.users;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.sleepycat.persist.model.Persistent;

import net.iubris.facri.model.parser.posts.Post;

@Persistent
public class Interactions implements Serializable {

	private static final long serialVersionUID = -2972638634494988995L;

	private int tags;
	private int likes;
	private final List<Post> posts = new CopyOnWriteArrayList<>();
	private int comments;

	public void incrementTags() {
		tags++;
	}
	public int getTagsCount() {
		return tags;
	}

	public void incrementLikes() {
		likes++;
//System.out.println(likes);
	}
	public int getLikesCount() {
		return likes;
	}
	
	public void addPost(Post post) {
		this.posts.add(post);
//	System.out.println(posts.size());
	}
	public int getPostsCount() {
		return posts.size();
	}

	public void incrementComments() {
		this.comments++;
//System.out.println(comments);
	}
	public int getCommentsCount() {
		return comments;
	}
	
	public int getTotalInteractions() {
		return tags+likes+posts.size()+comments;
	}
}
