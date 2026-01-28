package dev.angle.jevent;

import dev.angle.jevent.event.PEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Main {

	@Getter
	@RequiredArgsConstructor
	private static final class CustomEventArgs {
		private final int num1;
		private final int num2;
	}

	private static int sum;
	private static void onAddEvent(Object sender, CustomEventArgs args) {
		sum	= args.getNum1() + args.getNum2();
	}

	public static void main(String[] args) {
		PEvent<CustomEventArgs> addEvent = new PEvent<>();

		addEvent.subscribe(Main::onAddEvent);

		addEvent.invoke(
				null,
				new CustomEventArgs(10,20)
		);

		System.out.printf("Sum: %d\n", sum);
	}


}
