package net.iubris.facri._di.guice.grapher.factories;

import net.iubris.facri.console.actions.graph.utils.cache.CacheHandler;

public interface CacheHandlerFactory {
//	UseCache create(boolean read, boolean write);
	CacheHandler create(String[] params);
}
