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
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;

import androidx.annotation.NonNull;

import com.tianscar.androidutils.MathUtils;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A factory class providing functions to change bitmap.
 */
public final class BitmapChanger {

    private volatile Bitmap dst;
    private final Canvas canvas;
    private final Paint paint;
    private final Path path;
    private final Matrix matrix;
    private final ReentrantReadWriteLock readWriteLock;

    /**
     * Create a copy bitmap for change and do not modify the source.
     *
     * @see BitmapChanger(Bitmap, boolean);
     *
     * @param src the source bitmap
     */
    public BitmapChanger(@NonNull Bitmap src) {
        this(src, true);
    }

    /**
     * If @param isCopy is true,
     * it will create a copy bitmap for change and do not modify the source.
     * else it will change the source bitmap.
     *
     * @param src the source bitmap
     * @param isCopy whether create & use copy
     */
    public BitmapChanger(@NonNull Bitmap src, boolean isCopy) {
        this();
        wrap(src, isCopy);
    }

    public BitmapChanger(@NonNull Bitmap src, @NonNull Rect region) {
        this(src, region.left, region.top, region.width(), region.height());
    }

    public BitmapChanger(@NonNull Bitmap src, @NonNull RectF region) {
        this(src, (int) region.left, (int) region.top, (int) region.width(), (int) region.height());
    }

    public BitmapChanger(@NonNull Bitmap src, int x, int y, int width, int height) {
        this();
        wrap(src, x, y, width, height);
    }

    public BitmapChanger() {
        paint = new Paint();
        paint.setAntiAlias(false);
        paint.setFilterBitmap(false);
        canvas = new Canvas();
        path = new Path();
        matrix = new Matrix();
        readWriteLock = new ReentrantReadWriteLock(true);
    }

    public BitmapChanger wrap(@NonNull Bitmap src) {
        return wrap(src, true);
    }

