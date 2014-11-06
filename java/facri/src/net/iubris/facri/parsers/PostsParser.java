package net.iubris.facri.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.annotations.filenamefilters.PostFilenameFilter;
import net.iubris.facri.model.Post;
import net.iubris.facri.model.Posts;
import net.iubris.facri.model.User;
import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class PostsParser {
	
	private final JsonXMLMapper<Posts> postsMapper;
	private final FilenameFilter postFilesFilenameFilter;
	
	private final PostParser postParser;
	private final CommentsParser commentsParser;

	@Inject
	public PostsParser(@PostFilenameFilter FilenameFilter postFilenameFilter,
			JsonXMLMapper<Posts> postsMapper,
			PostParser postParser,
			CommentsParser commentsParser) {
				postFilesFilenameFilter = postFilenameFilter;
				this.postsMapper = postsMapper;
				this.postParser = postParser;
				this.commentsParser = commentsParser;
	}
	
	public void parse(File userDir, String owningWallUserId, Map<String, User> useridToUserMap) {
		
		File userFeedsJsonFile = userDir.listFiles(postFilesFilenameFilter)[0];
		
		try {
			Posts posts = postsMapper.readObject( new FileReader(userFeedsJsonFile) );
			List<Post> postsList = posts.getPosts();
			
			//System.out.println("\tPosts: "+postsList.size());
			int postsCounter=1;
			for (Post post: postsList) {
				postParser.parse(post, owningWallUserId, useridToUserMap);
				commentsParser.parse(userDir, owningWallUserId, post, useridToUserMap);
				postsCounter++;
			}
//System.out.println("\n");
			
		} catch(FileNotFoundException | XMLStreamException | NullPointerException | JAXBException e) {
			e.printStackTrace();
		} 

	}
}
