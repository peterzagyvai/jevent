import dev.angle.jevent.event.Event;
import dev.angle.jevent.exception.UninvokableEventException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EventTests {

	@Getter
	@Setter
	@AllArgsConstructor
	private static class Container<T> {
		private T value;
	}

	private Event event;
	private String actual;
	private Container<String> actualSender;

	@BeforeEach
	void setup() {
		event = new Event();
	}

	@Test
	void when_callbackIsAdded_thenInvokingRunsIt() {
		final String expected = "Event called lambda";
		final Container<String> actual = new Container<>("");

		final Consumer<Object> callback = sender -> {
			actual.setValue("Event called lambda");
		};

		event.subscribe(callback);

		event.invoke(this);

		assertEquals(expected, actual.getValue());
	}

	@Test
	void when_callbackIsMethod_then_invokeCallsIt() {
		final String expected = "Event called method";
		actual = "";
		event.subscribe(this::setActualToExpected);

		event.invoke(this);

		assertEquals(expected, actual);
	}

	@Test
	void when_callingMultipleCallbackIsSubscribed_then_invokeCallsThemAll() {
		final String expected1 = "Changed1";
		final String expected2 = "Changed2";

		final Container<String> actual1 = new Container<>("Unchanged1");
		final Container<String> actual2 = new Container<>("Unchanged2");

		Consumer<Object> callback1 = sender -> {
			actual1.setValue("Changed1");
		};

		Consumer<Object> callback2 = sender -> {
			actual2.setValue("Changed2");
		};

		event.subscribe(callback1);
		event.subscribe(callback2);

		event.invoke(this);

		assertEquals(expected1, actual1.getValue());
		assertEquals(expected2, actual2.getValue());
	}

	@Test
	void when_invokeIsCalled_senderIsTheCorrect() {
		actualSender = new Container<>("Test sender");
		final String expected = "Test sender";
		final Container<String> actual = new Container<>("");
		event.subscribe(sender -> {
			actual.setValue(actualSender.getValue());
		});


		event.invoke(actualSender);

		assertEquals(expected, actual.getValue());
	}

	@Test
	void when_unsubscribed_then_invokeWontCallAnymore() {
		final String expected = "Unchanged";
		final Container<String> actual = new Container<>("Unchanged");

		Consumer<Object> callback = sender -> {
			actual.setValue("Changed");
		};

		event.subscribe(callback);
		event.unsubscribe(callback);

		event.invoke(this);

		assertEquals(expected, actual.getValue());
	}

	@Test
	void when_unsubscribedWithMethod_then_invokeWontCallAnymore() {
		final String expected = "Unchanged";
		final Container<String> actual = new Container<>("Unchanged");

		event.subscribe(this::setActualToChanged);
		event.unsubscribe(this::setActualToChanged);

		event.invoke(this);

		assertEquals(expected, actual.getValue());
	}

	@Test
	void when_deletingFromMultipleCallbacks_then_onlyCorrectCallbackIsRemoved() {
		final String expected1 = "Changed1";
		final String expected2 = "Unchanged2";

		final Container<String> actual1 = new Container<>("Unchanged1");
		final Container<String> actual2 = new Container<>("Unchanged2");

		Consumer<Object> callback1 = sender -> {
			actual1.setValue("Changed1");
		};

		Consumer<Object> callback2 = sender -> {
			actual2.setValue("Changed2");
		};

		event.subscribe(callback1);
		event.subscribe(callback2);
		event.unsubscribe(callback2);

		event.invoke(this);

		assertEquals(expected1, actual1.getValue());
		assertEquals(expected2, actual2.getValue());
	}

	@Test
	void when_invokeCalledOnUninvokable_then_throwsException() {
		Event.UninvokableEvent uninvokableEvent = event.getUninvokable();

		assertThrows(
				UninvokableEventException.class,
				() -> uninvokableEvent.invoke(this));
	}



	private void setActualToExpected(Object sender) {
		actual = "Event called method";
	}
	private void setActualToChanged(Object sender) {
		actual = "Changed";
	}
}
