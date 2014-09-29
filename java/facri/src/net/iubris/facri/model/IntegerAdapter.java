package net.iubris.facri.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IntegerAdapter extends XmlAdapter<String, Integer> {

	@Override
	public String marshal(Integer arg0) throws Exception {
		return ""+arg0;
	}

	@Override
	public Integer unmarshal(String arg0) throws Exception {
		return Integer.parseInt(arg0);
	}
}
