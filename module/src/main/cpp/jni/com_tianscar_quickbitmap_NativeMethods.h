/*
 * MIT License
 *
 * Copyright (c) 2021 Tianscar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_tianscar_quickbitmap_NativeMethods */

#ifndef _Included_com_tianscar_quickbitmap_NativeMethods
#define _Included_com_tianscar_quickbitmap_NativeMethods
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_tianscar_quickbitmap_NativeMethods
 * Method:    nativeFill
 * Signature: (Landroid/graphics/Bitmap;III)V
 */
JNIEXPORT void JNICALL Java_com_tianscar_quickbitmap_NativeMethods_nativeFill
  (JNIEnv *, jclass clazz, jobject, jint, jint, jint);

/*
 * Class:     com_tianscar_quickbitmap_NativeMethods
 * Method:    nativeCompressBMP
 * Signature: (Landroid/graphics/Bitmap;Ljava/io/OutputStream;)V
 */
JNIEXPORT void JNICALL Java_com_tianscar_quickbitmap_NativeMethods_nativeCompressBMP
  (JNIEnv *, jclass clazz, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif