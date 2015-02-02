/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (FacriBerkeleyDBHelper.java) is part of facri.
 * 
 *     FacriBerkeleyDBHelper.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     FacriBerkeleyDBHelper.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.persister.helpers.base;

import net.iubris.berkeley_persister.core.helper.base.BerkeleyDBHelper;
import net.iubris.facri.model.parser.users.User;

public interface FacriBerkeleyDBHelper<U extends User> extends BerkeleyDBHelper<String, U> {}
