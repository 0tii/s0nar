# S0nar Event System
![](https://i.imgur.com/0zO3m3E.png)

*"[Sonar] A whale can speak to another whale across sixty miles of ocean"*

Lightweight event system, developed as part of closed-source Minecraft client Xc0n. Shipped as separate open-source project for aspiring client-developers to use.

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
Its as simple as extending ``Event`` for single-state events or ``CancelableEvent`` for, you guessed it, cancelable events. If more intrinsic event types are needed, extending either of both base classes shipped with **s0nar** will be valid.

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



