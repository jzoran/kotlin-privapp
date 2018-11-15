# Kotlin+Jetpack in an Android privileged app

## Introduction
Google announced Kotlin as first class citizen on Android in Google I/O 2017 and Jetpack
set of libraries in Google I/O 2018. Since then they've made great progress in providing
support for Kotlin and Jetpack for application developers.

With the release of Android Pie (9.0.0) the support for Kotlin and Jetpack has extended
to system applications. It is, though, still work in progress.

## Build
The support for Kotlin and support libraries was tentatively present on the platform since
their introduction and full support for 3rd party developers including tools and
documentation was there from day one, however in order to make everything work with the
platform build, additional work was required by the developer and in some cases things were
simply missing.

Now, Soong build tool provides support for Kotlin and most Jetpack libraries. To add Kotlin
support all you have to do is list .kt files in 'srcs' element of your .bp file and for
Jetpack add appropriate library to a library section, e.g. 'static_libs'.

### NOTE:
The build support is available for Soong only. Make is not supported.

### NOTE:
Naming of libraries in .bp files DOES NOT fully comply to the usual Maven nomenclature you
would use in a gradle file e.g. in Android Studio. The difference is that Soong uses '_' as
separator instead of ':' and also version is not stated in the blueprint file - you will
always get the latest platform build of the support library, e.g.

```
  androidx.core:core:1.0.0-rc01 -> androidx.core_core
```

## Version

### Kotlin
As of this writing the version of kotlin used in AOSP is 1.3.0.

### Jetpack
* As of this writing the version of Androidx libraries is 1.0.0-alpha1, however with build you will
always get the latest version available so bear that in mind when syncing your repo and building the
application.
* Unfortunately, not all Jetpack libraries are supported out of box, only those that are part of sdk
build. Notably, Kotlin extensions are missing.

## Known issues
(...as of this writing, see date in the commit.)

### Kotlinc flags
Soong invokes kotlinc for compilation, so it would be useful to add certain flags,
e.g. -Werror, -P plugin..., -Xcoroutines=... etc., however this is not supported out of
box (the reason probably being so that it doesn't conflict with flags already added by Soong).

The following patch in AOSP:
https://android-review.googlesource.com/c/platform/build/soong/+/735669
adds support for kotlinc flags. See [Android.bp](Android.bp)
file for usage.

### Library support
* Kotlinc plugins not supported, e.g. kapt and Kotlin Android extensions plugin
* Kotlin extensions libraries from Jetpack are not supported, as they are not part of the
sdk build of Jetpack.

## LICENSE
The project is licensed under the license stated in the [LICENCE.md](LICENSE.md) file of
this project.

## Contributions
As much as I'm surprised that you would want to contribute to this small project, you are
more than welcome to do so by submitting issues and PRs or sending patch files. Thanks!

