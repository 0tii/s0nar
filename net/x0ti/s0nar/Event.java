package net.x0ti.s0nar;

/**
 * Base Event class that any single-state event and all more intrinsic event classes should inherit from
 * @author 0ti
 */
public abstract class Event {

	protected String name = this.getClass().getName();
	
	public Event() {}
	
	public String getName() 
	{
		return this.name;
	}
}
