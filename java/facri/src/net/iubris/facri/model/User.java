package net.iubris.facri.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

	private String id;
//	private int postCounter;
	private Map<String,List<Post>> toSomeoneElsePosts = new HashMap<>();
	private List<Post> ownPosts = new ArrayList<>();
	private int ownPostsResharingCount;
	private Map<String,Interactions> interactionsMap;
	private int ownPostsLiking;

	public String getId() {
		return id;
	}

	public boolean addOwnPost(Post post) {
		return ownPosts.add(post);
	}
	public List<Post> getOwnPosts() {
		return ownPosts ;
	}	
	public int howOwnPosts() {
		return ownPosts.size();
	}

	/*public void addToSomeoneElsePost(String targetUserId, Post post) {
		if (toSomeoneElsePosts.containsKey(targetUserId)) {
			toSomeoneElsePosts.get(targetUserId).add(post);
		} else {
			List<Post> posts = new ArrayList<>();
			posts.add(post);
			toSomeoneElsePosts.put(targetUserId, posts);
		}
	}
	public Map<String, List<Post>> getToSomeoneElsePosts() {
		return toSomeoneElsePosts;
	}*/	
	public int howToSomeoneElsePosts(String targetUserId) {
		return toSomeoneElsePosts.get(targetUserId).size();
	}

	public void incrementOwnPostResharing(int shareCount) {
		this.ownPostsResharingCount += shareCount;		
	}
	public int getOwnPostsResharingCount() {
		return ownPostsResharingCount;
	}

	public Map<String, Interactions> getOtherUsersMapInteractions() {
		return interactionsMap;
	}
	
	public Interactions getOtherUserMapInteractions(String targetUserId) {
		if (!interactionsMap.containsKey(targetUserId)) {
			Interactions interactions = new Interactions();
			interactionsMap.put(targetUserId, interactions);
			return interactions;
		}
		return interactionsMap.get(targetUserId);
	}

	public void incrementOwnPostsLiking(int likesCount) {
		this.ownPostsLiking += likesCount;
	}
	public int getHowOwnPostsLiking() {
		return ownPostsLiking;
	}

}
