package net.iubris.facri.model.parser.users;

import java.io.Serializable;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import net.iubris.facri.model.parser.posts.Post;

import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PrimaryKey;

@Persistent
public abstract class AbstractUser implements User, Serializable {

	private static final long serialVersionUID = -1400614535955943141L;
	
	final private Set<Post> ownPosts = 
//			new CopyOnWriteArraySet<>();
			new ConcurrentSkipListSet<>();
	final private Set<Post> ownPostsOnOwnWall = 
//			new CopyOnWriteArraySet<>();
			new ConcurrentSkipListSet<>();
	
	final private Map<String,Interactions> interactionsMap = new ConcurrentHashMap<>();
	
   @PrimaryKey
	protected String uid;
   
	protected String name;
   
	protected int friendsCount;
	
//	protected int mutualFriendsCount;
   
	protected URL picSmall;
   
	protected URL profileURL;
   
	protected Sex sex;
   
	protected String significantOtherId;
	
	protected int ownPostsResharingCount;
	protected int ownPostsLiking;
	
	public AbstractUser(String uid) {
		this.uid = uid;
	}
	public AbstractUser() {}

	@Override
	public String getUid() {
		return uid;
	}
	@Override
	public void setUid(String uid) {
		this.uid = uid;	
	}
	
	@Override
	public boolean addOwnPost(Post post) {
		return ownPosts.add(post);
	}
	@Override
	public Set<Post> getOwnPosts() {
		return ownPosts ;
	}
	@Override
	public int getOwnPostsCount() {
		return ownPosts.size();
	}
	
	@Override
	public boolean addOwnPostOnOwnWall(Post post) {
		return ownPostsOnOwnWall.add(post);
	}
	@Override
	public Set<Post> getOwnPostsOnOwnWall() {
		return ownPostsOnOwnWall;
	}	
	@Override
	public int getOwnPostsOnOwnWallCount() {
		return ownPostsOnOwnWall.size();
	};

	@Override
	public void incrementOwnPostResharing(int reshareCount) {
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
		this.ownPostsLiking += likesCount;
	}
	@Override
	public int getOwnLikedPostsCount() {
		return ownPostsLiking;
	}
	
	public String getName() {
		return name;
	}
	
	public int getFriendsCount() {
		return friendsCount;
	}
	
	public URL getPicSmallURL() {
		return picSmall;
	}
	
	public URL getProfileURL() {
		return profileURL;
	}
	
	public Sex getSex() {
		return sex;
	}
	
	public final String getSignificantOtherId() {
		return significantOtherId;
	}

	public enum Sex {
		male,
		female,
		unknown;
	}
	
	@Override
	public String toString() {
		String toPrint = "uid: "+uid
				+"\nname:"+name
				+"\nsex: "+sex.name()
				+"\nprofile url: "+profileURL
				+"\npic url: "+picSmall
				+"\nsignificant other id: "+significantOtherId;
		return toPrint;
	}
}
