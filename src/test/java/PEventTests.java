import dev.angle.jevent.event.PEvent;
import dev.angle.jevent.exception.UninvokablePEventException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PEventTests {

	@Getter
	@Setter
	@AllArgsConstructor
	private static class Container<T> {
		private T value;
	}

	private PEvent<Integer> event;
	private String actual;
	private Container<String> actualSender;

	@BeforeEach
	void setup() {
		event = new PEvent<>();
	}

	@Test
	void when_callbackIsAdded_thenInvokingRunsIt() {
		final String expected = "Event called lambda (1)";
		final Container<String> actual = new Container<>("");

		final BiConsumer<Object, Integer> callback = (sender, num) -> {
			actual.setValue("Event called lambda (%d)".formatted(num));
		};

		event.subscribe(callback);

		event.invoke(this, 1);

		assertEquals(expected, actual.getValue());
	}

	@Test
	void when_callbackIsMethod_then_invokeCallsIt() {
		final String expected = "Event called method (1)";
		actual = "";
		event.subscribe(this::setActualToExpected);

		event.invoke(this, 1);

		assertEquals(expected, actual);
	}

	@Test
	void when_callingMultipleCallbackIsSubscribed_then_invokeCallsThemAll() {
		final String expected1 = "Changed1 (1)";
		final String expected2 = "Changed2 (1)";

		final Container<String> actual1 = new Container<>("Unchanged1");
		final Container<String> actual2 = new Container<>("Unchanged2");

		BiConsumer<Object, Integer> callback1 = (sender, num) -> {
			actual1.setValue("Changed1 (%d)".formatted(num));
		};

		BiConsumer<Object, Integer> callback2 = (sender, num) -> {
			actual2.setValue("Changed2 (%d)".formatted(num));
		};

		event.subscribe(callback1);
		event.subscribe(callback2);

		event.invoke(this, 1);

		assertEquals(expected1, actual1.getValue());
		assertEquals(expected2, actual2.getValue());
	}

	@Test
	void when_invokeIsCalled_senderIsTheCorrect() {
		actualSender = new Container<>("Test sender");
		final String expected = "Test sender";
		final Container<String> actual = new Container<>("");
		event.subscribe((sender, num) -> {
			actual.setValue(actualSender.getValue());
		});


		event.invoke(actualSender, 1);

		assertEquals(expected, actual.getValue());
	}

	@Test
	void when_unsubscribed_then_invokeWontCallAnymore() {
		final String expected = "Unchanged";
		final Container<String> actual = new Container<>("Unchanged");

		BiConsumer<Object, Integer> callback = (sender, num) -> {
			actual.setValue("Changed (%d)".formatted(1));
		};

		event.subscribe(callback);
		event.unsubscribe(callback);

		event.invoke(this, 1);

		assertEquals(expected, actual.getValue());
	}

	@Test
	void when_unsubscribedWithMethod_then_invokeWontCallAnymore() {
		final String expected = "Unchanged";
		final Container<String> actual = new Container<>("Unchanged");

		event.subscribe(this::setActualToChanged);
		event.unsubscribe(this::setActualToChanged);

		event.invoke(this, 1);

		assertEquals(expected, actual.getValue());
	}

	@Test
	void when_deletingFromMultipleCallbacks_then_onlyCorrectCallbackIsRemoved() {
		final String expected1 = "Changed1 (1)";
		final String expected2 = "Unchanged2";

		final Container<String> actual1 = new Container<>("Unchanged1");
		final Container<String> actual2 = new Container<>("Unchanged2");

		BiConsumer<Object, Integer> callback1 = (sender, num) -> {
			actual1.setValue("Changed1 (%d)".formatted(num));
		};

		BiConsumer<Object, Integer> callback2 = (sender, num) -> {
			actual2.setValue("Changed2 (%d)".formatted(num));
		};

		event.subscribe(callback1);
		event.subscribe(callback2);
		event.unsubscribe(callback2);

		event.invoke(this, 1);

		assertEquals(expected1, actual1.getValue());
		assertEquals(expected2, actual2.getValue());
	}

	@Test
	void when_invokeCalledOnUninvokable_then_throwsException() {
		PEvent.UninvokableEvent<Integer> uninvokableEvent = event.getUninvokable();

		assertThrows(
				UninvokablePEventException.class,
				() -> uninvokableEvent.invoke(this, 1));
	}



	private void setActualToExpected(Object sender, Integer num) {
		actual = "Event called method (%d)".formatted(num);
	}
	private void setActualToChanged(Object sender, Integer num) {
		actual = "Changed (%d)".formatted(num);
	}
}
