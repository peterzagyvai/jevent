# JEVENT

It's a C# like event system for java. You can:

- Create
- Subscribe to
- Unsubscribe from
- Invoke

...events.

## Parameterized / Parameterless
There are two systems that are **incompatible** with each other by design.
One of them is the parameterless classes (`Callback`, `Event`, `Publisher`) and parameterized (`ParameterizedCallback<Args>`, `ParameterizedEvent<Args>`, `ParameterizedPublisher<Args>`) which takes an argument.

## [Parameterized]Publisher
These interfaces enforce you to return an `UninvokableEvent` to other objects with the `getEvent()` method.
These events can't be invoked instead they will throw an exception so other objects can't invoke it.

## Event / Callback
These classes make possible to invoke and run the methods (`Callback`) that were subscribed to the event.
First you have to subscribe to the `Event` with a `Callback`
```java
      Event event = new Event();
      Callback callback = Callback.create(() -> { 
        /* Whatever you need */
        System.out.println("Hello JEVENT");
      });
      event.subscribe(callback);
```
The subscribe can happen in other classes so it's recommended to get the event with the `Publisher`'s `getEvent()` method.
After you subscribed the event can be invoked (unlike in C# you don't need to check if the event is null because on initialization it will have an empty list of the callbacks).
If you call the `invoke()` on an UninvokableEvent then it will throw an exception. 
```java
    event.invoke(); // Prints "Hello JEVENT"
```
Also if you don't need to invoke your method you can also unsubscribe from the event with the `unsubscribe()` method.
```java
    event.unsubscribe(callback);
```

## Parameterized versions
Very similar to the parameterless version you can invoke, subscribe unsubscribe to events but you can also pass parameters to the invoked methods.

```java
    ParameterizedEvent<String> event = new ParameterizedEvent<>();
    ParamterizedCallback<String> callback = ParameterizedCallback.create(messge -> {
        System.out.printf("Message in callback: %s\n", message);		
    });
		
    ...

    event.invoke("Hello JEVENT"); // Prints: "Message in callback: Hello JEVENT"
```


