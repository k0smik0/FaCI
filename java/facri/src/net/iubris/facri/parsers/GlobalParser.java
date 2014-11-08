package net.iubris.facri.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.model.User;
import net.iubris.ishtaran.gui._di.annotations.ProgressBarGlobalSize;

public class GlobalParser {

//	private final String feedsDataDir;
	private final String feedsFriendsDataDir;
	private final String feedsMeDataDir;

	private final Map<String, User> useridToUserMap;
	
	private final MutualFriendsParser mutualFriendsParser;
	private final PostsParser postsParser;
	
	private int userCounter;
	
	@ProgressBarGlobalSize
	private int usersTotal;
	private String meIdFileRelativePath;
	private String feedsDirRelativePath;
	

	@Inject
	public GlobalParser(
			@Named("data_root_dir_path") String dataRootDirPath, // "output"
			@Named("feeds_dir_relative_path") String feedsDirRelativePath, // "feeds"
			
			@Named("feeds_me_dir_relative_path") String feedsMeDirRelativePath, // "me"
			@Named("feeds_friends_dir_relative_path") String feedsFriendsDirRelativePath, // "friends"
//			
			@Named("me_id_file") String meIdFileRelativePath, // me_id.txt
			@Named("friends_ids_file") String friendsIdsFileRelativePath, // friends_ids.txt
//			@Named("friends_like_dir_relative_path") String friendsLikesDirRelativePath,
			
			PostsParser postsParser,
			MutualFriendsParser mutualFriendsParser
			) {
		
		this.feedsDirRelativePath = feedsDirRelativePath;
		this.meIdFileRelativePath = meIdFileRelativePath;
		//		this.feedsDataDir = dataRootDirPath+File.separatorChar+feedsDirRelativePath;
		this.feedsMeDataDir = dataRootDirPath+File.separatorChar+feedsMeDirRelativePath;
		this.feedsFriendsDataDir = dataRootDirPath+File.separatorChar+feedsFriendsDirRelativePath;		
		
		this.mutualFriendsParser = mutualFriendsParser;
		this.postsParser = postsParser;
		
		this.useridToUserMap = new ConcurrentHashMap<>();
		
		this.userCounter = 0;
	}
	
	public Map<String, User> getUseridToUserMap() {
		return useridToUserMap;
	}

	public void parse() throws JAXBException, FileNotFoundException, XMLStreamException {
		
		File ownUserDirectory = getDirectories(feedsMeDataDir).get(0);		
		List<File> friendsDirectories = getDirectories(feedsFriendsDataDir);		
		setUsersTotal( friendsDirectories.size()+1 );
		
System.out.println("Parsing my friends feeds:");		
		parse( friendsDirectories );
		
System.out.println("");
		
System.out.println("Parsing my own feeds:");
		incrementUserCounter();
		parse( ownUserDirectory );
	}
	
	private void setUsersTotal(int usersTotal) {
		this.usersTotal = usersTotal;	
	}
	
	private void incrementUserCounter() {
		userCounter++;
	}

	private void parse(List<File> usersDirs) {
		Date start = new Date();
		usersDirs.stream()
		.parallel()
//		.sequential()
		.forEach( new Consumer<File>() {
			@Override
			public void accept(File userDir) {
				incrementUserCounter();
				parse(userDir);
			}
		}); // end stream
		Date end = new Date();
		double finish = (end.getTime()-start.getTime())/1000f;
System.out.println( "parsed "+usersTotal+" users in: "+finish+"s" );
	}
	
	private void parse(File userDir) {
		// userId is Post.sourceId, that is the id of user which wall contains the post
		String owningWallUserId = userDir.getName();
System.out.println("["+userCounter+"/"+usersTotal+"] user "+owningWallUserId+": ");
		
		File[] files = userDir.listFiles();
		if (files.length ==0) {
			incrementUserCounter();
			return;
		}
		mutualFriendsParser.parse(userDir, owningWallUserId, useridToUserMap);
		postsParser.parse(userDir, owningWallUserId, useridToUserMap);
		
	}
	
	private List<File> getDirectories(String dirName) {
		List<File> dataDirs = Arrays.asList( new File(dirName).listFiles() );
		return dataDirs;
	}

}
