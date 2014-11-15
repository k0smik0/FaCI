package net.iubris.facri.model.comments;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import net.iubris.facri.model.adapters.DateAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class Comment {

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
