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
