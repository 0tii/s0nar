package net.x0ti.s0nar.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.x0ti.s0nar.Event;
import net.x0ti.s0nar.util.EventPriority;
import net.x0ti.s0nar.util.S0narEventListener;

/**
 * Annotation-based event bus for S0nar Events. Optionally supports easy Priority Staging.
 * @author 0ti
 * @version 0.3 stable
 * @since 05/2021
 */
public class EventBus {
	
	/**
	 * Map <i>(ConcurrentHashMap)</i> containing the {@link Event}-derived class as key and a list of {@link EventListener}.<br>
	 */
	private final Map<Class<?>, ArrayList<EventListener>> eventListeners = new ConcurrentHashMap<Class<?>, ArrayList<EventListener>>();
	
	/**
	 * optional parameter to control whether priority staging should be used at all. Will eliminate sort-operations on register if false. Default true.
	 */
	private boolean usePriorityStaging = true;
	
	public EventBus(){}	
	public EventBus(boolean usePriorities) { usePriorityStaging = usePriorities; }
	
	/**
	 * Registers an Object to the event bus, scanning it for annotated, valid subscriber methods. If it finds a valid subscriber method it will be added to the Map of listeners.<br>
	 * The event each method subscribes to is determined by the class of the event parameter of the method.
	 * @param o - the subscriber object
	 * @return true if all annotated methods could be subscribed, false if one or more fail.
	 */
	public boolean register(Object o)
	{
		//get all valid methods
		for(Method m: o.getClass().getDeclaredMethods())
		{
			if(isMethodValid(m))
			{
				try 
				{
					//if event does not have listeners yet, initialize ArrayList for that events listeners
					Class<?> eventClass = m.getParameterTypes()[0];
					if(eventListeners.get(eventClass) == null)
						eventListeners.put(eventClass, new ArrayList<EventListener>());
					//assign each valid method to its spot in the eventListeners Map
					eventListeners.get(eventClass).add(new EventListener(m, o, m.getAnnotation(S0narEventListener.class).priority()));
					//sort event listeners by priority
					sortPriorityList(eventListeners.get(eventClass));
				}
				catch(Exception e) 
				{
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Unregisters all subscriber methods of the object given from the event bus. The object is scanned for valid methods, the corresponding Events are then determined from the method parameters
	 * and subsequently removed from the listener list as identified through the owner object.
	 * @param o - the unsubscribing object
	 */
	public void unregister(Object o)
	{
		//get all subscribed (valid) methods
		for(Method m: o.getClass().getDeclaredMethods())
		{
			if(isMethodValid(m) && eventListeners.get(m.getParameterTypes()[0]) != null)
			{
				try 
				{
					//remove listener from input object
					eventListeners.get(m.getParameterTypes()[0]).removeIf(obj -> obj.owner == o);
				}
				catch(Exception e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Notifies all {@link EventListener}s which listen to <code>eventIn</code>'s type.<br>
	 * @param eventIn - the event raised
	 * @return the event posted after it was supplied to each listener
	 */
	public Event post(Event eventIn)
	{
		if(eventListeners.get(eventIn.getClass()) == null) return eventIn;
				
		for(EventListener listener : eventListeners.get(eventIn.getClass()))
		{
			try 
			{
				listener.method.invoke(listener.owner, eventIn);
			} 
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) 
			{
				e.printStackTrace();
			}
		}	
		return eventIn;
	}
	
	/**
	 * Sorts a list of {@link EventSubscriber}s by priority in ascending order. 
	 * @param list
	 */
	private void sortPriorityList(ArrayList<EventListener> list)
	{
		if(usePriorityStaging && list.size() > 1)
			list.sort((o1,o2) -> o1.priority.compareTo(o2.priority));
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
	
	/**
	 * Class to wrap all necessary listener information and make it accessible in an intuitive manner.
	 * @author 0ti
	 * @version 1.0
	 */
	class EventListener
	{
		public Method method;
		public Object owner;
		public EventPriority priority;
		
		public EventListener(Method mthIn, Object ownIn, EventPriority prioIn)
		{
			method = mthIn;
			owner = ownIn;
			priority = prioIn;
		}
	}
	
}
