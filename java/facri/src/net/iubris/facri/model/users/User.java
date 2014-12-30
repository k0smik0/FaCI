package net.iubris.facri.model.users;

import java.util.List;
import java.util.Map;

import net.iubris.facri.model.posts.Post;

public interface User {
	
	public void setUid(String userId);
	public String getUid();
	
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
