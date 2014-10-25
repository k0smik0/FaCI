package net.iubris.facri.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.annotation.filenamefilter.CommentFilenameFilter;
import net.iubris.facri._di.annotation.filenamefilter.PostFilenameFilter;
import net.iubris.facri.model.Comment;
import net.iubris.facri.model.CommentsHolder;
import net.iubris.facri.model.Post;
import net.iubris.facri.model.Posts;
import net.iubris.facri.model.User;
import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class PostsParser {

	private String feedsDataDir;
	private String feedsFriendsDataDir;
	private String feedsMeDataDir;
//	private String friendsIds;
//	private String friendsLikesDir;
//	
	private JsonXMLMapper<Posts> postsMapper;
	private JsonXMLMapper<CommentsHolder> commentsMapper;
	
	private FilenameFilter postFilesFilenameFilter;
	
	private FilenameFilter commentFilesFilenameFilter;

	private Map<String, User> useridToUserMap;
	private int userCounter;

	@Inject
	public PostsParser(
			@Named("data_root_dir_path") String dataRootDirPath,
			@Named("feeds_dir_relative_path") String feedsDirRelativePath,
			@Named("feeds_me_dir_relative_path") String feedsMeDirRelativePath,
			@Named("feeds_friends_dir_relative_path") String feedsFriendsDirRelativePath,
			@Named("friends_ids_file") String friendsIdsFileRelativePath,
			@Named("friends_like_dir_relative_path") String friendsLikesDirRelativePath,
			
//			@Named("post_file_regex") String postFileRegex,
//			@Named("comment_file_regex") String commentFileRegex,
			
			@PostFilenameFilter FilenameFilter postFilenameFilter,
			@CommentFilenameFilter FilenameFilter commentFilenameFilter,
			
			JsonXMLMapper<Posts> postsMapper,
			JsonXMLMapper<CommentsHolder> commentsMapper) {
		
		System.out.println(dataRootDirPath);

		this.feedsDataDir = dataRootDirPath+File.separatorChar+feedsDirRelativePath;		
		this.feedsMeDataDir = feedsDirRelativePath+File.separatorChar+feedsMeDirRelativePath;		
		this.feedsFriendsDataDir = feedsDataDir+File.separatorChar+feedsFriendsDirRelativePath;		
//		this.friendsIds = dataRootDirPath+File.separatorChar+friendsIdsFileRelativePath;		
//		this.friendsLikesDir = dataRootDirPath+File.separatorChar+friendsLikesDirRelativePath;
		
		this.postFilesFilenameFilter = postFilenameFilter;
		this.commentFilesFilenameFilter = commentFilenameFilter;

		this.postsMapper = postsMapper;
		this.commentsMapper = commentsMapper;
		
		useridToUserMap = new ConcurrentHashMap<>();
		
		this.userCounter = 0;
	}

	public void parseFeed() throws JAXBException, FileNotFoundException, XMLStreamException {
		
//		Map<String,User> usersMap = new ConcurrentHashMap<>();
		
//		sequentialWay(friendsDirs, mapper);
System.out.println(feedsFriendsDataDir);
		parallelWay( getDirectories(feedsFriendsDataDir));
		
		// TO DO restore
//		parallelWay( getDirectories(feedsMeDataDir) );
	}
	
	private List<File> getDirectories(String dirName) {
		List<File> dataDirs = Arrays.asList( new File(dirName).listFiles() );
		return dataDirs;
	}
	
	protected void parallelWay(List<File> friendsDirs) {
		Date start = new Date();
		userCounter = 1;
		
//		friendsDirs.stream().sequential().forEach( new Consumer<File>() {
//			@Override
//			public void accept(File userDir) {
		for (File userDir: friendsDirs) {
				// userId is Post.sourceId, that is the id of user which wall contains the post
				String owningWallUserId = userDir.getName();
System.out.println("user ["+userCounter+"]:"+owningWallUserId);
				File[] files = userDir.listFiles();
				if (files.length==0) {
					System.out.println(userDir.getName()+": zero files");
					return;
				}
				
				File userFeedsJsonFile = userDir.listFiles(postFilesFilenameFilter)[0];
				
//				System.out.println("\t"+userFeedsJsonFile.getName());
//				try {
System.out.println("\t"+"trying "+userFeedsJsonFile.getName());
					
					List<Post> postsList = null;
					try {
						Posts posts = postsMapper.readObject( new FileReader(userFeedsJsonFile) );
						postsList = posts.getPosts();
					} catch(FileNotFoundException | XMLStreamException | NullPointerException | JAXBException e) {
						postsList = Collections.emptyList();
						e.printStackTrace();
					}
//					List<Post> postsList = posts.getPosts();
System.out.println("\tPosts: "+postsList.size());
					int i=1;
					for (Post post: postsList) {
						
						parsePost(post, owningWallUserId, i);
						
						/*File[] commentsFiles = userDir.listFiles(commentFilesFilenameFilter);
						if (commentsFiles.length>0) {
System.out.println(commentsFiles.length+" commentsFiles");
							File commentsJsonFile = commentsFiles[0];
System.out.println("\t\tcomments in: "+commentsJsonFile.getName());
							CommentsHolder comments = commentsMapper.readObject( new FileReader(commentsJsonFile));
							parseComments(comments,owningWallUserId,post);
						} else {
System.out.println(".");
						}*/
						parseComments(userDir,owningWallUserId,post);
						i++;
					}
					System.out.println("\n");
//				} catch (FileNotFoundException | XMLStreamException | NullPointerException e) {
//					e.printStackTrace();
//				}
				userCounter++;
			}
//		}); // end stream
		Date end = new Date();
		double finish = (end.getTime()-start.getTime())/1000f;
		System.out.println( "parallel way in: "+finish+"s" );
	}

	private void parsePost(Post post, String owningWallUserId, int i) {
		System.out.print("\tpost ["+i+"]: "+post.getPostId()+", ");
		// the actorId is the post author id
		String actorPostId = post.getActorId();

		// we use actorId because a wall contains both posts 
		// from owner (actorId == userDir) or other users (actorId != userDir) 
		User user = isExistentUserOrCreateEmpty(actorPostId);
		user.addOwnPost(post); // always add post to its author
		
		if (actorPostId==owningWallUserId) { // post author is the wall owner 
			handleActorUser(user, post);
		} else if (actorPostId!=owningWallUserId ) {
			handleTargetUser(user, post, owningWallUserId);
		}

		handleTags(post, user, post.getTaggedIDs());
		handleTags(post, user, post.getWithTaggedFriendsIDs() );
		
		handleLikes(post.getLikesInfo().getFriendsUserIDs(), user, owningWallUserId);
		handleLikes(post.getLikesInfo().getSamplesUserIDs(), user, owningWallUserId);
	}
	
	private void handleActorUser(User user, Post post) {
		int shareCount = post.getShareCount();
		if (shareCount >0)
			user.incrementOwnPostResharing(shareCount);
	}
	
	private void handleTargetUser(User user, Post post, String targetUserId) {
//		user.addToSomeoneElsePost(targetUserId, post);
		user.getOtherUserMapInteractions(targetUserId).addPost(post);
	}
	
	/*
	private void buildUser(User user, Post post, String actorId, String sourceId, Map<String, User> useridToUserMap) {
//		// associate the post to user/author
//		String sourceID = post.getSourceID(); // where the post is
//		// if the postActorId is the same of sourceId, 
//		// the post exists in the own author wall,
//		// elsewhere is on his friend wall
//		if (sourceID == actorId) {
//			user.addOwnPost(post);
//		} else {
//			// maintain a reference to target user
//			User targetUser = isExistentUserOrCreateEmpty(useridToUserMap, userId);  
//			user.addToSomeoneElsePost(sourceId, post);
//		}
		
		// in degree

//		if (post.getCommentInfo().getCommentCount() >0) {
//			File commentsJsonFile = userDir.listFiles(commentFilesFilenameFilter)[0];
//			Comments comments = commentsMapper.readObject( new FileReader(commentsJsonFile));
//			parseComments(user,comments);
//		}
		
//		int shareCount = post.getShareCount();
//		if (shareCount >0)
//			user.incrementOwnPostResharing(shareCount);
		
		
//		post.getPermalink()
			

		// out degree
//		user.getPostInteractions().getTargetedTo().add( post.getTaggedIDs() );
//		user.getPostInteractions().getTargetedTo().add( post.getWithTaggedFriendsIDs() );
			
//		CommentInfo commentInfo = post.getCommentInfo();
//		commentInfo.getCommentCount();
//		Comments comments; 
//		List<CommentData> commentData = comments.getCommentData();
//		for (CommentData comment : commentData) {
//			comment.getFromId();
//		}
	}
	*/
	
	private void handleTags(Post post, User user, Set<String> taggedIds) {
		for (String taggedId : post.getTaggedIDs()) {
			/*
			Map<String, Interactions> otherUsersMapInteractions = user.getOtherUsersMapInteractions();
			Interactions interactions = null;
			if (!otherUsersMapInteractions.containsKey(taggedId)) {
				interactions = new Interactions();
				otherUsersMapInteractions.put(taggedId, interactions);
			} else {
				interactions = otherUsersMapInteractions.get(taggedId);
			}
			interactions.incrementTags();
			*/
			user.getOtherUserMapInteractions(taggedId).incrementTags();
			
		}
	}
	
	/**
	 * here we handle the post likes: maintain a counter for interacting user, that is:
	 * "if a user like a post, then he interacts with an other user (friends or not) which wall 
	 * is containing the post; in addition, a counter for liked post is incremented, in actor user instance   
	 * @param likingUsersIds
	 * @param wallOwnerId
	 */
	private void handleLikes(Set<String> likingUsersIds, User actorUser, String wallOwnerId) {
		// interactingUsersIds are owning wall user's friends or not - it depends on wall privacy		
		int howLikes = likingUsersIds.size();
		if (howLikes >0) {
			for (String interactingUserId : likingUsersIds) {
				User interactingUser = isExistentUserOrCreateEmpty(interactingUserId);
				useridToUserMap.put(interactingUserId, interactingUser);
				
				// create user if it doesn't exist
				if (!useridToUserMap.containsKey(wallOwnerId))
					useridToUserMap.put(wallOwnerId, isExistentUserOrCreateEmpty(wallOwnerId));
				
				interactingUser.getOtherUserMapInteractions(wallOwnerId).incrementLikes();
			}
			actorUser.incrementOwnPostsLiking(howLikes);
		}
	}
	
	private void parseComments(File userDir,/*CommentsHolder commentsHolder,*/ String owningWallUserId, Post post) {
		
		File[] commentsFiles = userDir.listFiles(commentFilesFilenameFilter);
		if (commentsFiles.length>0) {
System.out.println(commentsFiles.length+" commentsFiles");
			File commentsJsonFile = commentsFiles[0];
System.out.println("\t\tcomments in: "+commentsJsonFile.getName());
			try {
				CommentsHolder comments = commentsMapper.readObject( new FileReader(commentsJsonFile));
				
//System.out.println(comments);
				List<Comment> commentsData = comments.getCommentsData().getCommentsData();
				
				for (Comment commentData: commentsData) {
//		System.out.println(commentData);
					String commentingUserId = commentData.getFromId();
		System.out.println("\t\t\tcomment: "+commentingUserId);
//		System.out.println("here");
					User commentingUser = isExistentUserOrCreateEmpty(commentingUserId);
					commentingUser.getOtherUserMapInteractions(owningWallUserId).incrementComments();				
				}
			} catch (FileNotFoundException | JAXBException | XMLStreamException e) {
				System.out.println("error for file: "+commentsJsonFile.getName());
				e.printStackTrace();
			}						
		}	else {
			System.out.println(".");
		}
		
		
		/*List<Comment> comments = commentsHolder.getComments();
		for (Comment comment: comments) {
			String commentingUserId = comment.getFromId();
System.out.println("\t\t\tcomment: "+commentingUserId);			
			User commentingUser = isExistentUserOrCreateEmpty(commentingUserId);
			commentingUser.getOtherUserMapInteractions(owningWallUserId).incrementComments();
		}*/
	}
	
	private User isExistentUserOrCreateEmpty(String userId) {
		User user = null;
		if (useridToUserMap.containsKey(userId)) {
			user = useridToUserMap.get(userId);
		} else {
			user = new User();
			useridToUserMap.put(userId, user);
		}
		return user;
	}
	
	/*protected void sequentialWay (List<File> friendsDirs, JsonXMLMapper<Posts> mapper) throws FileNotFoundException, JAXBException, XMLStreamException {
		Date start = new Date();
		for (File userDir: friendsDirs) {
			File[] files = userDir.listFiles();
			if (files.length==0)
				continue;
			
			File userFeedsJsonFile = userDir.listFiles()[0];
			
			Posts posts = mapper.readObject( new FileReader(userFeedsJsonFile) );
//			for (Post post: posts.getPosts()) {
////				System.out.print(post.getPostId()+" ");
//			}
//			System.out.println("\n");
		}
		Date end = new Date();
		double finish = (end.getTime()-start.getTime())/1000f;
		System.out.println( "sequenzial way in: "+finish+"s" );
	}*/
	
}
