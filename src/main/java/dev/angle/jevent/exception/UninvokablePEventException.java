package dev.angle.jevent.exception;

import dev.angle.jevent.event.PEvent;
import lombok.Getter;

public class UninvokablePEventException extends RuntimeException {
	@Getter
	private final PEvent<?> event;
	@Getter
	private final Object sender;

	public UninvokablePEventException(String message, PEvent<?> event, Object sender) {
		super(message);
		this.event = event;
		this.sender = sender;
	}
}
