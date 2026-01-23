package dev.angle.jevent.event;


import dev.angle.jevent.callback.Callback;
import dev.angle.jevent.exception.InvokedUninvokableException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public sealed class Event permits Event.UninvokableEvent {
	@RequiredArgsConstructor
	public static final class UninvokableEvent extends Event {
		private final Event original;

		@Override
		public void unsubscribe(Callback callback) {
			original.unsubscribe(callback);
		}

		@Override
		public void subscribe(Callback callback) {
			original.subscribe(callback);
		}

		@Override
		public void invoke() {
			throw new InvokedUninvokableException("Invoked uninvokable event", this);
		}
	}

	private final List<Callback> callbacks = new ArrayList<>();

	public void invoke() {
		callbacks.forEach(
				Callback::run
		);
	}

	public void subscribe(Callback callback) {
		callbacks.add(callback);
	}

	public void unsubscribe(Callback callback) {
		callbacks.remove(callback);
	}

	public final Event.UninvokableEvent getUninvokable() {
		return new UninvokableEvent(this);
	}

}
