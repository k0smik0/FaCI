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
package net.iubris.faci.model.world.users;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.iubris.faci.parser.model.comments.CommentData;
import net.iubris.faci.parser.model.posts.Post;

import com.sleepycat.persist.model.Persistent;

@Persistent
public class Interactions implements Serializable {

	private static final long serialVersionUID = -2972638634494988995L;

	private final List<Post> posts = new CopyOnWriteArrayList<>();
	private final List<CommentData> commentDataList = new CopyOnWriteArrayList<>(); 
	private int tags;
	private int likes;

	public void incrementTags() {
		tags++;
	}
	public int getTagsCount() {
		return tags;
	}

	public void incrementLikes() {
		likes++;
	}
	public int getLikesCount() {
		return likes;
	}
	
	public void addPost(Post post) {
		this.posts.add(post);
	}
	public List<Post> getPosts() {
		return posts;
	}
	public int getPostsCount() {
		return posts.size();
	}

	public void addCommentData(CommentData commentData) {
		commentDataList.add(commentData);
	}
	public int getCommentsCount() {
		return commentDataList.size();
	}
	
	public int getTotalInteractions() {
		return tags+likes+posts.size()+getCommentsCount();
	}
	
}
