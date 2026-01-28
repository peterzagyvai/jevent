# JEVENT

It's a C# like event system for java. You can:

- Create
- Subscribe to
- Unsubscribe from
- Invoke

...events.

### Event / P(arameterized)Event
These classes make it possible to `.subscribe()` with methods or with functional interfaces.
After subscribing your event will run these methods when the `.invoke()` method is called.
As parameters, it takes the sender `Object` and a generic `Args`.
After unsubscribing, the invoke method won't call the callback.

### UninvokableEvents
In C# your events can't be invoked from other classes but with the simple `Event` and `PEvent` classes it's still possible.
To pass your event to other you should use the `.getUninvokalbe()` method which throws an `Uninvokable(P)EventException` when invoked.

```java
class EventProvider {
    private final Event event = new Event();

    public Event.UninvokableEvent getEvent() {
        return event.getUninvokable();
    }
}

class OtherClass {
    private final EventProvider eventProvider = new EventProvider();
	
    ...
  
    eventProvider.getEvent().invoke();  // Throws execption;
}
```

#### Event example
```java
    Event event = new Event();

    Consumer<Object> callback = sender -> {
	  System.out.println("Callback invoked");		
    };
		
    event.subscribe(callback);
		
    event.invoke(this); // Prints: "Callback invoked"
```

```java
    private void onEventCalled(Object sender) {
      System.out.println("Method invoked");
    }
		
    ...

    Event event = new Event();

    event.subscribe(this::onEventCalled);
		
    event.invoke(null); // Prints: "Method invoked"
```

#### PEvent example

```java
    PEvent<String> event = new PEvent<>();
		
    BiConsumer<Object, String> callback = (sender, args) -> {
      System.out.printf("Hello %s", args);
    };
		
    event.subscribe(callback);
		
    event.invoke(this, "PEvent"); // Prints: "Hello PEvent"
```

```java
  import dev.angle.jevent.event.PEvent;
  
  public static final class CustomEventArgs {
		public int num1;
		public int num2;
		
		// Getters
  }

  private int sum;
  private void onAddEvent(Object sender, CustomEventArgs args) {
      sum = args.getNum1() + args.getNum2();
  }

  ...

  PEvent<CustomEventArgs> addEvent = new PEvent<>();

  addEvent.subscribe(this::onAddEvent);

  addEvent.invoke(
        null,
        new CustomEventArgs(10,20)
  );

  System.out.printf("Sum: %d\n", sum); // Prints: "Sum: 30"
```


#### Unsubscribe
The `.unsubscribe()` method takes a method or a functional interface as a reference so it's necessary to have that reference.
Passing a lambda for subscribing will work, but you won't be able to unsubscribe from the event.
```java
  Event event = new Event();

  event.subscribe(sender -> { /* Do something */ });
  event.unsubscribe(sender -> { /* Same somehting */ }); // WILL NOT WORK!!!
```

