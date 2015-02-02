/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (HelpersHolder.java) is part of facri.
 * 
 *     HelpersHolder.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     HelpersHolder.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.persister.persisters.base;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;

import net.iubris.facri.persister.helpers.base.FacriBerkeleyDBHelper;

@SuppressWarnings("rawtypes")
@Singleton
public class HelpersHolder {

	private Set<FacriBerkeleyDBHelper> helpers = new HashSet<>();

	public Set<FacriBerkeleyDBHelper> getHelpers() {
		return helpers;
	}

	public void addHelper(FacriBerkeleyDBHelper facriBerkeleyDBHelper) {
		System.out.println("adding ");
		this.helpers.add(facriBerkeleyDBHelper);
	}
}
