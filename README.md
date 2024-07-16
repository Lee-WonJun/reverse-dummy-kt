# Reverse Dummy Library

![reverse+dummy_out](https://github.com/user-attachments/assets/d5109a89-0dfe-454b-b8cb-18a2cfa3536b)


The `ReverseDummy` library is designed to help reverse data objects into their corresponding initialization code in Kotlin. This can be particularly useful for testing, debugging, or generating sample code for documentation.

## Key Features
- **Reverse Object Initialization**: Convert instances of data classes, lists, maps, and other objects into Kotlin initialization code.
- **Custom Matchers**: Extend the library with custom matchers for specific types.


## Usage

```kotlin
val dummy = Person("John", 25, true)
val reversed = reverseDummy(dummy, "reversed")
println(reversed) // val reversed = reversedummy.Person(name = "John", age = 25, isStudent = true)
```

### Custom Matchers
Extend the library with custom matchers for specific types:

```kotlin
val customMatcher = defaultCustomMatcher + mapOf(
    LocalDate::class to { date -> "LocalDate.parse(\"${date.toString()}\")" }
)

val myTime = MyTime(
    time = LocalDate.of(2019, 11, 1)
)

val dummy = reverseDummy(myTime, "DummyTime", matcher = customMatcher)
println(dummy) // val DummyTime = reversedummy.ReverseDummyKtTest2.MyTime(time = LocalDate.parse("2019-11-01"))
```
