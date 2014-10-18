package net.iubris.facri.model;

import java.util.ArrayList;
import java.util.List;

public class Interactions {

	private int tags;
	private int likes;
	private List<Post> posts = new ArrayList<>();
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
