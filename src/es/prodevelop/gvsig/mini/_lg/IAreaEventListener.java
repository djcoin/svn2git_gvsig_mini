package es.prodevelop.gvsig.mini._lg;


public interface IAreaEventListener 
{
	final static int AREA_ENTER = 1;
	final static int AREA_LEAVE = 2;
	
	public void onAreaChanged(int event, Area z);
	// Shortcut method to use in Map...
	public void onAreaVisited(Area z);
	public void onAreaUnvisited(Area z);
}
