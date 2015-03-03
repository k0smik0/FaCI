/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (CommentInfo.java) is part of facri.
 * 
 *     CommentInfo.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     CommentInfo.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.parser.model.comments;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.sleepycat.persist.model.Persistent;

/**
 * @author "Massimiliano Leone - maximilianus@gmail.com"
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Persistent
public class CommentInfo implements Serializable {

	private static final long serialVersionUID = -7878220575677233802L;
	// can_comment":"1","comment_count":"0","comment_order":"chronological
	
	@XmlElement(name = "comment_count")
//	@XmlJavaTypeAdapter(IntegerAdapter.class)
	private int commentCount;

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
}
