/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (Posts.java) is part of facri.
 * 
 *     Posts.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     Posts.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.parser.model.posts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.odysseus.staxon.json.jaxb.JsonXML;

@JsonXML(autoArray=true,virtualRoot=true)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Posts {
	
	@XmlElement(name="posts")
	private List<Post> posts;

	public List<Post> getPosts() {
		return posts;
	}

//	public void setPosts(List<Post> posts) {
//		this.posts = posts;
//	}
}
