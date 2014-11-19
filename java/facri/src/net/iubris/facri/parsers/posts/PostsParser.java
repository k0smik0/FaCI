package net.iubris.facri.parsers.posts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.annotations.filenamefilters.FeedsDirFilenameFilter;
import net.iubris.facri._di.annotations.filenamefilters.PostsFilenameFilter;
import net.iubris.facri.model.posts.Post;
import net.iubris.facri.model.posts.Posts;
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
	
	public void parse(File... arguments) {
		
		File userDir = arguments[0];
		Arrays.asList(
				(userDir.listFiles(feedsDirFilenameFilter)[0])
						.listFiles(postFilesFilenameFilter)
						)
		.stream()
		.parallel() // parallel on each posts file
		.forEach( new Consumer<File>() {
			@Override
			public void accept(File userFeedsJsonFile) {
				try {
					String owningWallUserId = userDir.getName();
					
					Posts posts = postsMapper.readObject(new FileReader(userFeedsJsonFile));
					
					posts.getPosts()
					.stream()
					.parallel() // parallel on each post
					.forEach( new Consumer<Post>() {
						@Override
						public void accept(Post post) {
							postParser.parse(post, owningWallUserId/*, useridToUserMap*/);
							commentsParser.parse(userDir, owningWallUserId, post/*, useridToUserMap*/);
						}
					});
				} catch (FileNotFoundException | XMLStreamException | NullPointerException | JAXBException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
