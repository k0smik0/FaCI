package net.iubris.facri.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri._di.guice.FacriModule;
import net.iubris.facri._di.provider.mapper.CommentsHolderMapperProvider;
import net.iubris.facri.model.Comment;
import net.iubris.facri.model.CommentsHolder;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class CommentsParser {

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
	System.out.println(commentData);
				String commentingUserId = commentData.getFromId();
	System.out.println("\t\t\tcomment: "+commentingUserId);
	System.out.println("here");
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
