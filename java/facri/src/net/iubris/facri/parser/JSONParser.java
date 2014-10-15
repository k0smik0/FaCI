package net.iubris.facri.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.model.CommentInfo;
import net.iubris.facri.model.Comments;
import net.iubris.facri.model.Post;
import net.iubris.facri.model.Posts;
import net.iubris.facri.model.User;
import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class JSONParser {

	private String feedsDataDir;
	private String feedsFriendsDataDir;
	private String feedsMeDataDir;
	private String friendsIds;
	private String friendsLikesDir;
	
	private FilenameFilter postsFileFilenameFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			if (name.equals("posts.json"))
				return true;
			return false;
		}
	};
	
	private FilenameFilter commentsFileFilenameFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			if (name.equals("comments.json"))
				return true;
			return false;
		}
	};

	public JSONParser(String dataRootDir) {
		
		this.feedsDataDir = dataRootDir+File.separatorChar+"feeds";
		this.feedsMeDataDir = feedsDataDir+File.separatorChar+"me";
		this.feedsFriendsDataDir = feedsDataDir+File.separatorChar+"friends";
		
		this.friendsIds = dataRootDir+File.separatorChar+"friends_ids.txt";
		
		this.friendsLikesDir = dataRootDir+File.separatorChar+"likes";
	}

	public void parseFeed() throws JAXBException, FileNotFoundException, XMLStreamException {
		
		JsonXMLMapper<Posts> mapper = new JsonXMLMapper<Posts>(Posts.class);
		
		File friendsDataDir = new File(feedsFriendsDataDir);
System.out.println(friendsDataDir);
		List<File> friendsDirs = Arrays.asList( new File(feedsFriendsDataDir).listFiles() );
		
		
		Map<String,User> usersMap = new ConcurrentHashMap<>();
		
//		sequentialWay(friendsDirs, mapper);
		parallelWay(friendsDirs, usersMap, mapper);
		
	}
	
	protected void parallelWay(List<File> friendsDirs, Map<String,User> map, JsonXMLMapper<Posts> mapper) {
		Date start = new Date();
		friendsDirs.parallelStream().forEach( new Consumer<File>() {
			@Override
			public void accept(File userDir) {
				// userId is sourceId, that is the wall containing the post
				String userId = userDir.getName();
				File[] files = userDir.listFiles();
				
				if (files.length==0)
					return;
				File userFeedsJsonFile = userDir.listFiles(postsFileFilenameFilter)[0];
				File commentsJsonFile = userDir.listFiles(commentsFileFilenameFilter)[0];
//				System.out.println(userFeedsJsonFile.getName());
				try {
					Posts posts = mapper.readObject( new FileReader(userFeedsJsonFile) );
					Comments comments = mapper.readObject( new FileReader(commentsJsonFile));
					for (Post post: posts.getPosts()) {
//						System.out.print(post.getPostId()+" ");
						// the actorId is the post author id
						String actorPostId = post.getActorId();
						
						// we use actorId because a wall contains both posts 
						// from owner (actorId = userDir) or other users (actorId != userDir) 
						User user = null;
						if (map.containsKey(actorPostId)) {
							user = map.get(actorPostId);
						} else {
							user = new User();
							map.put(actorPostId, user);
						}
						buildUser(user, post, actorPostId, userId);
					}
//					System.out.println("\n");
				} catch (FileNotFoundException | JAXBException | XMLStreamException e) {
					e.printStackTrace();
				}
				
			}
		});
		Date end = new Date();
		double finish = (end.getTime()-start.getTime())/1000f;
		System.out.println( "parallel way in: "+finish+"s" );
	}
	
	protected void sequentialWay (List<File> friendsDirs, JsonXMLMapper<Posts> mapper) throws FileNotFoundException, JAXBException, XMLStreamException {
		Date start = new Date();
		for (File userDir: friendsDirs) {
			File[] files = userDir.listFiles();
			if (files.length==0)
				continue;
			
			File userFeedsJsonFile = userDir.listFiles()[0];
			
			Posts posts = mapper.readObject( new FileReader(userFeedsJsonFile) );
			for (Post post: posts.getPosts()) {
//				System.out.print(post.getPostId()+" ");
			}
//			System.out.println("\n");
		}
		Date end = new Date();
		double finish = (end.getTime()-start.getTime())/1000f;
		System.out.println( "sequenzial way in: "+finish+"s" );
	}
	
	
	private void buildUser(User user, Post post, String actorId, String sourceId) {
		// here we have a builded user with userId = [post]ActorId
//		String userId = user.getId();
		/* PSEUDO_CODE
		
		 	if (actorId == userId)
		 		user.getOwnPost.add(post)
		 	else
		 		user.getElsewherepost.add(post)
			
		*/
		// if the user is post author...
//		if (userId.equals( postActorId ) ) {			 
			
			// associate the post to user/author
			String sourceID = post.getSourceID(); // where the post is
			// if the postActorId is the same of sourceId, 
			// the post exists in the own author wall,
			// elsewhere is on his friend wall
			if (sourceID == actorId) {
				user.getOwnPosts().add(post);
			} else {
				user.getElsewhereToPosts().add(post);
			}
							
			
			// in degree	
			post.getCommentInfo().getCommentCount()
			post.getShareCount()
			post.getLikesInfo().getFriendsUserIDs()
			post.getLikesInfo().getSamplesUserIDs()

//			post.getPermalink()
			

			// out degree
			user.getPostInteractions().getTargetedTo().add( post.getTaggedIDs() );
			user.getPostInteractions().getTargetedTo().add( post.getWithTaggedFriendsIDs() );
			
		CommentInfo commentInfo = post.getCommentInfo();
	}
	
	/*public static void main(String[] args) {
				
		String FILE_PATH = "../../fbcmd/facri_output/me/836460098/output.json";
		JSONParser jp = new JSONParser();
		try {
			jp.parse(new File(FILE_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}*/
}
