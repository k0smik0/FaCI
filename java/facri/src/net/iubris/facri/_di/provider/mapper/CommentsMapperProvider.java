package net.iubris.facri._di.provider.mapper;

import javax.inject.Provider;
import javax.xml.bind.JAXBException;

import net.iubris.facri.model.Comments;
import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class CommentsMapperProvider implements Provider<JsonXMLMapper<Comments>> {
	
	private final JsonXMLMapper<Comments> commentsMapper;

	public CommentsMapperProvider() throws JAXBException {
		commentsMapper = new JsonXMLMapper<Comments>(Comments.class);
	}
	
	@Override
	public JsonXMLMapper<Comments> get() {
		return commentsMapper;
	}
}
