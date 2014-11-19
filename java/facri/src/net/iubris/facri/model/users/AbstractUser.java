package net.iubris.facri.model.users;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.iubris.facri.model.posts.Post;

public abstract class AbstractUser implements User {

	private String id;
	final private List<Post> ownPosts = new CopyOnWriteArrayList<>();
	final private Map<String,Interactions> interactionsMap = new ConcurrentHashMap<>();
	
	private int ownPostsResharingCount;
	private int ownPostsLiking;
	
	public AbstractUser(String userId) {
		this.id = userId;
	}

	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String userId) {
		this.id = userId;		
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

	public void incrementOwnPostResharing(int shareCount) {
		this.ownPostsResharingCount += shareCount;		
	}
	public int getOwnPostsResharingCount() {
		return ownPostsResharingCount;
	}

	public Map<String, Interactions> getOtherUsersInteractions() {
		return interactionsMap;
	}
	
	public Interactions getOtherUserInteractions(String targetUserId) {
		if (!interactionsMap.containsKey(targetUserId)) {
			Interactions interactions = new Interactions();
			interactionsMap.put(targetUserId, interactions);
			return interactions;
		}
		return interactionsMap.get(targetUserId);
	}

	public int howUserInteracted() {
		return interactionsMap.size();
	}
	
	public void incrementOwnPostsLiked(int likesCount) {
		this.ownPostsLiking += likesCount;
	}
	public int getHowOwnPostsLiked() {
		return ownPostsLiking;
	}
}