    public BitmapChanger wrap(@NonNull Bitmap src, boolean isCopy) {
        if (!src.isMutable()) {
            throw new IllegalArgumentException("Unable to change immutable bitmap.");
        }
        readWriteLock.writeLock().lock();
        try {
            if (isCopy) {
                dst = Bitmap.createBitmap(src);
            }
            else {
                dst = src;
            }
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return this;
    }

    public BitmapChanger wrap(@NonNull Bitmap src, @NonNull Rect region) {
        return wrap(src, region.left, region.top, region.width(), region.height());
    }

    public BitmapChanger wrap(@NonNull Bitmap src, int x, int y, int width, int height) {
        if (!src.isMutable()) {
            throw new IllegalArgumentException("Unable to change immutable bitmap.");
        }
        readWriteLock.writeLock().lock();
        try {
            dst = Bitmap.createBitmap(src, x, y, width, height);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return this;
    }

    /**
     * Cut out part of the bitmap.
     *
     * @param x begin x
     * @param y begin y
     * @param width dest bitmap width
     * @param height dest bitmap height
     * @param outBounds whether create bitmap which can larger than the source
     * @return BitmapChanger the current instance
     */
    public BitmapChanger cut (int x, int y, int width, int height, boolean outBounds) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be > 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be > 0");
        }
        readWriteLock.writeLock().lock();
        try {
            int originalWidth = dst.getWidth();
            int originalHeight = dst.getHeight();
            int beginX = x;
            int beginY = y;
            int endX = beginX + width - 1;
            int endY = beginY + height - 1;
            if (beginX >= 0 && beginY >= 0 && beginX < originalWidth && beginY < originalHeight
                    && endX >= 0 && endY >= 0 && endX < originalWidth && endY < originalHeight) {
                replaceDst(Bitmap.createBitmap(dst, beginX, beginY, width, height));
                return this;
            }
            if (outBounds) {
                Bitmap temp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                if (beginX >= originalWidth || beginY >= originalHeight || endX < 0 || endY < 0) {
                    replaceDst(temp);
                    return this;
                }
                int pasteX = 0;
                int pasteY = 0;
                if (beginX < 0) {
                    pasteX = - beginX;
                    beginX = 0;
                }
                if (beginY < 0) {
                    pasteY = - beginY;
                    beginY = 0;
                }
                if (endX >= originalWidth) {
                    endX = originalWidth - 1;
                }
                if (endY >= originalHeight) {
                    endY = originalHeight - 1;
                }
                replaceDst(Bitmap.createBitmap(dst, beginX, beginY,
                        endX - beginX + 1, endY - beginY + 1));
                canvas.setBitmap(temp);
                canvas.drawBitmap(dst, pasteX, pasteY, paint);
                replaceDst(temp);
            }
            else {
                if (beginX < 0) {
                    beginX = 0;
                }
                if (beginY < 0) {
                    beginY = 0;
                }
                if (endX >= originalWidth) {
                    endX = originalWidth - 1;
                }
                if (endY >= originalHeight) {
                    endY = originalHeight - 1;
                }
                replaceDst(Bitmap.createBitmap(dst, beginX, beginY,
                        endX - beginX + 1, endY - beginY + 1));
            }
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return this;
    }
    
    public BitmapChanger cut (int x, int y, int width, int height) {
        return cut(x, y, width, height, true);
    }

    /**
     * Cut out part of the bitmap.
     *
     * @param left the left-bound coordinate to crop
     * @param top the top-bound coordinate to crop
     * @param right the right-bound coordinate to crop
     * @param bottom the bottom-bound coordinate to crop
     * @param outBounds whether create bitmap which can larger than the source
     * @return BitmapChanger the current instance
     */
    public BitmapChanger crop (int left, int top, int right, int bottom, boolean outBounds) {
        right = right - 1;
        bottom = bottom - 1;
        int x = Math.min(left, right);
        int y = Math.min(top, bottom);
        int width = Math.max(left, right) - x + 1;
        int height = Math.max(top, bottom) - y + 1;
        return cut (x, y, width, height, outBounds);
    }
    
    public BitmapChanger crop (int left, int top, int right, int bottom) {
        return crop(left, top, right, bottom, true);
    }

    /**
     * Cut out part of the bitmap.
     *
     * @see BitmapChanger crop(int, int, int, int, boolean)
     *
     * @param region the region
     * @param outBounds whether create bitmap which can larger than the source
     * @return BitmapChanger the current instance
     */
    public BitmapChanger crop (@NonNull Rect region, boolean outBounds) {
        return crop (region.left, region.top, region.right, region.bottom, outBounds);
    }
    
    public BitmapChanger crop (@NonNull Rect region) {
        return crop (region, true);
    }

    /**
     * Cut out part of the bitmap.
     *
     * @see BitmapChanger crop(int, int, int, int, boolean)
     *
     * @param region the region
     * @param outBounds whether create bitmap which can larger than the source
     * @return BitmapChanger the current instance
     */
    public BitmapChanger crop (@NonNull RectF region, boolean outBounds) {
        return crop ((int) region.left, (int) region.top, (int) region.right, (int) region.bottom,
                outBounds);
    }

    public BitmapChanger crop (@NonNull RectF region) {
        return crop (region, true);
    }

    /**
     * Clip path for the bitmap.
     *
     * @param path path to clip
     * @return BitmapChanger the current instance
     */
    public BitmapChanger clipPath (@NonNull Path path) {
        readWriteLock.writeLock().lock();
        try {
            Bitmap temp = Bitmap.createBitmap(dst.getWidth(), dst.getHeight(),
                    Bitmap.Config.ARGB_8888);
            canvas.setBitmap(temp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                canvas.clipPath(path);
            }
            else {
                canvas.clipPath(path, Region.Op.REPLACE);
            }
            canvas.drawBitmap(dst, 0, 0, paint);
            replaceDst(temp);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return this;
    }

    /**
     * Clip oval for the whole bitmap.
     *
     * @see BitmapChanger clipPath(Path)
     *
     * @return BitmapChanger the current instance
     */
    public BitmapChanger clipOval () {
        readWriteLock.writeLock().lock();
        try {
            path.reset();
            path.addOval(new RectF(0, 0, dst.getWidth(), dst.getHeight()), Path.Direction.CW);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return clipPath(path);
    }

    /**
     * Clip round rect for the whole bitmap.
     *
     * @see BitmapChanger clipPath(Path)
     *
     * @param radiusX radius x
     * @param radiusY radius y
     * @return BitmapChanger the current instance
     */
    public BitmapChanger clipRoundRect (float radiusX, float radiusY) {
        readWriteLock.writeLock().lock();
        try {
            path.reset();
            path.addRoundRect(new RectF(0, 0, dst.getWidth(), dst.getHeight()), radiusX, radiusY, Path.Direction.CW);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return clipPath(path);
    }

    /**
     * Clip round rect for the whole bitmap.
     *
     * @see BitmapChanger clipPath(Path)
     *
     * @param radii radii
     * @return BitmapChanger the current instance
     */
    public BitmapChanger clipRoundRect (float[] radii) {
        readWriteLock.writeLock().lock();
        try {
            path.reset();
            path.addRoundRect(new RectF(0, 0, dst.getWidth(), dst.getHeight()), radii, Path.Direction.CW);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return clipPath(path);
    }

    /**
     * Use matrix to change the bitmap.
     *
     * @param matrix the matrix for change
     * @return BitmapChanger the current instance
     */
    public BitmapChanger matrixChange(@NonNull Matrix matrix) {
        readWriteLock.writeLock().lock();
        try {
            replaceDst(Bitmap.createBitmap(dst, 0, 0, dst.getWidth(), dst.getHeight(), matrix, false));
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return this;
    }

    /**
     * Rotate the bitmap.
     *
     * @see BitmapChanger matrixChange(Matrix)
     *
     * @param degrees the degrees
     * @return BitmapChanger the current instance
     */
    public BitmapChanger rotateDegrees (int degrees) {
        readWriteLock.writeLock().lock();
        try {
            matrix.reset();
            matrix.setRotate(degrees, dst.getWidth() * 0.5f, dst.getHeight() * 0.5f);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return matrixChange(matrix);
    }

    /**
     * Rotate the bitmap.
     *
     * @see BitmapChanger matrixChange(Matrix)
     *
     * @param degrees the degrees
     * @return BitmapChanger the current instance
     */
    public BitmapChanger rotateDegrees (float degrees) {
        readWriteLock.writeLock().lock();
        try {
            matrix.reset();
            matrix.setRotate(degrees, dst.getWidth() * 0.5f, dst.getHeight() * 0.5f);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return matrixChange(matrix);
    }

    /**
     * Rotate the bitmap.
     *
     * @see BitmapChanger matrixChange(Matrix)
     *
     * @param radians the radians
     * @return BitmapChanger the current instance
     */
    public BitmapChanger rotateRadians (int radians) {
        return rotateDegrees(MathUtils.rad2deg(radians));
    }

    /**
     * Rotate the bitmap.
     *
     * @see BitmapChanger matrixChange(Matrix)
     *
     * @param radians the radians
     * @return BitmapChanger the current instance
     */
    public BitmapChanger rotateRadians (float radians) {
        return rotateDegrees(MathUtils.rad2deg(radians));
    }

    /**
     * Flip the bitmap horizontally.
     *
     * @see BitmapChanger matrixChange(Matrix)
     *
     * @return BitmapChanger the current instance
     */
    public BitmapChanger flipHorizontally () {
        readWriteLock.writeLock().lock();
        try {
            matrix.reset();
            matrix.postScale(-1, 1);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return matrixChange(matrix);
    }

    /**
     * Flip the bitmap vertically.
     *
     * @see BitmapChanger matrixChange(Matrix)
     *
     * @return BitmapChanger the current instance
     */
    public BitmapChanger flipVertically () {
        readWriteLock.writeLock().lock();
        try {
            matrix.reset();
            matrix.postScale(1, -1);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return matrixChange(matrix);
    }

    /**
     * Scale the bitmap.
     *
     * @see BitmapChanger matrixChange(Matrix)
     *
     * @param scaleX x scale
     * @param scaleY y scale
     * @param filter use filter
     * @return BitmapChanger the current instance
     */
    public BitmapChanger scale (float scaleX, float scaleY, boolean filter) {
        if (scaleX <= 0) {
            throw new IllegalArgumentException("ScaleX must be > 0");
        }
        if (scaleY <= 0) {
            throw new IllegalArgumentException("ScaleY must be > 0");
        }
        readWriteLock.writeLock().lock();
        try {
            matrix.reset();
            matrix.postScale(scaleX, scaleY);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return matrixChange(matrix);
    }

    public BitmapChanger scale (float scaleX, float scaleY) {
        return scale(scaleX, scaleY, true);
    }

    /**
     * Scale the bitmap.
     *
     * @see BitmapChanger scale(float, float, boolean)
     *
     * @param scale x & y scale
     * @param filter use filter
     * @return BitmapChanger the current instance
     */
    public BitmapChanger scale (float scale, boolean filter) {
        return scale(scale, scale, filter);
    }

    public BitmapChanger scale (float scale) {
        return scale(scale, scale);
    }

    /**
     * Scale the bitmap.
     *
     * @see BitmapChanger scale(float, float, boolean)
     *
     * @param scaleX x scale
     * @param filter use filter
     * @return BitmapChanger the current instance
     */
    public BitmapChanger scaleX (float scaleX, boolean filter) {
        return scale(scaleX, 1, filter);
    }

    public BitmapChanger scaleX (float scaleX) {
        return scale(scaleX, 1);
    }

    /**
     * Scale the bitmap.
     *
     * @see BitmapChanger scale(float, float, boolean)
     *
     * @param scaleY y scale
     * @param filter use filter
     * @return BitmapChanger the current instance
     */
    public BitmapChanger scaleY (float scaleY, boolean filter) {
        return scale(1, scaleY, filter);
    }

    public BitmapChanger scaleY (float scaleY) {
        return scale(1, scaleY);
    }

    /**
     * Scale the bitmap.
     *
     * @param width scaled width
     * @param height scaled height
     * @param filter use filter
     * @return BitmapChanger the current instance
     */
    public BitmapChanger resize (int width, int height, boolean filter) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be > 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be > 0");
        }
        readWriteLock.writeLock().lock();
        try {
            replaceDst(Bitmap.createScaledBitmap(dst, width, height, filter));
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return this;
    }

    public BitmapChanger resize (int width, int height) {
        return resize(width, height, true);
    }

    /**
     * Scale the bitmap.
     *
     * @see BitmapChanger resize(int, int, boolean)
     *
     * @param width scaled width
     * @param filter use filter
     * @return BitmapChanger the current instance
     */
    public BitmapChanger resizeWidth (int width, boolean filter) {
        return resize(width, dst.getHeight(), filter);
    }

    public BitmapChanger resizeWidth (int width) {
        return resize(width, dst.getHeight());
    }

    /**
     * Scale the bitmap.
     *
     * @see BitmapChanger resize(int, int, boolean)
     *
     * @param height scaled height
     * @param filter use filter
     * @return BitmapChanger the current instance
     */
    public BitmapChanger resizeHeight (int height, boolean filter) {
        return resize(dst.getWidth(), height, filter);
    }

    public BitmapChanger resizeHeight (int height) {
        return resize(dst.getWidth(), height);
    }

    /**
     * Return the changed bitmap.
     *
     * @return bitmap changed bitmap
     */
    @NonNull
    public Bitmap change () {
        return dst;
    }

    private void replaceDst(Bitmap newDst) {
        if (dst == newDst) {
            return;
        }
        Bitmap temp = dst;
        dst = newDst;
        BitmapUtils.recycle(temp);
    }

    /**
     * Seed filling.
     *
     * @param x position x
     * @param y position y
     * @param color color to fill
     * @return BitmapChanger the current instance
     */
    public BitmapChanger fill (int x, int y, int color) {
        switch (dst.getConfig()) {
            case HARDWARE:
            case RGBA_F16:
                throw new IllegalArgumentException("Unsupported bitmap config.");
        }
        readWriteLock.writeLock().lock();
        try {
            NativeMethods.fill(dst, x, y, color);
        }
        finally {
            readWriteLock.writeLock().unlock();
        }
        return this;
    }

}
