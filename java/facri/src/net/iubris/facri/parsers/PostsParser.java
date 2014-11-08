package net.iubris.facri.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.annotations.filenamefilters.FeedsDirFilenameFilter;
import net.iubris.facri._di.annotations.filenamefilters.PostsFilenameFilter;
import net.iubris.facri.model.Post;
import net.iubris.facri.model.Posts;
import net.iubris.facri.model.User;
import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class PostsParser {
	
	private final FilenameFilter feedsDirFilenameFilter;
	private final FilenameFilter postFilesFilenameFilter;
	private final JsonXMLMapper<Posts> postsMapper;
	
	private final PostParser postParser;
	private final CommentsParser commentsParser;

	@Inject
	public PostsParser(
			@FeedsDirFilenameFilter FilenameFilter feedsDirFilenameFilter,
			@PostsFilenameFilter FilenameFilter postFilenameFilter,
			JsonXMLMapper<Posts> postsMapper,
			PostParser postParser,
			CommentsParser commentsParser) {
				this.feedsDirFilenameFilter = feedsDirFilenameFilter;
				postFilesFilenameFilter = postFilenameFilter;
				this.postsMapper = postsMapper;
				this.postParser = postParser;
				this.commentsParser = commentsParser;
	}
	
	public void parse(File userDir, String owningWallUserId, Map<String, User> useridToUserMap) {
		
//		userDir.listFiles( new FilenameFilter() {
//			@Override
//			public boolean accept(File dir, String name) {
//				return false;
//			}
//		});
		File feedDir = userDir.listFiles(feedsDirFilenameFilter)[0];
//		System.out.println("FEED: "+feedDir.getName());
		List<File> userFeedsJsonFiles = Arrays.asList(feedDir.listFiles(postFilesFilenameFilter) );
		
		userFeedsJsonFiles.stream().parallel().forEach( new Consumer<File>() {
			@Override
			public void accept(File userFeedsJsonFile) {
				try {
					Posts posts = postsMapper.readObject(new FileReader(userFeedsJsonFile));
					List<Post> postsList = posts.getPosts();

//System.out.println("\tPosts: "+postsList.size());
//					int postsCounter = 1;
					for (Post post : postsList) {
						postParser.parse(post, owningWallUserId, useridToUserMap);
						commentsParser.parse(userDir, owningWallUserId, post, useridToUserMap);
//						postsCounter++;
					}
				} catch (FileNotFoundException | XMLStreamException | NullPointerException | JAXBException e) {
					e.printStackTrace();
				}
			}
		});

	}
}
