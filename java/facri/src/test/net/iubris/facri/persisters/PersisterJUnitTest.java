package net.iubris.facri.persisters;

import net.iubris.facri._di.guice.module.main.FacriModule;

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
