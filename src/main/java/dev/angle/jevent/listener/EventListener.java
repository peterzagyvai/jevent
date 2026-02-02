package dev.angle.jevent.listener;

import java.util.function.Consumer;

public interface EventListener {

	/**
	 * Subscribed callbacks will be executed when {@code this} event is invoked.
	 * @param callback the callback that will be subscribed
	 */
	void subscribe(Consumer<Object> callback);

	/**
	 * Unsubscribes an already subscribed {@code Callback}
	 * @param callback will be unsubscribed;
	 */
	void unsubscribe(Consumer<Object> callback);
}
