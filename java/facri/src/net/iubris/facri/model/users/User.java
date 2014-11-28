package net.iubris.facri.model.users;

import java.util.List;
import java.util.Map;

import net.iubris.facri.model.posts.Post;

public interface User {
	
	public void setId(String userId);
	public String getId();
	
	public boolean addOwnPost(Post post);	
	public List<Post> getOwnPosts();	
	public int getOwnPostsCount();
	
	public void incrementOwnPostResharing(int shareCount);	
	public int getOwnPostsResharingCount();

	public Map<String, Interactions> getToOtherUsersInteractions();	
	public Interactions getToOtherUserInteractions(String targetUserId);
	public int getToOtherUserInteractionsCount(String targetUserId);
	
	public int getUserInteractionsCount();
	
	public void incrementOwnLikedPosts(int likesCount);	
	public int getOwnLikedPostsCount();	
}
