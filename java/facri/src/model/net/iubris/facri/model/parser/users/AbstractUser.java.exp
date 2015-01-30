package net.iubris.facri.model.users;

import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.iubris.facri.model.posts.Post;

import com.googlecode.jcsv.annotations.MapToColumn;

public abstract class AbstractUser implements User,Serializable {

	protected static final long serialVersionUID = -1400614535955943141L;
	
//	@MapToColumn(column=1)
//	protected String uid;
	
	protected final List<Post> ownPosts = new CopyOnWriteArrayList<>();
	protected final Map<String,Interactions> interactionsMap = new ConcurrentHashMap<>();
	
	protected int ownPostsResharingCount;
	protected int ownPostsLiked;
	
	public AbstractUser(String userId) {
		this.uid = userId;
	}
	public AbstractUser() {}

	@Override
	public String getUId() {
		return uid;
	}
	@Override
	public void setId(String userId) {
		this.uid = userId;		
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
		this.ownPostsLiked += likesCount;
	}
	@Override
	public int getOwnLikedPostsCount() {
		return ownPostsLiked;
	}
	
	@MapToColumn(column=0)
	protected String uid;
	
	@MapToColumn(column=1)
	protected String name;
	
	@MapToColumn(column=2)
	protected int friendsCount;

//	@MapToColumn(column=3)
//	protected int mutualFriendCount;
	
	@MapToColumn(column=4)
	protected URL picSmallURL;
	
	@MapToColumn(column=5)
	protected URL profileURL;
	
	@MapToColumn(column=6)
	protected Sex sex;
	
	@MapToColumn(column=7)
	protected String significant_other_id;
	
	public String getName() {
		return name;
	}

//	public int getFriendsCount() {
//		return friendsCount;
//	}

//	public int getMutualFriendCount() {
//		return mutualFriendCount;
//	}

	public URL getPicSmallURL() {
		return picSmallURL;
	}

	public URL getProfileURL() {
		return profileURL;
	}

	public Sex getSex() {
		return sex;
	}

	public String getSignificant_other_id() {
		return significant_other_id;
	}

	public enum Sex {
		male,
		female;
	}
}
