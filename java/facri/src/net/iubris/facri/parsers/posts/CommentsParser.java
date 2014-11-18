package net.iubris.facri.parsers.posts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.annotations.filenamefilters.CommentsFilenameFilter;
import net.iubris.facri._di.guice.module.FacriModule;
import net.iubris.facri._di.providers.mappers.CommentsHolderMapperProvider;
import net.iubris.facri.model.World;
import net.iubris.facri.model.comments.Comment;
import net.iubris.facri.model.comments.CommentsHolder;
import net.iubris.facri.model.posts.Post;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class CommentsParser {
	
	private final JsonXMLMapper<CommentsHolder> commentsMapper;
	private final FilenameFilter commentFilesFilenameFilter;
	private final World world;

	@Inject
	public CommentsParser(JsonXMLMapper<CommentsHolder> commentsMapper, 
			@CommentsFilenameFilter FilenameFilter commentFilesFilenameFilter,
			World world) {
		this.commentsMapper = commentsMapper;
		this.commentFilesFilenameFilter = commentFilesFilenameFilter;
		this.world = world;
	}

	public void parse(File userDir, String owningWallUserId, Post post/*, Map<String,User> useridToUserMap*/) {
		
		File[] commentsFiles = userDir.listFiles(commentFilesFilenameFilter);
		if (commentsFiles.length>0) {
//System.out.println(commentsFiles.length+" commentsFiles");
			File commentsJsonFile = commentsFiles[0];
//System.out.println("\t\tcomments in: "+commentsJsonFile.getName());
			try {
				CommentsHolder comments = commentsMapper.readObject( new FileReader(commentsJsonFile));
				
				List<Comment> commentsData = comments.getCommentsData().getCommentsData();
				
				for (Comment commentData: commentsData) {
					String commentingUserId = commentData.getFromId();
//		System.out.println("\t\t\tcomment: "+commentingUserId);
//					User commentingUser = ParsingUtils.isExistentFriendUserOrCreateEmpty(commentingUserId, useridToUserMap);
//					User commentingUser = 
							world.isExistentUserOrCreateNew(commentingUserId)
//							;
//					commentingUser
					.getOtherUserInteractions(owningWallUserId).incrementComments();				
				}
			} catch (FileNotFoundException | JAXBException | XMLStreamException e) {
				System.out.println("error for file: "+commentsJsonFile.getName());
				e.printStackTrace();
			}
		}	/*else {
			System.out.println(".");
		}*/
		
	}
	
	
	public static void main(String[] args) {
		String commentsJsonFile = "../../fbcmd/output/feeds/friends/1529169343/10203765980702095_comments.json";
		
		Injector injector = Guice.createInjector( new FacriModule() );
		
		JsonXMLMapper<CommentsHolder> commentsMapper = injector.getInstance(CommentsHolderMapperProvider.class).get();
		
		try {
			CommentsHolder commentsHolder = commentsMapper.readObject( new FileReader(commentsJsonFile));
			
//			System.out.println(commentsHolder);
			List<Comment> commentsData = commentsHolder.getCommentsData().getCommentsData();
//			
			for (Comment commentData: commentsData) {
//	System.out.println(commentData);
				String commentingUserId = commentData.getFromId();
	System.out.println("\t\t\tcomment: "+commentingUserId);
//	System.out.println("here");
			}
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
}