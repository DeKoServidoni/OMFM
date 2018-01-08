# OneMoreFabMenu

Another floating action button menu with expand/collapse behavior.

 [ ![Download](https://api.bintray.com/packages/dekoservidoni/AndroidLibs/OMFM/images/download.svg) ](https://bintray.com/dekoservidoni/AndroidLibs/OMFM/_latestVersion)
 
 [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-OneMoreFabMenu-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/6280)

![Example gif](/images/example_v1.0.3.gif) 

## How to use

This library have 1 main layout param and 6 optionals

* `<app:content_options>` **[Required]** "menu" resource file with the options that the menu will show<br>
* `<app:color_main_button>` **[optional]** color of the main button (with + and x)<br>
* `<app:color_secondary_buttons>` **[optional]** color of the other options buttons<br>
* `<app:expanded_background_color>` **[optional]** color of the background when the component expands<br>
* `<app:rotate_main_button>` **[optional, *default=true*]** enable/disable the rotation of main button<br>
* `<app:size_main_button="@integer/omfm_fab_size_normal">` **[optional]** size of the main button<br>
* `<app:size_secondary_buttons="@integer/omfm_fab_size_mini">` **[optional]** size of the secondary buttons<br>
* `<app:close_on_click="true">` **[optional, *default=false*]** flag to enable/disable the close menu when some option is clicked<br>

###### Example

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
        app:rotate_main_button="true"
        app:size_main_button="@integer/omfm_fab_size_normal"
        app:size_secondary_buttons="@integer/omfm_fab_size_mini" />
```

#### Menu resource example

The menu is structure from top to bottom, for example, the first one is the main button
and the others will be the first option, second option and etc.

The first item don't need to have a text because only the options have labels.

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- First button is the initial Fab of the menu -->
    <!-- Don't need the title in this case, so let it empty -->
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
compile 'com.github.dekoservidoni:omfm:1.0.3'
```

## Licence

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
