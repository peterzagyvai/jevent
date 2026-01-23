package dev.angle.jevent.publisher;

import dev.angle.jevent.event.ParameterizedEvent;

public interface ParameterizedPublisher<Args> {
	ParameterizedEvent.UninvokableEvent<Args> getEvent();
}
