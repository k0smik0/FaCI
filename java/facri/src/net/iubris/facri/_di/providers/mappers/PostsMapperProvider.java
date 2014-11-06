package net.iubris.facri._di.providers.mappers;

import javax.inject.Provider;
import javax.xml.bind.JAXBException;

import net.iubris.facri.model.Posts;
import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class PostsMapperProvider implements Provider<JsonXMLMapper<Posts>> {
	
	private final JsonXMLMapper<Posts> postsMapper;

	public PostsMapperProvider() throws JAXBException {
		postsMapper = new JsonXMLMapper<Posts>(Posts.class);
	}
	
	@Override
	public JsonXMLMapper<Posts> get() {
		return postsMapper;
	}
}
