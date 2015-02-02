/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (CommentsHolderMapperProvider.java) is part of facri.
 * 
 *     CommentsHolderMapperProvider.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     CommentsHolderMapperProvider.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
