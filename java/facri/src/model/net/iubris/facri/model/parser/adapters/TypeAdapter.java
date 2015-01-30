package net.iubris.facri.model.parser.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import net.iubris.facri.model.parser.posts.Type;

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
