/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (PostsMapperProvider.java) is part of facri.
 * 
 *     PostsMapperProvider.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     PostsMapperProvider.java is distributed in the hope that it will be useful,
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

import net.iubris.facri.model.parser.posts.Posts;
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
