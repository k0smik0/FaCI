/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (CommentsHolder.java) is part of facri.
 * 
 *     CommentsHolder.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     CommentsHolder.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.model.parser.comments;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.odysseus.staxon.json.jaxb.JsonXML;

@JsonXML(autoArray=false,virtualRoot=true)
@XmlRootElement
//(name="comments")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentsHolder {
	
	@XmlElement(name = "comments")
	private CommentsData commentsData;
	
	public CommentsData getCommentsData() {
		return commentsData;
	}

	@Override
	public String toString() {
		return hashCode()+": "+commentsData/*+", "+commentsData.size()*/;
	}
}
