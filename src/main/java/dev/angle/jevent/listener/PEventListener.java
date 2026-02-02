package dev.angle.jevent.listener;

import java.util.function.BiConsumer;

public interface PEventListener<Args> {

	/**
	 * Subscribed callbacks will be executed when {@code this} event is invoked.
	 * @param callback the callback that will be subscribed
	 */
	void subscribe(BiConsumer<Object, Args> callback);

	/**
	 * Unsubscribes an already subscribed {@code Callback}
	 * @param callback will be unsubscribed;
	 */
	void unsubscribe(BiConsumer<Object, Args> callback);
}
