package net.iubris.facri.model.adapter;

import java.text.ParseException;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date> {
	
//	private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
	

	@Override
	public String marshal(Date date) {
		return ""+date.getTime();
//		return dateFormat.format(date);
	}

	@Override
	public Date unmarshal(String string) throws ParseException {
//		return new Date.parse(string);
		return new Date( Long.parseLong(string)*1000 );
//		return dateFormat.parse(string);
	}

}
