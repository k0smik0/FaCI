/*******************************************************************************
 * Copyleft (c) 2015, "Massimiliano Leone - <maximilianus@gmail.com> - https://plus.google.com/+MassimilianoLeone"
 * This file (PersisterJUnitTest.java) is part of facri.
 * 
 *     PersisterJUnitTest.java is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     PersisterJUnitTest.java is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with .  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iubris.facri.persisters;

import net.iubris.facri._di.guice.module.main.FacriModule;
import net.iubris.facri.persisters.specialized.EgoPersister;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class PersisterJUnitTest {

	private Injector injector;
	@Before
	public void setUp() throws Exception {
		injector = Guice.createInjector( new FacriModule() );
	}

	@Test
	public void test() {
//		fail("Not yet implemented");
		injector.getInstance(EgoPersister.class)
			.keysIterator()
			.forEachRemaining(n->System.out.println(n));
	}

}
