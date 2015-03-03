/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (PostsParser.java) is part of facri.
 * 
 *     PostsParser.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     PostsParser.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.parser.parsers.posts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.faci.parser._di.annotations.filenamefilters.FeedsDirFilenameFilter;
import net.iubris.faci.parser._di.annotations.filenamefilters.PostsFilenameFilter;
import net.iubris.faci.parser.model.posts.Post;
import net.iubris.faci.parser.model.posts.Posts;
import net.iubris.faci.parser.parsers.Parser;
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
				this.postFilesFilenameFilter = postFilenameFilter;
				this.postsMapper = postsMapper;
				this.postParser = postParser;
				this.commentsParser = commentsParser;
	}
	
	public void parse(File... userDirs) {
		File userDir = userDirs[0];
		Arrays.asList( userDir.listFiles(feedsDirFilenameFilter) )
		.stream()
		.flatMap( feedDir->Arrays.asList(feedDir.listFiles(postFilesFilenameFilter)).stream() )
		
		.parallel() // parallel on each posts file: 1-2 sec benefit
		.forEach( 
			userFeedsJsonFile-> {
				try {
					String owningWallUserId = userDir.getName();						
					Posts posts = postsMapper.readObject(new FileReader(userFeedsJsonFile));						
					List<Post> postsList = posts.getPosts();
					postsList.stream()
					.parallel() // parallel on each post: half-time benefit
					.forEach( 
						post -> {
							postParser.parse(post, owningWallUserId);
							commentsParser.parse(userDir, owningWallUserId, post);
						}
					);
				} catch (FileNotFoundException | XMLStreamException | NullPointerException | JAXBException e) {
					e.printStackTrace();
				}
			}
		);
	}
}
