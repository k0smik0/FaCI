package net.iubris.facri.parsers;

import java.io.FileNotFoundException;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.model.World;
import net.iubris.facri.parsers.ego.EgoDataParser;
import net.iubris.facri.parsers.friends.FriendsDataParser;

public class GlobalParser {

private final EgoDataParser egoDataParser;
private final FriendsDataParser friendsDataParser;
private final World world;

//	private final String feedsDataDir;
	
//	private final String meIdFileRelativePath;
//	private final String friendsIdsFileRelativePath;
	
//	private final String feedsFriendsDataDir;
//	private final String feedsMeDataDir;

//	private final Map<String, User> useridsToUsersMap;
	
//	private final PostsParser postsParser;
//	private final MyFriendsParser myFriendsParser;
//	private final MutualFriendsParser mutualFriendsParser;
	
//	private int userCounter;
	
//	@ProgressBarGlobalSize
//	private int usersTotal;
	
//	private final World world;
	
	
//	private String feedsDirRelativePath;
	

	@Inject
	public GlobalParser(
			EgoDataParser egoDataParser,
			FriendsDataParser friendsDataParser,
			World world
			
//			@Named("data_root_dir_path") String dataRootDirPath, // "output"
//			@Named("feeds_dir_relative_path") String feedsDirRelativePath, // "feeds"
			
//			@Named("feeds_me_dir_relative_path") String feedsMeDirRelativePath, // "me"
//			@Named("feeds_friends_dir_relative_path") String feedsFriendsDirRelativePath, // "friends"
//			
//			@Named("me_id_file") String meIdFileRelativePath, // me_id.txt
//			@Named("friends_ids_file") String friendsIdsFileRelativePath, // friends_ids.txt
//			@Named("friends_like_dir_relative_path") String friendsLikesDirRelativePath,
			
//			PostsParser postsParser,
//			MutualFriendsParser mutualFriendsParser,
//			MyFriendsParser myFriendsParser,
//			World world
			) {
				this.egoDataParser = egoDataParser
//			@Named("data_root_dir_path") String dataRootDirPath, // "output"
//			@Named("feeds_dir_relative_path") String feedsDirRelativePath, // "feeds"
;
				this.friendsDataParser = friendsDataParser;
				this.world = world;
		
//		this.world = world;
		//		this.feedsDirRelativePath = feedsDirRelativePath;
//		this.meIdFileRelativePath = dataRootDirPath+File.separatorChar+meIdFileRelativePath;
//		this.friendsIdsFileRelativePath = dataRootDirPath+File.separatorChar+friendsIdsFileRelativePath; 
//		this.myFriendsParser = myFriendsParser;
		//		this.feedsDataDir = dataRootDirPath+File.separatorChar+feedsDirRelativePath;
//		this.feedsMeDataDir = dataRootDirPath+File.separatorChar+feedsMeDirRelativePath;
//		this.feedsFriendsDataDir = dataRootDirPath+File.separatorChar+feedsFriendsDirRelativePath;
		
		
//		this.mutualFriendsParser = mutualFriendsParser;
//		this.postsParser = postsParser;
		
//		this.useridsToUsersMap = new ConcurrentHashMap<>();
		
//		this.userCounter = 0;
	}
	
//	public Map<String, User> getUseridToUserMap() {
//		return useridToUserMap;
//	}
	
//	public World getWorld() {
//		return world;
//	}

	public void parse() throws JAXBException, FileNotFoundException, XMLStreamException {
		// parseMyUserData();
		egoDataParser.parse();
System.out.println("");
		friendsDataParser.parse();		
	}
	
