/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (TypeAdapter.java) is part of facri.
 * 
 *     TypeAdapter.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     TypeAdapter.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
