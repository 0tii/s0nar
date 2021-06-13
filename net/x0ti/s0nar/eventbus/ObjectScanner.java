package net.x0ti.s0nar.eventbus;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.x0ti.s0nar.Event;
import net.x0ti.s0nar.util.S0narEventListener;

/**
 * !Not implemented!
 * Lightweight reflection based class to scan an object for valid methods.
 * @author 0ti
 */
public class ObjectScanner 
{
	public Method[] scan(Object o)
	{
		List<Method> validMethods = new ArrayList<>();
		for(Method m: o.getClass().getDeclaredMethods())
		{
			if(isMethodValid(m))
			{
				validMethods.add(m);
			}
		}
		return validMethods.toArray(new Method[0]);
	}
	
	/**
	 * Checks a methods validity according to the three compulsory properties:<br>
	 * - Must be annotated with {@link S0narEventListener}<br>
	 * - Must have no more than 1 parameter<br>
	 * - Parameter must extend {@link Event}<br>
	 * @param method - the method to check
	 * @return true if all 3 conditions are met, false if one or more conditions are not met.
	 */
	private boolean isMethodValid(Method method)
	{
		return (method.isAnnotationPresent(S0narEventListener.class) && 
				method.getParameterCount() == 1 &&
				Event.class.isAssignableFrom(method.getParameterTypes()[0]));
	}
}