	public World getResult() {
		return world;
	}
	
//	private void parseOwn() {
//System.out.println("Parsing my own feeds:");
//		File ownUserDirectory = getDirectories(feedsMeDataDir).get(0);
//		String owningWallUserId = ownUserDirectory.getName();		
//		Ego myUser = new Ego(owningWallUserId);
//		useridsToUsersMap.put(owningWallUserId, myUser);
//
//		if ( checkDir(ownUserDirectory) ) {
//			postsParser.parse(ownUserDirectory/*, owningWallUserId*/, useridsToUsersMap);
//			myFriendsParser.parse(new File(friendsIdsFileRelativePath), null);
//			myUser.addFriends( myFriendsParser.getFriendsIds() );
////			System.out.println("\bok");
//		}
////		System.out.println(myUser.getId());
////		world = new World( myUser );
//		world.setMyUser(myUser);
////		User myUserRemoved = useridsToUsersMap.remove( myUser );
////		System.out.println(myUserRemoved);
//	}
	
	
//	private void parseFriends() {
//		List<File> friendsDirectories = getDirectories(feedsFriendsDataDir);		
//		setUsersTotal( friendsDirectories.size()+1 );
//System.out.println("Parsing my friends feeds:");	
//		parseFriendsDirs( friendsDirectories );
//		
//		Map<String,FriendOrAlike> friends = new ConcurrentHashMap<>();
//		for (String friendId : world.getMyUser().getFriends()) {
//			User person = useridsToUsersMap.remove(friendId);
//			if (person!=null)
//				friends.put(friendId, (FriendOrAlike) person);
//		}
//		world.setMyFriendsMap( friends );
//		
//		Map<String,FriendOrAlike> others = new ConcurrentHashMap<>();
////		Set<Entry<String, User>> entrySet = useridToUserMap.entrySet();
//		for (Entry<String, User> entrySet: useridsToUsersMap.entrySet()) {
//			others.put(entrySet.getKey(), (FriendOrAlike) entrySet.getValue());
//		}
//		world.setOtherUsersMap( others );
//		useridsToUsersMap.clear();
//	}
//	private void parseFriendsDirs(List<File> usersDirs) {
//		Date start = new Date();
//
//		// explicit stream
////		usersDirs.stream()
////		.parallel()
//////		.sequential()
////		.forEach( new Consumer<File>() {
////			@Override
////			public void accept(File userDir) {				
////				String owningWallUserId = userDir.getName();
////				if (checkDir(userDir)) {
////					postsParser.parse(userDir, owningWallUserId, useridToUserMap);
////					mutualFriendsParser.parse(userDir, owningWallUserId, useridToUserMap);
//////					System.out.println("ok");
////				} 
////			}
////		}); // end stream
//		
//		// directory stream
////		Path path = FileSystems.getDefault().getPath( feedsFriendsDataDir);
////		try {
////			DirectoryStream<Path> directoryStream = Files.newDirectoryStream( path );
////			directoryStream.forEach( new Consumer<Path>() {
////				@Override
////				public void accept(Path userDirPath) {
////					File userDir = userDirPath.toFile();
////					String owningWallUserId = userDir.getName();
////					if (checkDir(userDir)) {
////						postsParser.parse(userDir, owningWallUserId, useridToUserMap);
////						mutualFriendsParser.parse(userDir, owningWallUserId, useridToUserMap);
//////						System.out.println("ok");
////					} 
////				}
////			});
////			directoryStream.close();
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
//		
//		// executor
////		Executor parseExecutor = Executors.newFixedThreadPool(1);
////		for (File userDir : usersDirs) {
////			Runnable parseRunnable = new Runnable() {			
////				@Override
////				public void run() {
////					String owningWallUserId = userDir.getName();
////					if (checkDir(userDir)) {
////						postsParser.parse(userDir, owningWallUserId, useridToUserMap);
////						mutualFriendsParser.parse(userDir, owningWallUserId, useridToUserMap);
//////						System.out.println("ok");
////					} else
////						System.out.println("empty");
////				}
////			};
////			parseExecutor.execute(parseRunnable);
////		}
//		
//		// lambda stream
//		usersDirs.stream()
//		.parallel()
//		.filter( s->checkDir(s) )
//		.parallel()
//		.forEach( new Consumer<File>() {
//			@Override
//			public void accept(File userDir) {				
////				String owningWallUserId = userDir.getName();
//				postsParser.parse(userDir, /*owningWallUserId,*/ useridsToUsersMap);
//				mutualFriendsParser.parse(userDir, /*owningWallUserId,*/ useridsToUsersMap);
////					System.out.println("ok");
//			}
//		});
//		
//		
//		Date end = new Date();
//		double finish = (end.getTime()-start.getTime())/1000f;
//System.out.println( "parsed "+usersTotal+" users in: "+finish+"s" );
//	}
	
//	private boolean checkDir(File userDir) {
//		incrementUserCounter();
//		// userId is Post.sourceId, that is the id of user which wall contains the post
////		String owningWallUserId = userDir.getName();
////System.out.print("["+userCounter+"/"+usersTotal+"] user "+owningWallUserId+": ");
//		
//		File[] files = userDir.listFiles();
//		if (files.length==0) {
//			System.out.println("empty");
//			return false;
//		}
//		return true;
//	}
	
//	private void setUsersTotal(int usersTotal) {
//		this.usersTotal = usersTotal;	
//	}	
//	private void incrementUserCounter() {
//		userCounter++;
//	}
	
//	private List<File> getDirectories(String dirName) {
//		List<File> dataDirs = Arrays.asList( new File(dirName).listFiles() );
//		return dataDirs;
//	}
}
