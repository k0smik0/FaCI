/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (URLAdapter.java) is part of facri.
 * 
 *     URLAdapter.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     URLAdapter.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.faci.parser.model.adapters;

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
