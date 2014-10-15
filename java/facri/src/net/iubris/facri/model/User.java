package net.iubris.facri.model;

import java.util.ArrayList;
import java.util.List;

public class User {

	private String id;
//	private int postCounter;
	private List<Post> elsewherePosts = new ArrayList<>();
	private List<Post> ownPosts = new ArrayList<>();

	public String getId() {
		return id;
	}

	public List<Post> getOwnPosts() {
		return ownPosts ;
	}
	
	public int howOwnPosts() {
		return ownPosts.size();
	}

	public List<Post> getElsewhereToPosts() {
		return elsewherePosts;
	}
	
	public int howElsewhereToPosts() {
		return elsewherePosts.size();
	}


}
