package dev.angle.jevent.publisher;

import dev.angle.jevent.event.Event;

public interface Publisher {
	/**
	 * Returns an uninvokable event
	 * @return an uninvokable event;
	 */
	Event.UninvokableEvent getEvent();
}
