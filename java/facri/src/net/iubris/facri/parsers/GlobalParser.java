package net.iubris.facri.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.model.User;
import net.iubris.ishtaran.gui._di.annotations.ProgressBarGlobalSize;

public class GlobalParser {

	private final String feedsDataDir;
	private final String feedsFriendsDataDir;
	private final String feedsMeDataDir;

	private final Map<String, User> useridToUserMap;
	
	private final PostsParser postsParser;
	
	private int userCounter;
	
	@ProgressBarGlobalSize
	private int usersTotal;

	@Inject
	public GlobalParser(
			@Named("data_root_dir_path") String dataRootDirPath,
			@Named("feeds_dir_relative_path") String feedsDirRelativePath,
			
			@Named("feeds_me_dir_relative_path") String feedsMeDirRelativePath,
			@Named("feeds_friends_dir_relative_path") String feedsFriendsDirRelativePath,
//			
			@Named("friends_ids_file") String friendsIdsFileRelativePath,
//			@Named("friends_like_dir_relative_path") String friendsLikesDirRelativePath,
			
			PostsParser postsParser
			) {
		
		
		this.feedsDataDir = dataRootDirPath+File.separatorChar+feedsDirRelativePath;
		this.feedsMeDataDir = feedsDataDir+File.separatorChar+feedsMeDirRelativePath;
		this.feedsFriendsDataDir = feedsDataDir+File.separatorChar+feedsFriendsDirRelativePath;		
				
		this.postsParser = postsParser;
		
		this.useridToUserMap = new ConcurrentHashMap<>();
		
		this.userCounter = 0;
	}
	
	public Map<String, User> getUseridToUserMap() {
		return useridToUserMap;
	}

	public void parse() throws JAXBException, FileNotFoundException, XMLStreamException {
		
		List<File> friendsDirectories = getDirectories(feedsFriendsDataDir);
		File ownUserDirectory = getDirectories(feedsMeDataDir).get(0);
		setUsersTotal((friendsDirectories.size())+1);
		
System.out.println("Parsing my friends feeds:");		
		parse( friendsDirectories );
		
System.out.println("");
		
System.out.println("Parsing my own feeds:");
		parse( ownUserDirectory );
	}
	
	private void setUsersTotal(int usersTotal) {
		this.usersTotal = usersTotal;	
	}
	
	private void incrementUserCounter() {
		userCounter++;
	}

	protected void parse(List<File> usersDirs) {
		Date start = new Date();
//System.out.println("acting on "+usersTotal);
		usersDirs.stream()
		.parallel()
//		.sequential()
		.forEach( new Consumer<File>() {
			@Override
			public void accept(File userDir) {
//		for (File userDir: friendsDirs) {
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
System.out.println("user ["+userCounter+"/"+usersTotal+"]:"+owningWallUserId);
		File[] files = userDir.listFiles();
		if (files.length==0) {
			incrementUserCounter();
			return;
		}
		
		postsParser.parse(userDir, owningWallUserId, useridToUserMap);
	}
	
	private List<File> getDirectories(String dirName) {
		List<File> dataDirs = Arrays.asList( new File(dirName).listFiles() );
//		List<File> dataDirs = new CopyOnWriteArrayList<>();
//		dataDirs.addAll( Arrays.asList( new File(dirName).listFiles() ) );
		return dataDirs;
	}

}
