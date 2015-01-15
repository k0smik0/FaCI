package net.iubris.facri.model.users;

import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.iubris.facri.model.posts.Post;

import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PrimaryKey;

@Persistent
public abstract class AbstractUser implements User,Serializable {

	private static final long serialVersionUID = -1400614535955943141L;
	
	final private List<Post> ownPosts = new CopyOnWriteArrayList<>();
	final private Map<String,Interactions> interactionsMap = new ConcurrentHashMap<>();
	
//   @MapToColumn(column=0,type=String.class)
   @PrimaryKey
	protected String uid;
   
//   @MapToColumn(column=1,type=String.class)
	protected String name;
   
//   @MapToColumn(column=2,type=Integer.class)
	protected int friendsCount;
	
//	protected int mutualFriendsCount;
   
//	@MapToColumn(column=4,type=URL.class)
	protected URL picSmall;
   
//	@MapToColumn(column=5,type=URL.class)
	protected URL profileURL;
   
//	@MapToColumn(column=6,type=Sex.class)
	protected Sex sex;
   
//	@MapToColumn(column=7,type=String.class)
	protected String significantOtherId;

	
	protected int ownPostsResharingCount;
	protected int ownPostsLiking;
	
	public AbstractUser(String uid) {
		this.uid = uid;
	}
	public AbstractUser() {}

	@Override
	public String getUid() {
//		System.out.println(uid);
		return uid;
	}
	@Override
	public void setUid(String uid) {
//		System.out.println(uid);
		this.uid = uid;	
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
//System.out.println(reshareCount);
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
	
	public String getName() {
		return name;
	}
//	public void setName(String name) {
//		this.name = name;
//	}
	
	public int getFriendsCount() {
		return friendsCount;
	}
	
	public URL getPicSmallURL() {
		return picSmall;
	}
//	public void setPicSmall(String picSmall) throws MalformedURLException  {
//		this.picSmall = new URL(picSmall);
//	}
//	public void setPicSmall(URL picSmall)  {
//		this.picSmall = new URL(picSmall);
//	}
	
	public URL getProfileURL() {
		return profileURL;
	}
//	public void setProfilURL(String profileURL) throws MalformedURLException {
//		this.profileURL = new URL(profileURL);
//	}
//	public void setProfileURL(URL profileURL) {
//		this.profileURL = profileURL;
//	}
	
	public Sex getSex() {
		return sex;
	}
//	public void setSex(String sex) {
////		String real = sex.split("\n")[0];
////		System.out.print(sex);
////		System.out.println(sex+" o");
//		this.sex = Sex.valueOf(Sex.class, sex);
////		System.out.println( Sex.valueOf(Sex.class, sex.split("\n")[1]) );
//	}
//	public void setSex(Sex sex) {
//		this.sex = sex;
//	}
	
	public final String getSignificantOtherId() {
		return significantOtherId;
	}
//	public final void setSignificantOtherId(String significantOtherId) {
//		this.significantOtherId = significantOtherId;
//	}

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
