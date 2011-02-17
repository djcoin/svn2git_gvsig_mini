package es.prodevelop.gvsig.mini._lg;

public class Area
{
	protected String name;

	public Area(String name) {
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString() {
		return "Area: " + name;
	}
	
}
