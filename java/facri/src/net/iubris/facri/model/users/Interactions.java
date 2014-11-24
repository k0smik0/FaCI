package net.iubris.facri.model.users;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.iubris.facri.model.posts.Post;

public class Interactions implements Serializable {

	private static final long serialVersionUID = -2972638634494988995L;

	private int tags;
	private int likes;
	private final List<Post> posts = new CopyOnWriteArrayList<>();
	private int comments;

	public void incrementTags() {
		tags++;		
	}
	public int getHowTags() {
		return tags;
	}

	public void incrementLikes() {
		likes++;		
	}
	public int getHowLikes() {
		return likes;
	}
	
	public void addPost(Post post) {
		this.posts.add(post);
	}

	public void incrementComments() {
		this.comments++;
	}
	public int getHowComments() {
		return comments;
	}

}
