package dev.angle.jevent.event;

import dev.angle.jevent.callback.ParameterizedCallback;
import dev.angle.jevent.exception.InvokedUninvokableParameterizedException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public sealed class ParameterizedEvent<Args> permits ParameterizedEvent.UninvokableEvent {
	@RequiredArgsConstructor
	public static final class UninvokableEvent<Args> extends ParameterizedEvent<Args> {
		private final ParameterizedEvent<Args> original;

		@Override
		public void unsubscribe(ParameterizedCallback<Args> callback) {
			original.unsubscribe(callback);
		}

		@Override
		public void subscribe(ParameterizedCallback<Args> callback) {
			original.subscribe(callback);
		}

		@Override
		public void invoke(Args args) {
			throw new InvokedUninvokableParameterizedException("Invoked uninvokable event", this);
		}
	}

	private final List<ParameterizedCallback<Args>> callbacks = new ArrayList<>();

	public void invoke(Args args) {
		callbacks.forEach(callback -> {
				callback.run(args);
			}
		);
	}

	public void subscribe(ParameterizedCallback<Args> callback) {
		callbacks.add(callback);
	}

	public void unsubscribe(ParameterizedCallback<Args> callback) {
		callbacks.remove(callback);
	}

	public final ParameterizedEvent.UninvokableEvent<Args> getUninvokable() {
		return new ParameterizedEvent.UninvokableEvent<>(this);
	}

}
