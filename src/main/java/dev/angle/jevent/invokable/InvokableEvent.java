package dev.angle.jevent.invokable;

public interface InvokableEvent {

	/**
	 * Will call every subscribed event and pass the {@code sender} as parameter.
	 * @param sender The object that calls the invoke event
	 */
	void invoke(Object sender);
}
