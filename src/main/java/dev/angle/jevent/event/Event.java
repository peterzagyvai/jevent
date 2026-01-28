package dev.angle.jevent.event;


import dev.angle.jevent.exception.UninvokableEventException;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * <p>Callback methods can subscribe and unsubscribe to an {@code Event} object</p>
 * <p>Subscribed callbacks will be executed when the {@code invoke()} method is called</p>
 */
public sealed class Event permits Event.UninvokableEvent {

	/**
	 * Uninvokable event's throw an {@code UninvokableEventException} when the {@code invoke()} is called
	 */
	@RequiredArgsConstructor
	public static final class UninvokableEvent extends Event {
		private final Event original;

		@Override
		public void unsubscribe(Consumer<Object> callback) {
			original.unsubscribe(callback);
		}

		@Override
		public void subscribe(Consumer<Object> callback) {
			original.subscribe(callback);
		}

		@Override
		public void invoke(Object sender) {
			throw new UninvokableEventException("Invoked uninvokable event", this, sender);
		}
	}

	private final Set<Consumer<Object>> callbacks = new HashSet<>();

	/**
	 * Will call every subscribed event and pass the {@code sender} as parameter.
	 * @param sender The object that calls the invoke event
	 */
	public void invoke(Object sender) {
		callbacks.forEach(callback -> callback.accept(sender));
	}

	/**
	 * Subscribed callbacks will be executed when {@code this} event is invoked.
	 * @param callback the callback that will be subscribed
	 */
	public void subscribe(Consumer<Object> callback) {
		callbacks.add(callback);
	}

	/**
	 * Unsubscribes an already subscribed {@code Callback}
	 * @param callback will be unsubscribed;
	 */
	public void unsubscribe(Consumer<Object> callback) {
		callbacks.remove(callback);
	}

	/**
	 * Returns an {@code UninvokableEvent} that can be used to subscribe to or unsubscribe
	 * @return {@code UninvokableEvent} object
	 */
	public final Event.UninvokableEvent getUninvokable() {
		return new UninvokableEvent(this);
	}

}
