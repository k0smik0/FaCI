package net.iubris.facri._di.providers.parser.mappers;

import javax.inject.Provider;
import javax.xml.bind.JAXBException;

import net.iubris.facri.model.parser.comments.CommentsHolder;
import de.odysseus.staxon.json.jaxb.JsonXMLMapper;

public class CommentsHolderMapperProvider implements Provider<JsonXMLMapper<CommentsHolder>> {
	
	private final JsonXMLMapper<CommentsHolder> commentsMapper;

	public CommentsHolderMapperProvider() throws JAXBException {
		commentsMapper = new JsonXMLMapper<CommentsHolder>(CommentsHolder.class);
	}
	
	@Override
	public JsonXMLMapper<CommentsHolder> get() {
		return commentsMapper;
	}
}
