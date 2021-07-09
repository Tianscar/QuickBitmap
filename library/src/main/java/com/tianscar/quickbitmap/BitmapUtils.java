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

package com.tianscar.quickbitmap;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.BitmapCompat;

/**
 * A utility class providing functions about bitmap.
 */
public class BitmapUtils {

    private BitmapUtils (){}

    /**
     * Recycle bitmap.
     *
     * @param bitmap bitmap to recycle
     */
    public static void recycle(@Nullable Bitmap bitmap) {
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    /**
     * Recycle bitmap.
     *
     * @see BitmapUtils recycle(Bitmap)
     *
     * @param bitmaps bitmaps to recycle
     */
    public static void recycle(@Nullable Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bitmap : bitmaps) {
            recycle(bitmap);
        }
    }

    /**
     * If the bitmap is mutable, it will return it.
     * Otherwise, it returns the bitmap's mutable copy and recycle it.
     *
     * @param bitmap the bitmap in which to return its mutable copy and recycle
     * @return the mutable copy of the bitmap.
     */
    @NonNull
    public static Bitmap copyMutable(@NonNull Bitmap bitmap) {
        if (!bitmap.isMutable()) {
            Bitmap temp = bitmap;
            bitmap = bitmap.copy(bitmap.getConfig(), true);
            recycle(temp);
        }
        return bitmap;
    }

    public static boolean hasMipMap(@NonNull Bitmap bitmap) {
        return BitmapCompat.hasMipMap(bitmap);
    }

    public static void setHasMipMap(@NonNull Bitmap bitmap, boolean hasMipmap) {
        BitmapCompat.setHasMipMap(bitmap, hasMipmap);
    }

    /**
     * Returns the size of the allocated memory used to store this bitmap's pixels in a backwards
     * compatible way.
     *
     * @param bitmap the bitmap in which to return its allocation size
     * @return the allocation size in bytes
     */
    public static int getAllocationByteCount(@NonNull Bitmap bitmap) {
        return BitmapCompat.getAllocationByteCount(bitmap);
    }

}
