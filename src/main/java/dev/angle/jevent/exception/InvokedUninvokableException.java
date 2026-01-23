package dev.angle.jevent.exception;

import dev.angle.jevent.event.Event;
import lombok.Getter;

public class InvokedUninvokableException extends RuntimeException {
	@Getter
	private final Event uninvokable;

	public InvokedUninvokableException(String message, Event uninvokable) {
		super(message);
		this.uninvokable = uninvokable;
	}
}
