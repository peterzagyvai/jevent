package dev.angle.jevent.callback;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public abstract sealed class Callback permits SingletonCallbackFactory.CallbackImpl {
	private final long id;
	private final Runnable callback;

	public void run() {
		callback.run();
	}

	public static Callback create(Runnable runnable) {
		return SingletonCallbackFactory.getInstance().create(runnable);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Callback callback1 = (Callback) o;
		return id == callback1.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
