package dev.angle.jevent.callback;

import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract sealed class ParameterizedCallback<Args> permits SingletonCallbackFactory.ParameterizedCallbackImpl {
	private final long id;
	private final Consumer<Args> callback;

	public void run(Args args) {
		callback.accept(args);
	}

	public static <Args> ParameterizedCallback<Args> create(Consumer<Args> consumer) {
		return SingletonCallbackFactory.getInstance().create(consumer);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		ParameterizedCallback<?> that = (ParameterizedCallback<?>) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
