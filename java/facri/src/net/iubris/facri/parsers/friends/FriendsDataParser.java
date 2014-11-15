package net.iubris.facri.parsers.friends;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Named;

import net.iubris.facri.parsers.posts.PostsParser;
import net.iubris.facri.parsers.utils.ParsingUtils;
import net.iubris.ishtaran.gui._di.annotations.ProgressBarGlobalSize;

public class FriendsDataParser {
	
	private final String feedsFriendsDataDir;
	
	private final PostsParser postsParser;
	private final MutualFriendsParser mutualFriendsParser;
	
//	private final World world;
	
	
	@ProgressBarGlobalSize
	private int usersTotal;
	
	private int userCounter;
	
	@Inject
	public FriendsDataParser(PostsParser postsParser, MutualFriendsParser mutualFriendsParser,
			@Named("data_root_dir_path") String dataRootDirPath, // "output"
			@Named("feeds_friends_dir_relative_path") String feedsFriendsDirRelativePath // "friends"
//			,@Named("friends_like_dir_relative_path") String friendsLikesDirRelativePath
//			, World world
			) {
		this.postsParser = postsParser;
		this.mutualFriendsParser = mutualFriendsParser;
//		this.world = world;
		
		this.feedsFriendsDataDir = dataRootDirPath+File.separatorChar+feedsFriendsDirRelativePath;
	}
	
	
	public void parse() {
		List<File> friendsDirectories = ParsingUtils.getDirectories(feedsFriendsDataDir);		
		setUsersTotal( friendsDirectories.size() );
System.out.println("Parsing my friends feeds:");	
		parseFriendsDirs( friendsDirectories );
		
//		Map<String,FriendOrAlike> friends = new ConcurrentHashMap<>();
//		for (String friendId : world.getMyUser().getFriendsIds()) {
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
	}
	private void parseFriendsDirs(List<File> usersDirs) {
		Date start = new Date();

		// explicit stream
//		usersDirs.stream()
//		.parallel()
////		.sequential()
//		.forEach( new Consumer<File>() {
//			@Override
//			public void accept(File userDir) {				
//				String owningWallUserId = userDir.getName();
//				if (checkDir(userDir)) {
//					postsParser.parse(userDir, owningWallUserId, useridToUserMap);
//					mutualFriendsParser.parse(userDir, owningWallUserId, useridToUserMap);
////					System.out.println("ok");
//				} 
//			}
//		}); // end stream
		
		// directory stream
//		Path path = FileSystems.getDefault().getPath( feedsFriendsDataDir);
//		try {
//			DirectoryStream<Path> directoryStream = Files.newDirectoryStream( path );
//			directoryStream.forEach( new Consumer<Path>() {
//				@Override
//				public void accept(Path userDirPath) {
//					File userDir = userDirPath.toFile();
//					String owningWallUserId = userDir.getName();
//					if (checkDir(userDir)) {
//						postsParser.parse(userDir, owningWallUserId, useridToUserMap);
//						mutualFriendsParser.parse(userDir, owningWallUserId, useridToUserMap);
////						System.out.println("ok");
//					} 
//				}
//			});
//			directoryStream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		// executor
//		Executor parseExecutor = Executors.newFixedThreadPool(1);
//		for (File userDir : usersDirs) {
//			Runnable parseRunnable = new Runnable() {			
//				@Override
//				public void run() {
//					String owningWallUserId = userDir.getName();
//					if (checkDir(userDir)) {
//						postsParser.parse(userDir, owningWallUserId, useridToUserMap);
//						mutualFriendsParser.parse(userDir, owningWallUserId, useridToUserMap);
////						System.out.println("ok");
//					} else
//						System.out.println("empty");
//				}
//			};
//			parseExecutor.execute(parseRunnable);
//		}
		
		// lambda stream
		usersDirs.stream()
//		.parallel()
		.sequential()
		.peek( 
				/*new Consumer<File>() {
			@Override
			public void accept(File userDir) {
				incrementUserCounter();
System.out.println("["+userCounter+"/"+usersTotal+"] user "+userDir.getName()+": ");
			}}*/
			s->System.out.print("["+ incrementUserCounter() +"/"+usersTotal+"] user "+s.getName()+": ")
		)
		.filter( s->s.listFiles().length>0 /*checkDir(s)*/ )
		.parallel()
		.forEach( new Consumer<File>() {
			@Override
			public void accept(File userDir) {				
//				String owningWallUserId = userDir.getName();
				postsParser.parse(userDir/*owningWallUserId,*/ /*useridsToUsersMap*/);
				mutualFriendsParser.parse(userDir/*, owningWallUserId, useridsToUsersMap*/);
System.out.println("ok");
//				System.out.println("");
			}
		});
		
		
		Date end = new Date();
		double finish = (end.getTime()-start.getTime())/1000f;
System.out.println( "parsed "+usersTotal+" users in: "+finish+"s" );
	}
	
	private void setUsersTotal(int usersTotal) {
		this.usersTotal = usersTotal;	
	}	
	private int incrementUserCounter() {
		return ++userCounter;
	}

}
