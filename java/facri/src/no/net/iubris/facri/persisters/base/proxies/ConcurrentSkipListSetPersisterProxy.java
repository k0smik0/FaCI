/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (ConcurrentSkipListSetPersisterProxy.java) is part of facri.
 * 
 *     ConcurrentSkipListSetPersisterProxy.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     ConcurrentSkipListSetPersisterProxy.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.persisters.base.proxies;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.sleepycat.persist.model.Persistent;
import com.sleepycat.persist.model.PersistentProxy;

@Persistent(proxyFor=ConcurrentSkipListSet.class)
public class ConcurrentSkipListSetPersisterProxy<E> implements PersistentProxy<ConcurrentSkipListSet<E>> {

	private final Set<E> set = new HashSet<E>();
	
	@Override
	public ConcurrentSkipListSet<E> convertProxy() {
		ConcurrentSkipListSet<E> concurrentSkipListSet = new ConcurrentSkipListSet<E>();
		concurrentSkipListSet.addAll(set);
		return concurrentSkipListSet;
	}

	@Override
	public void initializeProxy(ConcurrentSkipListSet<E> arg0) {
		set.addAll(arg0);
	}

}
