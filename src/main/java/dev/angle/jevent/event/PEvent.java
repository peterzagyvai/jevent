package dev.angle.jevent.event;

import dev.angle.jevent.exception.UninvokablePEventException;
import dev.angle.jevent.invokable.InvokablePEvent;
import dev.angle.jevent.listener.PEventListener;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * <p>Callback methods ({@code BiConsumer<Object, Args>}) can subscribe and unsubscribe to this event.</p>
 * <p>Subscribed callbacks will be executed when the {@code invoke()} method is called</p>
 * @param <Args> the type of the arguments
 */
public sealed class PEvent<Args> implements PEventListener<Args>, InvokablePEvent<Args> permits PEvent.UninvokableEvent {
	@RequiredArgsConstructor
	public static final class UninvokableEvent<Args> extends PEvent<Args> {
		private final PEvent<Args> original;

		@Override
		public void unsubscribe(BiConsumer<Object, Args> callback) {
			original.unsubscribe(callback);
		}

		@Override
		public void subscribe(BiConsumer<Object, Args> callback) {
			original.subscribe(callback);
		}

		/**
		 * Throws an {@code UninvokableEventException} when called
		 * @throws UninvokablePEventException when called
		 */
		@Override
		public void invoke(Object sender, Args args) throws UninvokablePEventException {
			throw new UninvokablePEventException("Invoked uninvokable event", this, sender);
		}
	}

	private final Set<BiConsumer<Object, Args>> callbacks = new HashSet<>();

	/**
	 * Will call every subscribed event and pass the {@code sender} as parameter.
	 * @param sender The object that calls the invoke event
	 * @param args the arguments passed to the callbacks
	 */
	public void invoke(Object sender, Args args) {
		callbacks.forEach(callback -> callback.accept(sender, args));
	}

	/**
	 * Subscribed callbacks will be executed when {@code this} event is invoked.
	 * @param callback the callback that will be subscribed
	 */
	public void subscribe(BiConsumer<Object, Args> callback) {
		callbacks.add(callback);
	}

	/**
	 * Unsubscribes an already subscribed {@code Callback}
	 * @param callback will be unsubscribed;
	 */
	public void unsubscribe(BiConsumer<Object, Args> callback) {
		callbacks.remove(callback);
	}

	/**
	 * Returns an Uninvokable  equivalent of {@code this} event;
	 * @return Uninvokable {@code EventListener} equivalent of {@code this} event
	 */
	public PEventListener<Args> getEventListener() {
		return new UninvokableEvent<>(this);
	}

	/**
	 * Returns an {@code UninvokableEvent} that can be used to subscribe to or unsubscribe
	 * <p>Deprecated! Use {@code getEventListener()}</p>
	 * @return {@code UninvokableEvent} object
	 */
	@Deprecated(since = "v1.2.0")
	public final PEvent.UninvokableEvent<Args> getUninvokable() {
		return new PEvent.UninvokableEvent<>(this);
	}

}
