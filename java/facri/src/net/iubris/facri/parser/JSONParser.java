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

	private String feedsDataDir;
	private String feedsFriendsDataDir;
	private String feedsMeDataDir;
	private String friendsIds;
	private String friendsLikesDir;

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
		File[] friendsDirs = new File(feedsFriendsDataDir).listFiles();
		for (File userDir: friendsDirs) {
			System.out.println(userDir.getName());
			File[] files = userDir.listFiles();
			if (files.length==0)
				continue;
			
			File userFeedsJsonFile = userDir.listFiles()[0];
			
			System.out.println(userFeedsJsonFile.getName());
			Posts posts = mapper.readObject( new FileReader(userFeedsJsonFile) );
			for (Post post: posts.getPosts()) {
				System.out.println(post.getPostId());
			}
			System.out.println();
		}
		
		
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
