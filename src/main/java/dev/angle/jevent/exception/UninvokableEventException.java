package dev.angle.jevent.exception;

import dev.angle.jevent.event.Event;
import lombok.Getter;

public class UninvokableEventException extends RuntimeException {
	@Getter
	private final Event uninvokable;

	@Getter
	private final Object sender;

	public UninvokableEventException(String message, Event uninvokable, Object sender) {
		super(message);
		this.uninvokable = uninvokable;
		this.sender = sender;
	}
}
