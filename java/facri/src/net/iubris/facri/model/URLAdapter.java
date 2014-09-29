package net.iubris.facri.model;

import java.net.URL;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class URLAdapter extends XmlAdapter<String, URL> {

	@Override
	public String marshal(URL v) throws Exception {
		return v.toString();
	}

	@Override
	public URL unmarshal(String v) throws Exception {
		return new URL(v);
	}	
}
