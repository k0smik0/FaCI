package net.iubris.facri.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TypeAdapter extends XmlAdapter<String, Type> {

	@Override
	public String marshal(Type v) throws Exception {
		return v.getCode()+"";
	}

	@Override
	public Type unmarshal(String v) throws Exception {
		return Type.get(Integer.parseInt(v));
	}

}
