package net.iubris.facri.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import net.iubris.facri.model.Post;
import net.iubris.facri.model.Posts;
import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class JSONParser {

	public void parse(File jsonFile) throws JAXBException, FileNotFoundException, XMLStreamException {
		
		JsonXMLMapper<Posts> mapper = new JsonXMLMapper<Posts>(Posts.class);
		
		Posts posts = mapper.readObject( new FileReader(jsonFile) );
		for (Post post: posts.getPosts()) {
			System.out.println(post.getPostId());
		}
	}
	
	public static void main(String[] args) {
		
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
	}
}
