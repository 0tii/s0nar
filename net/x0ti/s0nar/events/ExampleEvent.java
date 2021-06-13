package net.x0ti.s0nar.events;

import net.x0ti.s0nar.Event;

/**
 * Just a normal event, extends Event base class and can be raised and intercepted. Can be assigned any values through attributes. This can be transferred over to {@link CancelableEvent}
 * @author 0ti
 */
public class ExampleEvent extends Event 
{
	private String value;
	
	public ExampleEvent(String val)
	{
		value = val;
	}
	
	public String getValue() { return value; }
}
