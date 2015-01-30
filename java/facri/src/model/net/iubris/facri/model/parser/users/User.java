package net.iubris.facri.model.parser.users;

import java.util.Map;
import java.util.Set;

import net.iubris.facri.model.parser.posts.Post;

public interface User {
	
	public void setUid(String userId);
	public String getUid();
	
	public boolean addOwnPost(Post post);	
	public Set<Post> getOwnPosts();	
	public int getOwnPostsCount();
	
	public boolean addOwnPostOnOwnWall(Post post);
	public Set<Post> getOwnPostsOnOwnWall();	
	public int getOwnPostsOnOwnWallCount();
	
	public void incrementOwnPostResharing(int shareCount);	
	public int getOwnPostsResharingCount();

	public Map<String, Interactions> getToOtherUsersInteractions();	
	public Interactions getToOtherUserInteractions(String targetUserId);
	public int getToOtherUserInteractionsCount(String targetUserId);
	
	public int getUserInteractionsCount();
	
	public void incrementOwnLikedPosts(int likesCount);	
	public int getOwnLikedPostsCount();
	
	public int getFriendsCount();
}
