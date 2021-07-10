# QuickBitmap
[**文档**](https://tianscar.github.io/QuickBitmap)

**一个简单的安卓Bitmap工具包，包含一些基础功能：**
* **BitmapPool**<br/>
用于Bitmap的二级缓存池，基于LruCache和[DiskLruCache](https://github.com/JakeWharton/DiskLruCache)。
* **BitmapEncoder/Decoder**<br/>
Bitmap编/解码器，支持PNG、JPEG、BMP、WEBP格式。
* **BitmapChanger**<br/>
Bitmap转换器，可以对Bitmap进行裁剪、缩放、翻转、旋转、填充等操作。<br/>
种子填充算法使用C/C++实现，性能得到保证。

# 在项目中添加Git依赖 (gradle)：

* 第一步：将JitPack储存库添加到构建文件中<br/>
将下图链接添加到项目根目录build.gradle储存库列表的末尾：<br/>
```
allprojects {
        repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

* 第二步：添加依赖：<br/>
```
dependencies {
	...
	implementation 'com.github.tianscar:quickbitmap:1.0.2a'
}
```

# 许可证
[MIT](https://github.com/Tianscar/QuickBitmap/blob/master/LICENSE) © Tianscar
