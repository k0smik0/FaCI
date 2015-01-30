package net.iubris.facri.parsers.posts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.annotations.parser.filenamefilters.FeedsDirFilenameFilter;
import net.iubris.facri._di.annotations.parser.filenamefilters.PostsFilenameFilter;
import net.iubris.facri.model.parser.posts.Post;
import net.iubris.facri.model.parser.posts.Posts;
import net.iubris.facri.parsers.Parser;
import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class PostsParser implements Parser {
	
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
	
	public void parse(File... userDirs) {
		
		File userDir = userDirs[0];
		
		File[] feedsFiles = userDir.listFiles(feedsDirFilenameFilter);
		
		if (feedsFiles.length>0) {
			File[] listFiles = feedsFiles[0].listFiles(postFilesFilenameFilter);
//			System.out.println(userDir+": "+listFiles.length);
			Arrays.asList( listFiles[0] )
			.stream()
			.parallel() // parallel on each posts file: 1-2 sec benefit
			.forEach( new Consumer<File>() {
				@Override
				public void accept(File userFeedsJsonFile) {
					try {
						String owningWallUserId = userDir.getName();						
						Posts posts = postsMapper.readObject(new FileReader(userFeedsJsonFile));						
						List<Post> postsList = posts.getPosts();
//						System.out.println(userDir+": "+postsList.size());						
						postsList.stream()
						.parallel() // parallel on each post: half-time benefit
						.forEach( new Consumer<Post>() {
							@Override
							public void accept(Post post) {
								postParser.parse(post, owningWallUserId);
								commentsParser.parse(userDir, owningWallUserId, post);
//								System.out.println( post.getLikesInfo() );
//								System.out.println(post.getTaggedIDs().size() );
							}
						});
					} catch (FileNotFoundException | XMLStreamException | NullPointerException | JAXBException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
