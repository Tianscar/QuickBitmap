# QuickBitmap

**An android-bitmap toolkit provides some useful functions e.g.**
* **BitmapPool**<br/>
Based on LruCache and DiskLruCache (https://github.com/JakeWharton/DiskLruCache).
* **BitmapEncoder/Decoder**<br/>
Supports PNG, JPEG, BMP, WEBP.
* **BitmapChanger**<br/>
Can clip, zoom, flip, rotate, seed filling bitmap and so on.
Seed filling function using C/C++ to achieve, efficiency is guaranteed.

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

# License
[MIT](https://github.com/Tianscar/QuickBitmap/blob/master/LICENSE) © Tianscar
