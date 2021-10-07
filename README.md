
# 📡 S0nar Event System 
![](https://i.imgur.com/FcMKY9H.png)

<sup><sub>Xc0n Client is discontinued due to market oversaturation</sub></sup>

*"A whale can speak to another whale across sixty miles of ocean"* ... how does he do it? 🐳

Lightweight event system, shipped as individual open-source project for aspiring client-developers to use.

S0nar can be used as a base event system in any environment where event-injection is of use or a necessity.


## How to use
Add the package hierarchy to your project and instantiate a single instance of EventBus, preferably in your main Mod class (if developing a Minecraft mod).

```java
public static final EventBus EVENT_BUS = new EventBus();
```
 or if you do not plan on using priority staging
 ```java
public static final EventBus EVENT_BUS = new EventBus(false);
```

### Creating Events
Its as simple as extending ``Event`` for single-state events or ``CancelableEvent`` for dual-state events. If more intrinsic event types are needed, extending either of both base classes shipped with *s0nar* will be valid.

**Example event:**
```java
public class ExampleEvent extends CancelableEvent 
{
	private String value;
	
	public ExampleEvent(String val)
	{
		value = val;
	}
	
	public String getValue() { return value; }
}
```
### Posting Events
In order to post events from inside a source block, inject a call to `EventBus#post(Event event)` using whatever method you are most comfortable with. In a Minecraft context, Mixins have gained increasing popularity, however ASM is a valid option, too. Of course with the appearance of MCP Reborn you might want to choose just to edit source directly (which you should not lol).

Example 1:
```java
//In this case GenericEvent extends Event, Main is where you instantiated the event bus
Main.EVENT_BUS.post(new GenericEvent());
```
Example 2:
```java
//WalkEvent extends CancelableEvent and if setCanceled(true) anywhere in the event raise chain, the resulting event instance will cause a return.
WalkEvent event = (WalkEvent) Main.EVENT_BUS.post(new WalkEvent());
if(event.isCanceled())
{
	return; 
}
```
### Registering Event Listeners
In order to register event listeners, simply annotate subscriber methods with the `S0narEventListener` @ interface and register the object of the owner class to the `EventBus`. If you wish to use priority staging, you can optionally supply the @ interface with a `priority` parameter, which takes a value from `EventPriority` enum. Do not forget to unregister your subscriber object from the `EventBus` at destruction.

**Note: Your event listener methods may not have more than one parameter, that being the event.** They should never need additional parameters anyways. Additional parsing data should probably be wrapped as event class attributes.

```java
public class Subscriber
{
	//example call to register the subscriber object to the event bus	
	public void enable()
	{
		Main.EVENT_BUS.register(this);
	}
	//example call to unregister the subscriber object from the event bus
	public void disable()
	{
		Main.EVENT_BUS.unregister(this);
	}
	
	@S0narEventListener(priority = EventPriority.HIGH)
	public void onWalkEvent(WalkEvent event)
	{
		if(/*condition*/)
		{
			event.setCanceled(true);
		}
	}
}
```
## Event Staging
S0nar gives you full flexibility over the execution order of your event listeners. Using the `priority` argument of the `S0narEventListener` annotation, you can specify the execution priority preference. Listeners assigned a higher priority will be executed first. Listeners within the same priority segment will be executed in order of registration. *Order of same-priority listeners is not guaranteed.*

**If no priority argument is specified on decorated methods, the default fallback is `EventPriority.LOW`.**

The `EventPriority` enum that is used to represent the `priority` value has 4 pre-configured priority-segments
```
TOP
HIGH
MEDIUM
LOW
```
but can easily be expanded at will, as internally only the index values are used to determine execution order. 

