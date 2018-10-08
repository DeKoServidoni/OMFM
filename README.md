# OneMoreFabMenu

Another floating action button menu with expand/collapse behavior.

 [ ![Download](https://api.bintray.com/packages/dekoservidoni/AndroidLibs/OMFM/images/download.svg) ](https://bintray.com/dekoservidoni/AndroidLibs/OMFM/_latestVersion)
 
 [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-OneMoreFabMenu-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/6280)

![Example gif](/images/example_v1.0.3.gif) 

## What's new:

### Version 1.1.1 (02/03/2018)
* [Fix] Bug fixes
  * Main button label shows with not text when text is “” (empty)

### Version 1.1.0 (01/25/2018)
* [Feature] Add parameter in the layout to background text label
* [Feature] Add parameter in the layout to color text label
* [Feature] Add parameter in the layout to add label to the main button
* [Feature] Add parameter in the layout to add action to the main button
* [Feature] Add parameter in the layout to add drawable to the main button when opened
* [Enhancement] Increase padding left/right of the label
* [Enhancement] Update sample to have different example images

## How to use

This library have 1 main layout param and 12 optionals

* `<app:content_options>` **[Required]** 
   * Resource "menu" file with the options that the menu will show<br>
* `<app:color_main_button>` **[optional]** 
   * Color of the main button<br>
* `<app:color_secondary_buttons>` **[optional]** 
   * Color of the other options buttons<br>
* `<app:expanded_background_color>` **[optional]** 
   * Color of the background when the component expands<br>
* `<app:rotate_main_button>` **[optional, *default=true*]** 
   * Flag to enable/disable the rotation of main button<br>
* `<app:size_main_button>` **[optional]** 
   * Size of the main button. Can use the already defined size: ***@integer/omfm_fab_size_normal***<br>
* `<app:size_secondary_buttons>` **[optional]** 
   * Size of the secondary buttons. Can use the already defined size: ***@integer/omfm_fab_size_mini***<br>
* `<app:close_on_click>` **[optional, *default=false*]** 
   * Flag to enable/disable the close menu when some option is clicked<br>
* `<app:label_background_color>` **[optional, *default=white*]** 
   * Background color of the label<br>
* `<app:label_background_drawable>` **[optional]** 
   * If you want a drawable instead of color in the label's background<br>
* `<app:label_text_color>` **[optional, *default=black*]** 
   * Color of the label's text<br>
* `<app:enable_main_as_action>` **[optional, *default=false*]** 
   * Flag to enable/disable the main button as an action when the menu is expanded<br>
* `<app:main_action_drawable>` **[optional, *default=icon defined in the content menu file*]** 
   * Drawable of the main button when it is set as an action.<br>

###### Example of usage

```xml
   <com.dekoservidoni.omfm.OneMoreFabMenu
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        app:content_options="@menu/omfm_content_options"
        app:close_on_click="true"
        app:color_main_button="@color/colorPrimaryDark"
        app:color_secondary_buttons="@color/colorPrimary"
        app:expanded_background_color="@color/omfm_expanded_background_sample"
        app:rotate_main_button="false"
        app:size_main_button="@integer/omfm_fab_size_normal"
        app:size_secondary_buttons="@integer/omfm_fab_size_mini"
        app:label_text_color="@color/colorPrimary"
        app:enable_main_as_action="true"
        app:main_action_drawable="@mipmap/ic_launcher"/>
```

#### Menu resource example

The menu is structure from top to bottom, for example, the first one is the main button
and the others will be the first option, second option and etc.

The first item don't need to have a text because only the options have labels.

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- First button is the initial Fab of the menu -->
    <!-- Title here can be empty if you don't to add any action in the main button -->
    <item
        android:id="@+id/main_option"
        android:icon="@drawable/ic_add_white_24px"
        android:title=""/>

    <!-- Options buttons of the Fab menu -->
    <item
        android:id="@+id/option1"
        android:icon="@drawable/ic_alarm_white_24px"
        android:title="@string/options_1" />

    <item
        android:id="@+id/option2"
        android:icon="@drawable/ic_alarm_white_24px"
        android:title="@string/options_2" />

    <item
        android:id="@+id/option3"
        android:icon="@drawable/ic_room_service_white_24px"
        android:title="@string/options_3" />

    <item
        android:id="@+id/option4"
        android:icon="@drawable/ic_room_service_white_24px"
        android:title="@string/options_4" />

</menu>
```

## Integrating with Gradle

To integrate with your project, just add the following line to your app `<build.gradle>` file

```java
compile 'com.github.dekoservidoni:omfm:1.1.1'
```

## Important!

Please make sure your project have Kotlin support:

Top-level build.gradle:
```java
classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.1.51"
```

Module build.gradle:
```java
apply plugin: 'kotlin-android' 
apply plugin: 'kotlin-android-extensions'
dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jre7:1.1.51"
```

## Licence

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
