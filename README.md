# OneMoreFabMenu

Another floating action button menu with expand/collapse behavior.

 [ ![Download](https://api.bintray.com/packages/dekoservidoni/AndroidLibs/OMFM/images/download.svg) ](https://bintray.com/dekoservidoni/AndroidLibs/OMFM/_latestVersion)

### Collapsed
![Collapsed screen] (collapsed.png)

### Expanded
![Expanded screen] (expanded.png)

## How to use

This library have 1 main layout param and 3 optionals

`<app:content_options>` that need to receive a "menu" resource file with the options that the menu will show
`<app:color_main_button>` color of the main button (with + and x)<br>
`<app:color_secondary_buttons>` color of the other options buttons<br>
`<app:expanded_background_color>` color of the background when the component expands

```kotlin
    <com.dekoservidoni.omfm.OneMoreFabMenu
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        app:content_options="@menu/omfm_content_options"
        app:color_main_button="@color/colorPrimaryDark"
        app:color_secondary_buttons="@color/colorPrimary"
        app:expanded_background_color="@color/omfm_expanded_background_sample"/>
```

## Integrating with Gradle

To integrate with your project, just add the following line to your app `<build.gradle>` file

```kotlin
compile 'com.github.dekoservidoni:omfm:1.0.0'
```
