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

import net.iubris.facri._di.annotations.filenamefilters.CommentFilenameFilter;
import net.iubris.facri._di.guice.FacriModule;
import net.iubris.facri._di.providers.mappers.CommentsHolderMapperProvider;
import net.iubris.facri.model.Comment;
import net.iubris.facri.model.CommentsHolder;
import net.iubris.facri.model.Post;
import net.iubris.facri.model.User;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class CommentsParser {
	
	private final JsonXMLMapper<CommentsHolder> commentsMapper;
	private final FilenameFilter commentFilesFilenameFilter;

	@Inject
	public CommentsParser(JsonXMLMapper<CommentsHolder> commentsMapper, @CommentFilenameFilter FilenameFilter commentFilesFilenameFilter) {
		this.commentsMapper = commentsMapper;
		this.commentFilesFilenameFilter = commentFilesFilenameFilter;
	}

	public void parse(File userDir, String owningWallUserId, Post post, Map<String,User> useridToUserMap) {
		
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
					User commentingUser = ParsingUtils.isExistentUserOrCreateEmpty(commentingUserId, useridToUserMap);
					commentingUser.getOtherUserMapInteractions(owningWallUserId).incrementComments();				
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
