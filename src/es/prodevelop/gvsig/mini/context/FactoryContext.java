package es.prodevelop.gvsig.mini.context;

import es.prodevelop.gvsig.mini.activities.Map;
import es.prodevelop.gvsig.mini.context.map.DefaultContext;


/**
 * 
 * @author sim
 * A factory to retrieve the default context and much more !
 */
public class FactoryContext implements IFactoryContext {
	
	public ItemContext createDefaultContext(){
		return new DefaultContext();
	}
	
	public ItemContext createDefaultContext(Map m){
		return new DefaultContext(m);
	}
}
