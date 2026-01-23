package dev.angle.jevent.callback;

import java.util.function.Consumer;

public final class SingletonCallbackFactory {
	public static final class CallbackImpl extends Callback {
		private CallbackImpl(long id, Runnable runnable) {
			super(id, runnable);
		}
	}

	public static final class ParameterizedCallbackImpl<Args> extends ParameterizedCallback<Args> {
		private ParameterizedCallbackImpl(long id, Consumer<Args> consumer) {
			super(id, consumer);
		}
	}

	private SingletonCallbackFactory() {}

	private static final long INITIAL_ID = 1L;

	private static volatile SingletonCallbackFactory instance;
	private static long nextId;

	public static SingletonCallbackFactory getInstance() {
		if (instance == null){
			synchronized (SingletonCallbackFactory.class) {
				if (instance == null) {
					nextId = INITIAL_ID;
					instance = new SingletonCallbackFactory();
				}
			}
		}

		return instance;
	}

	public synchronized Callback create(final Runnable runnable) {
		final long id = nextId++;
		return new CallbackImpl(id, runnable);
	}

	public synchronized <Args> ParameterizedCallback<Args> create(Consumer<Args> consumer) {
		final long id = nextId++;
		return new ParameterizedCallbackImpl<Args>(id, consumer);
	}
}
