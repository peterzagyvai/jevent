package dev.angle.jevent.invokable;

public interface InvokablePEvent<Args> {

	/**
	 * Will call every subscribed event and pass the {@code sender} as parameter.
	 * @param sender The object that calls the invoke event
	 * @param args the arguments passed to the callbacks
	 */
	void invoke(Object sender, Args args);
}
