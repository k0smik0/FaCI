package net.iubris.facri.model.users;

import java.util.List;
import java.util.Map;

import net.iubris.facri.model.posts.Post;


public interface User {
	
	public void setId(String userId);
	public String getId();
	
	public boolean addOwnPost(Post post);	
	public List<Post> getOwnPosts();	
	public int howOwnPosts();
	
	public void incrementOwnPostResharing(int shareCount);	
	public int getOwnPostsResharingCount();

	public Map<String, Interactions> getOtherUsersInteractions();	
	public Interactions getOtherUserInteractions(String targetUserId);
	
	public int howUserInteracted();
	
	public void incrementOwnPostsLiked(int likesCount);	
	public int getHowOwnPostsLiked();
}
