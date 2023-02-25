# Compose Time Picker
A simple light-weight and customizable `material3` time picker library for jetpack compose.

As of jetpack compose material3 version 1.1.0, a time picker does not yet exist.
This libary aims to add it until the official material time picker is released by google.

# Instructions
The library is published to the [jitpack.io](http://jitpack.io "jitpack.io") repository.
To use this library you must add jitpack to your project gradle repositories.

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
Then add the library to dependencies.

```gradle
dependencies {
    implementation 'com.github.AndreasLill:ComposeTimePicker:1.0.3'
}
```

[![](https://jitpack.io/v/AndreasLill/ComposeTimePicker.svg)](https://jitpack.io/#AndreasLill/ComposeTimePicker)

# Usage

Using the time picker is easy and can be further customized to suit your needs.

```kotlin
val dialogState = remember { mutableStateOf(false) }
val time = remember { mutableStateOf(LocalTime.now()) }

TimePickerDialog(
    state = dialogState,
    initialTime = time.value,
    colors = TimePickerDefaults.colors(
        // Use custom colors here.
    )
    onSelectTime = {
        time.value = it
    }
)

Button(onClick = {
    dialogState.value = true
}) {
    Text("Pick a time")
}
```
