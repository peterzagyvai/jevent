package dev.angle.jevent.exception;

import dev.angle.jevent.event.ParameterizedEvent;
import lombok.Getter;

public class InvokedUninvokableParameterizedException extends RuntimeException {
	@Getter
	private final ParameterizedEvent<?> event;

	public InvokedUninvokableParameterizedException(String message, ParameterizedEvent<?> event) {
		super(message);
		this.event = event;
	}
}
