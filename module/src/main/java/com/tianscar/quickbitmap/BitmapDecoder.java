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

import android.content.ContentResolver;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tianscar.androidutils.ApplicationUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * A factory class providing functions to decode bitmap.
 */
public final class BitmapDecoder {

    private BitmapDecoder (){}

    public static @Nullable Bitmap decodeStream (@NonNull InputStream stream) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            bitmap = BitmapUtils.setMutable(bitmap);
            if (!bitmap.hasAlpha()) {
                bitmap.setHasAlpha(true);
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeStream (@NonNull InputStream stream, @NonNull Rect region) {
        Bitmap bitmap = null;
        BitmapRegionDecoder decoder = null;
        try {
            decoder = BitmapRegionDecoder.newInstance(stream, false);
            bitmap = decoder.decodeRegion(region, null);
            stream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (decoder != null) {
                if (!decoder.isRecycled()) {
                    decoder.recycle();
                }
            }
        }
        if (bitmap != null) {
            bitmap = BitmapUtils.setMutable(bitmap);
            if (!bitmap.hasAlpha()) {
                bitmap.setHasAlpha(true);
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeFile (@NonNull File file) {
        if (!file.exists()) {
            return null;
        }
        if (file.isDirectory()) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            FileInputStream stream = new FileInputStream(file);
            bitmap = BitmapFactory.decodeFileDescriptor(stream.getFD());
            stream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            bitmap = BitmapUtils.setMutable(bitmap);
            if (!bitmap.hasAlpha()) {
                bitmap.setHasAlpha(true);
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeFile (@NonNull File file, @NonNull Rect region) {
        Bitmap bitmap = null;
        BitmapRegionDecoder decoder = null;
        try {
            FileInputStream stream = new FileInputStream(file);
            decoder = BitmapRegionDecoder.newInstance(stream.getFD(), false);
            bitmap = decoder.decodeRegion(region, null);
            stream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (decoder != null) {
                if (!decoder.isRecycled()) {
                    decoder.recycle();
                }
            }
        }
        if (bitmap != null) {
            bitmap = BitmapUtils.setMutable(bitmap);
            if (!bitmap.hasAlpha()) {
                bitmap.setHasAlpha(true);
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeFile (@NonNull String path) {
        return decodeFile(new File(path));
    }

    public static @Nullable Bitmap decodeFile (@NonNull String path, Rect region) {
        return decodeFile(new File(path), region);
    }

    public static @Nullable Bitmap decodeByteArray (@NonNull byte[] data) {
        return decodeByteArray(data, 0, data.length);
    }

    public static @Nullable Bitmap decodeByteArray (@NonNull byte[] data, int offset, int length) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, offset, length);
        if (bitmap != null) {
            bitmap = BitmapUtils.setMutable(bitmap);
            if (!bitmap.hasAlpha()) {
                bitmap.setHasAlpha(true);
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeByteArray (@NonNull byte[] data, Rect region) {
        return decodeByteArray(data, 0, data.length, region);
    }

    public static @Nullable Bitmap decodeByteArray (@NonNull byte[] data, int offset, int length,
                                                    @NonNull Rect region) {
        Bitmap bitmap = null;
        BitmapRegionDecoder decoder = null;
        try {
            decoder = BitmapRegionDecoder.newInstance(data, offset, length, false);
            bitmap = decoder.decodeRegion(region, null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (decoder != null) {
                if (!decoder.isRecycled()) {
                    decoder.recycle();
                }
            }
        }
        if (bitmap != null) {
            bitmap = BitmapUtils.setMutable(bitmap);
            if (!bitmap.hasAlpha()) {
                bitmap.setHasAlpha(true);
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeAsset (@NonNull String fileName) {
        Bitmap bitmap = null;
        AssetManager assets = ApplicationUtils.getAssets();
        try {
            bitmap = decodeStream(assets.open(fileName));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeAsset (@NonNull String fileName, Rect region) {
        Bitmap bitmap = null;
        AssetManager assets = ApplicationUtils.getApplication().getAssets();
        try {
            bitmap = decodeStream(assets.open(fileName), region);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeResource (int resId) {
        Bitmap bitmap;
        Resources res = ApplicationUtils.getApplication().getResources();
        try {
            bitmap = decodeStream(res.openRawResource(resId));
        }
        catch (Resources.NotFoundException e) {
            bitmap = BitmapFactory.decodeResource(res, resId);
            if (bitmap == null) {
                Drawable drawable = ContextCompat.getDrawable(ApplicationUtils.getApplication(), resId);
                if (drawable != null) {
                    bitmap = decodeDrawable(drawable);
                }
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeResource (int resId, Rect region) {
        Bitmap bitmap = null;
        Resources res = ApplicationUtils.getApplication().getResources();
        try {
            bitmap = decodeStream(res.openRawResource(resId), region);
        }
        catch (Resources.NotFoundException e) {
            Drawable drawable = ContextCompat.getDrawable(ApplicationUtils.getApplication(), resId);
            if (drawable != null) {
                bitmap = decodeDrawable(drawable, region);
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeDrawable (@NonNull Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            if (bitmap != null) {
                bitmap = BitmapUtils.setMutable(bitmap);
                if (!bitmap.hasAlpha()) {
                    bitmap.setHasAlpha(true);
                }
            }
            return bitmap;
        }
        else {
            if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
                return null;
            }
            else {
                Bitmap bitmap = Bitmap.createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return bitmap;
            }
        }
    }

    public static @Nullable Bitmap decodeDrawable (@NonNull Drawable drawable, @NonNull Rect region) {
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            return null;
        }
        else {
            Bitmap bitmap = Bitmap.createBitmap(
                    region.width(),
                    region.height(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(region.left, region.top, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    public static @Nullable Bitmap decodeUri (@NonNull Uri uri) {
        Bitmap bitmap = null;
        ContentResolver resolver = ApplicationUtils.getApplication().getContentResolver();
        try {
            InputStream stream = resolver.openInputStream(uri);
            if (stream != null) {
                bitmap = decodeStream(stream);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeUri (@NonNull Uri uri, @NonNull Rect region) {
        Bitmap bitmap = null;
        ContentResolver resolver = ApplicationUtils.getApplication().getContentResolver();
        try {
            InputStream stream = resolver.openInputStream(uri);
            if (stream != null) {
                bitmap = decodeStream(stream, region);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeURI (@NonNull URI uri) {
        Bitmap bitmap = null;
        try {
            bitmap = decodeURL(uri.toURL());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeURI (@NonNull URI uri, Rect region) {
        Bitmap bitmap = null;
        try {
            bitmap = decodeURL(uri.toURL(), region);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeURL (@NonNull URL url) {
        Bitmap bitmap = null;
        try {
            bitmap = decodeStream(url.openStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeURL (@NonNull URL url, Rect region) {
        Bitmap bitmap = null;
        try {
            bitmap = decodeStream(url.openStream(), region);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
