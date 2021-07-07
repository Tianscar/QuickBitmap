# QuickBitmap

A simple android-bitmap toolkit.

# To get a Git project into your build (gradle):

* Step 1. Add the JitPack repository to your build file<br/>
Add it in your root build.gradle at the end of repositories:<br/>
```
allprojects {
        repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

* Step 2. Add the dependency:<br/>
```
dependencies {
	...
	implementation 'com.github.tianscar:quickbitmap:1.0.0'
}
```
