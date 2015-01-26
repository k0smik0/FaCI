package net.iubris.facri.model.world;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.BiConsumer;

import javax.inject.Singleton;

import net.iubris.facri.grapher.generators.interactions.InteractionsWeigths;
import net.iubris.facri.model.posts.Post;
import net.iubris.facri.model.users.Ego;
import net.iubris.facri.model.users.FriendOrAlike;
import net.iubris.facri.model.users.User;
import net.iubris.facri.utils.Printer;

import com.sleepycat.persist.model.Entity;

@Singleton
@Entity
public class World implements Serializable {

	private static final long serialVersionUID = -7112902990753202438L;
	
	private final Map<String,FriendOrAlike> myFriendsMap= new ConcurrentHashMap<>();
	private final Map<String,FriendOrAlike> otherUsersMap = new ConcurrentHashMap<>();
	
	private final Set<Integer> appreciationsRange = new ConcurrentSkipListSet<Integer>();
	private final Set<Integer> postsCountRange = new ConcurrentSkipListSet<Integer>();
	private final Set<Integer> interactionsRange = new ConcurrentSkipListSet<Integer>();
	
	private Ego myUser;

	private boolean parsingDone = false;
	
	public Ego getMyUser() {
		return myUser;
	}
	public void setMyUser(Ego myUser) {
		this.myUser = myUser;
	}
	
	public void populateMyFriendsMap(Map<String, FriendOrAlike> myFriendsMap) {
		this.myFriendsMap.putAll(myFriendsMap);
	}
	public Map<String, FriendOrAlike> getMyFriendsMap() {
		return myFriendsMap;
	}
	
	public void populateOtherUsersMap(Map<String, FriendOrAlike> otherUsersMap) {
		this.otherUsersMap.putAll(otherUsersMap);
	}
	public Map<String, FriendOrAlike> getOtherUsersMap() {
		return otherUsersMap;
	}
	
	/**
	 * try to find an existant {@link User} from userId: if it not exists, a new one 
	 * is created, and its instance will be returned;
	 * obviously, if the user is "my user", a new {@link Ego} instance is created, 
	 * as it is a FriendOrAlike (and if it is a friend, it will be added to "friends" list)
	 * @param userId
	 * @return
	 */
	public User isExistentUserOrCreateNew(String userId) {
		if (myUser.getUid().equals(userId))
			return myUser;
		
		FriendOrAlike user = null;
		if (myUser.isMyFriendById(userId)) {
			if (myFriendsMap.containsKey(userId)) {
				user = myFriendsMap.get(userId);
			} else {
				user = new FriendOrAlike(userId);
				myFriendsMap.put(userId, user);
			}
		} else {
			if (otherUsersMap.containsKey(userId)) {
				user = otherUsersMap.get(userId);
			} else {
				user = new FriendOrAlike(userId);
				otherUsersMap.put(userId, user);
			}
		}
		
		int updatedOwnPostsCount = user.getOwnPostsCount();
		postsCountRange.add(updatedOwnPostsCount);
		
		int updatedAppreciation = user.getOwnLikedPostsCount() + InteractionsWeigths.RESHARED_OWN_POST*user.getOwnPostsResharingCount();
		appreciationsRange.add(updatedAppreciation);
		
		int updatedInteractions = user.getUserInteractionsCount();
		interactionsRange.add(updatedInteractions);
		
		return user;
	}
	
	public Optional<FriendOrAlike> searchUserByName(String name) {
		Optional<FriendOrAlike> friend = myFriendsMap.values().parallelStream().filter(f->f.getName().equals(name)).findFirst();
		if (friend.isPresent())
			return friend;
		else 
			return otherUsersMap.values().parallelStream().filter(f->f.getName().equals(name)).findFirst();
	}
	public Optional<FriendOrAlike> searchUserById(String uid) {
		if (myFriendsMap.containsKey(uid)) {
			FriendOrAlike friendOrAlike = myFriendsMap.get(uid);
//			System.out.println(friendOrAlike);
			return Optional.of( friendOrAlike );
		}
		if (otherUsersMap.containsKey(uid)) {
			FriendOrAlike friendOrAlike = otherUsersMap.get(uid);
//			System.out.println(friendOrAlike);
			return Optional.of( friendOrAlike );
		}
		return Optional.empty();
	}
	public Ego searchMe() {
		return myUser;
	}
	public Optional<Ego> searchMe(String uid) {
		if (uid.equals(myUser.getUid()))
			return Optional.of(myUser);
		return Optional.empty();
	}
	
	
	public Set<Integer> getAppreciationsRange() {
		return appreciationsRange;
	}
	public Set<Integer> getPostsCountRange() {
		return postsCountRange;
	}
	public Set<Integer> getInteractionsRange() {
		return interactionsRange;
	}
	
	public void setParsingDone(boolean parsingDone) {
		this.parsingDone = parsingDone;
	}
	public boolean isParsingDone() {
		return parsingDone;
	};
	
	public void testData() {
		DataTester dataTester = new DataTester(this);
		dataTester.test();
	}
	
	
}

class DataTester {
//	private int friendsCounter;
//	private int friendsoffriendsCounter;
	
	private final World world;
	
	private int userCounter = 0;

	private final Ego ego;
	
	DataTester(World world) {
		this.world = world;
		ego = world.getMyUser();
	}
	
	public void test() {
		System.out.println( ego.getUid()+":\n"
				+"\tfriends: "+ego.getFriendsCount()+"|"+ego.getFriendsIds().size()
				);
		printSomeUserInformations(ego);
		System.out.println("");
		
		world.getMyFriendsMap()
			.forEach( friendoralikeConsumer );
		System.out.println("");
		
//		resetCounter();
//		world.getOtherUsersMap()
//			.forEach( friendoralikeConsumer );
	}
	
	void resetCounter() {
		userCounter = 0;
	}
	
	private final BiConsumer<String, FriendOrAlike> friendoralikeConsumer = new BiConsumer<String, FriendOrAlike>() {
//		private int userCounter = 0;
		@Override
		public void accept(String t, FriendOrAlike foa) {
//			if (foa.getMutualFriends().size() >0) {
				String uid = foa.getUid();
				Printer.println( "["+userCounter+"] "+uid+( ego.isMyFriendById(uid)? " (friend): ":" (friend of friend):") 
					+"\n\tmutual friends: "+foa.getMutualFriends().size()
					);
				printSomeUserInformations(foa);
				userCounter++;
//			}
		}
	};
	
	private void printSomeUserInformations(User user) {
		Set<Post> posts = user.getOwnPosts();
		Comparator<Post> postTimeComparator = new Comparator<Post>() {
			@Override
			public int compare(Post o1, Post o2) {
				return (o1.getCreatedTime().compareTo(o2.getCreatedTime()));
			}
		};
		Optional<Post> eagerPost = posts.stream().parallel().min(postTimeComparator);
		Optional<Post> youngerPost = posts.stream().parallel().max(postTimeComparator);
		Printer.println(
				"\tposts: "+user.getOwnPostsCount()+"/"+posts.size()
				);
		Printer.print("\t\teager: ");
				eagerPost.ifPresent(	 c->Printer.print(c.getCreatedTime()) );
		Printer.print("\n\t\tyounger: ");
				youngerPost.ifPresent(c->Printer.print(c.getCreatedTime()) );
		Printer.println(
				 "\n\t\treceived likes: "+user.getOwnLikedPostsCount()
				+"\n\t\treshared: "+user.getOwnPostsResharingCount()
				
				+"\n\tinteractions: "+user.getUserInteractionsCount()
				);
	}
}
