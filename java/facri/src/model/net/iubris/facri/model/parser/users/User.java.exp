package net.iubris.facri.model.users;

import java.net.URL;
import java.util.List;
import java.util.Map;

import net.iubris.facri.model.posts.Post;
import net.iubris.facri.model.users.AbstractUser.Sex;

public interface User {
	
	
	public String getName();

	public int getFriendsCount();

//	public int getMutualFriendCount();

	public URL getPicSmallURL();

	public URL getProfileURL();

	public Sex getSex();

	public String getSignificant_other_id();
	
	
	public void setId(String userId);
	public String getUId();
	
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
	
//	public int getOwnPostsLiking();
}
