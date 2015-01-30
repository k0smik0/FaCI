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
