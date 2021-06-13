# S0nar Event System

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

