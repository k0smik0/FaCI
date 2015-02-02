/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (Comment.java) is part of facri.
 * 
 *     Comment.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     Comment.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.model.parser.comments;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.iubris.facri.model.parser.adapters.DateAdapter;

import com.sleepycat.persist.model.Persistent;

@Persistent
@XmlAccessorType(XmlAccessType.FIELD)
public class Comment implements Serializable {

	private static final long serialVersionUID = 6999503471597138386L;

	@XmlElement(name="fromid")
   private String fromId;

   @XmlJavaTypeAdapter(DateAdapter.class)
   @XmlElement(name="time")
   private Date time;

   public final String getFromId() {
           return fromId;
   }

   public final Date getTime() {
           return time;
   }

   @Override
   public String toString() {
           return "fromId: "+fromId+", time: "+time;
   }
	
}
