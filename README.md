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

# LICENSE
MIT License

Copyright (c) 2021 Tianscar

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
