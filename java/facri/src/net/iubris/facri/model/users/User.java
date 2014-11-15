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
	
//	public void addToSomeoneElsePost(String targetUserId, Post post);
//	public Map<String, List<Post>> getToSomeoneElsePosts();
	
	public void incrementOwnPostResharing(int shareCount);
	
	public int getOwnPostsResharingCount();

	public Map<String, Interactions> getOtherUsersInteractions();
	
	public Interactions getOtherUserInteractions(String targetUserId);
	
	public int howUserInteracted();
	
	public void incrementOwnPostsLiked(int likesCount);
	
	public int getHowOwnPostsLiked();

	/*private String id;
//	private int postCounter;
//	private Map<String,List<Post>> toSomeoneElsePosts = new HashMap<>();
	final private List<Post> ownPosts = new ArrayList<>();
	final private Map<String,Interactions> interactionsMap = new ConcurrentHashMap<>();
	final private Set<String> mutualFriends = new HashSet<>();
	final private Set<String> friends = new HashSet<>();
	
	private int ownPostsResharingCount;
	private int ownPostsLiking;
	
	public User(String userId) {
		this.id = userId;
	}

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

	public void addToSomeoneElsePost(String targetUserId, Post post) {
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
	}
//	public int howToSomeoneElsePosts(String targetUserId) {
//		return toSomeoneElsePosts.get(targetUserId).size();
//	}

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

	public int howUserInteracted() {
		return interactionsMap.size();
	}
	
	public void incrementOwnPostsLiking(int likesCount) {
		this.ownPostsLiking += likesCount;
	}
	public int getHowOwnPostsLiking() {
		return ownPostsLiking;
	}

	public void addMutualFriend(String friendId) {
		mutualFriends.add(friendId);		
	}
	public Set<String> getMutualFriends(){
		return mutualFriends;
	}
	
	public Set<String> getFriends() {
		return friends;
	}
	public void addFriends(Set<String> friends) {
		this.friends.addAll(friends);
	}*/
}
