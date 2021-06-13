package net.x0ti.s0nar;

/**
 * Base class for cancelable events. If you wish to cancel execution of event-code, set setCanceled() true in code and handle EventBus#post() return value for isCanceled().
 * @author 0ti
 */
public abstract class CancelableEvent extends Event
{
	
	protected boolean canceled = false;
	
	public void setCanceled(boolean val)
	{
		canceled = val;
	}
	
	public boolean isCanceled()
	{
		return canceled;
	}
}
