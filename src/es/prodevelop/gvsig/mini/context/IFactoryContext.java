package es.prodevelop.gvsig.mini.context;

import es.prodevelop.gvsig.mini.activities.Map;

public interface IFactoryContext {

	public ItemContext createDefaultContext();		
	public ItemContext createDefaultContext(Map m);
	
}
