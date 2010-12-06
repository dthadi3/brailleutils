package org.daisy.paper;

import java.util.Iterator;

import javax.imageio.spi.ServiceRegistry;

import org.daisy.factory.FactoryCatalog;

/**
 * Provides a catalog of Paper factories.
 * @author Joel Håkansson, TPB
 */
public abstract class PaperCatalog implements FactoryCatalog<Paper> {
	
	protected PaperCatalog() {	}
	
	/**
	 * Obtains a new PaperCatalog instance. If at least one implementation can be found 
	 * using the Services API, then the first one will be returned. Otherwise the default PaperCatalog
	 * will be used.
	 * 
	 * The default PaperCatalog will use the Services API to
	 * find PaperProviders. The combined result from all PaperProviders are available to
	 * the catalog.
	 * @return returns a new PaperCatalog instance. 
	 */
	public static PaperCatalog newInstance() {
		Iterator<PaperCatalog> i = ServiceRegistry.lookupProviders(PaperCatalog.class);
		while (i.hasNext()) {
			return i.next();
		}
		return new DefaultPaperCatalog();
	}
	/*
	public abstract Object getFeature(String key);
	
	public abstract void setFeature(String key, Object value);
	
	public abstract Paper get(String identifier);
	
	public abstract Collection<Paper> list();
	
	public abstract Collection<Paper> list(PaperFilter filter);
*/
}
