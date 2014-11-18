package net.iubris.facri.model.users;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.iubris.facri.model.posts.Post;

public abstract class AbstractUser implements User {

	private String id;
//	private int postCounter;
//	private Map<String,List<Post>> toSomeoneElsePosts = new HashMap<>();
	final private List<Post> ownPosts = new CopyOnWriteArrayList<>();
	final private Map<String,Interactions> interactionsMap = new ConcurrentHashMap<>();
//	final private Set<String> mutualFriends = new HashSet<>();
//	final private Set<String> friends = new HashSet<>();
	
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
//	public int howToSomeoneElsePosts(String targetUserId) {
//		return toSomeoneElsePosts.get(targetUserId).size();
//	}

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

//	public void addMutualFriend(String friendId) {
//		mutualFriends.add(friendId);		
//	}
//	public Set<String> getMutualFriends(){
//		return mutualFriends;
//	}
	
//	public Set<String> getFriends() {
//		return friends;
//	}
//	public void addFriends(Set<String> friends) {
//		this.friends.addAll(friends);
//	}
}
