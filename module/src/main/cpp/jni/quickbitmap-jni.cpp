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

#include "com_tianscar_quickbitmap_NativeMethods.h"
#include <android/bitmap.h>
#include "fill.h"

uint32_t jint2uint(jint value) {
    if (value >= 0) {
        return value;
    }
    else {
        return 4294967296 + value;
    }
}

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_tianscar_quickbitmap_NativeMethods_nativeFill
        (JNIEnv *env, jclass clazz,
         jobject bitmap, jint x, jint y, jint color) {
    AndroidBitmapInfo info;
    int result;
    result = AndroidBitmap_getInfo(env, bitmap, &info);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        return;
    }
    uint32_t bitmap_width = info.width;
    uint32_t bitmap_height = info.height;
    int32_t bitmap_format = info.format;
    void *addr_ptr;
    result = AndroidBitmap_lockPixels(env, bitmap, &addr_ptr);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        AndroidBitmap_unlockPixels(env, bitmap);
        return;
    }
    switch (bitmap_format) {
        case ANDROID_BITMAP_FORMAT_A_8:
            fill((u_char *) addr_ptr, x, y, argb_alpha(jint2uint(color)), bitmap_width, bitmap_height);
            break;
        case ANDROID_BITMAP_FORMAT_RGBA_8888:
            fill((uint32_t *) addr_ptr, x, y, argb2abgr(jint2uint(color)), bitmap_width, bitmap_height);
            break;
        case ANDROID_BITMAP_FORMAT_RGB_565:
            fill((uint16_t *) addr_ptr, x, y, argb2rgb565(jint2uint(color)), bitmap_width, bitmap_height);
            break;
        default:
            break;
    }
    AndroidBitmap_unlockPixels(env, bitmap);
}

JNIEXPORT void JNICALL Java_com_tianscar_quickbitmap_NativeMethods_nativeCompressBMP
        (JNIEnv *env, jclass clazz,
         jobject jBitmap, jobject stream) {

}

#ifdef __cplusplus
}
#endif