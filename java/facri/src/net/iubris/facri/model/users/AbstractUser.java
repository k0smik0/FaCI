package net.iubris.facri.model.users;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.iubris.facri.model.posts.Post;

public abstract class AbstractUser implements User,Serializable {

	private static final long serialVersionUID = -1400614535955943141L;
	private String id;
	final private List<Post> ownPosts = new CopyOnWriteArrayList<>();
	final private Map<String,Interactions> interactionsMap = new ConcurrentHashMap<>();
	
	private int ownPostsResharingCount;
	private int ownPostsLiking;
	
	public AbstractUser(String userId) {
		this.id = userId;
	}

	@Override
	public String getId() {
		return id;
	}	
	@Override
	public void setId(String userId) {
		this.id = userId;		
	}
	
	@Override
	public boolean addOwnPost(Post post) {
		return ownPosts.add(post);
	}
	@Override
	public List<Post> getOwnPosts() {
		return ownPosts ;
	}
	@Override
	public int getOwnPostsCount() {
		return ownPosts.size();
	}

	@Override
	public void incrementOwnPostResharing(int reshareCount) {
System.out.println(reshareCount);
		this.ownPostsResharingCount += reshareCount;		
	}
	@Override
	public int getOwnPostsResharingCount() {
		return ownPostsResharingCount;
	}

	@Override
	public Map<String, Interactions> getToOtherUsersInteractions() {
		return interactionsMap;
	}	
	@Override
	public Interactions getToOtherUserInteractions(String targetUserId) {
		if (!interactionsMap.containsKey(targetUserId)) {
			Interactions interactions = new Interactions();
			interactionsMap.put(targetUserId, interactions);
			return interactions;
		}
		return interactionsMap.get(targetUserId);
	}
	
	@Override
	public int getToOtherUserInteractionsCount(String targetUserId) {
		return getToOtherUserInteractions(targetUserId).getTotalInteractions();
	}

	@Override
	public int getUserInteractionsCount() {
		return interactionsMap.size();
	}
	
	@Override
	public void incrementOwnLikedPosts(int likesCount) {
//System.out.println(likesCount);
		this.ownPostsLiking += likesCount;
	}
	@Override
	public int getOwnLikedPostsCount() {
		return ownPostsLiking;
	}
}
