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

    public static @Nullable Bitmap decodeStream (@NonNull InputStream stream,
                                                 @Nullable Bitmap.Config config) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            options.inPreferredConfig = config;
            bitmap = BitmapFactory.decodeStream(stream, null, options);
            stream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            if (!bitmap.hasAlpha()) {
                bitmap.setHasAlpha(true);
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeStream(@NonNull InputStream stream) {
        return decodeStream(stream, (Bitmap.Config) null);
    }

    public static @Nullable Bitmap decodeStream (@NonNull InputStream stream,
                                                 @NonNull Rect region, @Nullable Bitmap.Config config) {
        Bitmap bitmap = null;
        BitmapRegionDecoder decoder = null;
        try {
            decoder = BitmapRegionDecoder.newInstance(stream, false);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            options.inPreferredConfig = config;
            bitmap = decoder.decodeRegion(region, options);
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
            if (!bitmap.hasAlpha()) {
                bitmap.setHasAlpha(true);
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeStream(@NonNull InputStream stream, @NonNull Rect region) {
        return decodeStream(stream, region, null);
    }

    public static @Nullable Bitmap decodeFile (@NonNull File file, @Nullable Bitmap.Config config) {
        if (!file.exists()) {
            return null;
        }
        if (file.isDirectory()) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            FileInputStream stream = new FileInputStream(file);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            options.inPreferredConfig = config;
            bitmap = BitmapFactory.decodeFileDescriptor(stream.getFD(), null, options);
            stream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            if (!bitmap.hasAlpha()) {
                bitmap.setHasAlpha(true);
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeFile(@NonNull File file) {
        return decodeFile(file, (Bitmap.Config) null);
    }

    public static @Nullable Bitmap decodeFile (@NonNull File file,
                                               @NonNull Rect region, @Nullable Bitmap.Config config) {
        Bitmap bitmap = null;
        BitmapRegionDecoder decoder = null;
        try {
            FileInputStream stream = new FileInputStream(file);
            decoder = BitmapRegionDecoder.newInstance(stream.getFD(), false);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            options.inPreferredConfig = config;
            bitmap = decoder.decodeRegion(region, options);
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
            if (!bitmap.hasAlpha()) {
                bitmap.setHasAlpha(true);
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeFile(@NonNull File file, @NonNull Rect region) {
        return decodeFile(file, region, null);
    }

    public static @Nullable Bitmap decodeFile (@NonNull String pathname,
                                               @Nullable Bitmap.Config config) {
        return decodeFile(new File(pathname), config);
    }

    public static @Nullable Bitmap decodeFile(@NonNull String pathname) {
        return decodeFile(pathname, (Bitmap.Config) null);
    }

    public static @Nullable Bitmap decodeFile (@NonNull String pathname,
                                               @NonNull Rect region, @Nullable Bitmap.Config config) {
        return decodeFile(new File(pathname), region, config);
    }

    public static @Nullable Bitmap decodeFile(@NonNull String pathname, @NonNull Rect region) {
        return decodeFile(pathname, region, null);
    }

    public static @Nullable Bitmap decodeByteArray (@NonNull byte[] data,
                                                    @Nullable Bitmap.Config config) {
        return decodeByteArray(data, 0, data.length, config);
    }

    public static @Nullable Bitmap decodeByteArray (@NonNull byte[] data) {
        return decodeByteArray(data, (Bitmap.Config) null);
    }

    public static @Nullable Bitmap decodeByteArray (@NonNull byte[] data, int offset, int length,
                                                    @Nullable Bitmap.Config config) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        options.inPreferredConfig = config;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, offset, length, options);
        if (bitmap != null) {
            if (!bitmap.hasAlpha()) {
                bitmap.setHasAlpha(true);
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeByteArray (@NonNull byte[] data, int offset, int length) {
        return decodeByteArray(data, offset, length, (Bitmap.Config) null);
    }

    public static @Nullable Bitmap decodeByteArray (@NonNull byte[] data, @NonNull Rect region,
                                                    @Nullable Bitmap.Config config) {
        return decodeByteArray(data, 0, data.length, region, config);
    }

    public static @Nullable Bitmap decodeByteArray (@NonNull byte[] data, @NonNull Rect region) {
        return decodeByteArray(data, region, null);
    }

    public static @Nullable Bitmap decodeByteArray (@NonNull byte[] data, int offset, int length,
                                                    @NonNull Rect region, @Nullable Bitmap.Config config) {
        Bitmap bitmap = null;
        BitmapRegionDecoder decoder = null;
        try {
            decoder = BitmapRegionDecoder.newInstance(data, offset, length, false);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            options.inPreferredConfig = config;
            bitmap = decoder.decodeRegion(region, options);
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
            if (!bitmap.hasAlpha()) {
                bitmap.setHasAlpha(true);
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeByteArray (@NonNull byte[] data, int offset, int length,
                                                    @NonNull Rect region) {
        return decodeByteArray(data, offset, length, region, null);
    }

    public static @Nullable Bitmap decodeAsset (@NonNull String asset,
                                                @Nullable Bitmap.Config config) {
        Bitmap bitmap = null;
        AssetManager assets = ApplicationUtils.getAssets();
        try {
            bitmap = decodeStream(assets.open(asset), config);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeAsset (@NonNull String asset) {
        return decodeAsset(asset, (Bitmap.Config) null);
    }

    public static @Nullable Bitmap decodeAsset (@NonNull String asset,
                                                @NonNull Rect region, @Nullable Bitmap.Config config) {
        Bitmap bitmap = null;
        AssetManager assets = ApplicationUtils.getApplication().getAssets();
        try {
            bitmap = decodeStream(assets.open(asset), region, config);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeAsset (@NonNull String asset, @NonNull Rect region) {
        return decodeAsset(asset, region, null);
    }

    public static @Nullable Bitmap decodeResource (int resId, @Nullable Bitmap.Config config) {
        Bitmap bitmap;
        Resources res = ApplicationUtils.getApplication().getResources();
        try {
            bitmap = decodeStream(res.openRawResource(resId), config);
        }
        catch (Resources.NotFoundException e) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            options.inPreferredConfig = config;
            bitmap = BitmapFactory.decodeResource(res, resId, options);
            if (bitmap == null) {
                Drawable drawable = ContextCompat.getDrawable(ApplicationUtils.getApplication(), resId);
                if (drawable != null) {
                    bitmap = decodeDrawable(drawable, config);
                }
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeResource (int resId) {
        return decodeResource(resId, (Bitmap.Config) null);
    }

    public static @Nullable Bitmap decodeResource (int resId, @NonNull Rect region,
                                                   @Nullable Bitmap.Config config) {
        Bitmap bitmap = null;
        Resources res = ApplicationUtils.getApplication().getResources();
        try {
            bitmap = decodeStream(res.openRawResource(resId), region, config);
        }
        catch (Resources.NotFoundException e) {
            Drawable drawable = ContextCompat.getDrawable(ApplicationUtils.getApplication(), resId);
            if (drawable != null) {
                bitmap = decodeDrawable(drawable, region, config);
            }
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeResource (int resId, @NonNull Rect region) {
        return decodeResource(resId, region, null);
    }

    public static @Nullable Bitmap decodeDrawable (@NonNull Drawable drawable,
                                                   @Nullable Bitmap.Config config) {
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            if (bitmap != null) {
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
                        config == null ? (
                                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565) :
                                config);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return bitmap;
            }
        }
    }

    public static @Nullable Bitmap decodeDrawable (@NonNull Drawable drawable) {
        return decodeDrawable(drawable, (Bitmap.Config) null);
    }

    public static @Nullable Bitmap decodeDrawable (@NonNull Drawable drawable,
                                                   @NonNull Rect region, @Nullable Bitmap.Config config) {
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            return null;
        }
        else {
            Bitmap bitmap = Bitmap.createBitmap(
                    region.width(),
                    region.height(),
                    config == null ? (
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565) :
                            config);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(region.left, region.top, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    public static @Nullable Bitmap decodeDrawable (@NonNull Drawable drawable, @NonNull Rect region) {
        return decodeDrawable(drawable, region, null);
    }

    public static @Nullable Bitmap decodeUri (@NonNull Uri uri, @Nullable Bitmap.Config config) {
        Bitmap bitmap = null;
        ContentResolver resolver = ApplicationUtils.getApplication().getContentResolver();
        try {
            InputStream stream = resolver.openInputStream(uri);
            if (stream != null) {
                bitmap = decodeStream(stream, config);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeUri (@NonNull Uri uri) {
        return decodeUri(uri, (Bitmap.Config) null);
    }

    public static @Nullable Bitmap decodeUri (@NonNull Uri uri, @NonNull Rect region,
                                              @Nullable Bitmap.Config config) {
        Bitmap bitmap = null;
        ContentResolver resolver = ApplicationUtils.getApplication().getContentResolver();
        try {
            InputStream stream = resolver.openInputStream(uri);
            if (stream != null) {
                bitmap = decodeStream(stream, region, config);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeUri (@NonNull Uri uri, @NonNull Rect region) {
        return decodeUri(uri, region, null);
    }

    public static @Nullable Bitmap decodeURI (@NonNull URI uri, @Nullable Bitmap.Config config) {
        Bitmap bitmap = null;
        try {
            bitmap = decodeURL(uri.toURL(), config);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeURI (@NonNull URI uri) {
        return decodeURI(uri, (Bitmap.Config) null);
    }

    public static @Nullable Bitmap decodeURI (@NonNull URI uri, @NonNull Rect region,
                                              @Nullable Bitmap.Config config) {
        Bitmap bitmap = null;
        try {
            bitmap = decodeURL(uri.toURL(), region, config);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeURI (@NonNull URI uri, @NonNull Rect region) {
        return decodeURI(uri, region, null);
    }

    public static @Nullable Bitmap decodeURL (@NonNull URL url, @Nullable Bitmap.Config config) {
        Bitmap bitmap = null;
        try {
            bitmap = decodeStream(url.openStream(), config);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeURL (@NonNull URL url) {
        return decodeURL(url, (Bitmap.Config) null);
    }

    public static @Nullable Bitmap decodeURL (@NonNull URL url, @NonNull Rect region,
                                              @Nullable Bitmap.Config config) {
        Bitmap bitmap = null;
        try {
            bitmap = decodeStream(url.openStream(), region, config);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static @Nullable Bitmap decodeURL (@NonNull URL url, @NonNull Rect region) {
        return decodeURL(url, region, null);
    }

}
