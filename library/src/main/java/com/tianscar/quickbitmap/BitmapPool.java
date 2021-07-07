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
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jakewharton.disklrucache.DiskLruCache;
import com.tianscar.androidutils.EnvironmentUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class BitmapPool {

    public final static Bitmap.CompressFormat CACHE_COMPRESS_FORMAT =
            Bitmap.CompressFormat.PNG;

    public static int getDefaultLruCacheMaxSize() {
        long maxSize = Runtime.getRuntime().maxMemory() / 8;
        return maxSize > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)maxSize;
    }

    public static long getDefaultDiskLruCacheMaxSize() {
        return 10 * 1024 * 1024;
    }

    @NonNull
    public static String getDefaultDirectoryPath() {
        return getDefaultDirectory().getAbsolutePath();
    }

    @NonNull
    public static File getDefaultDirectory() {
        return new File(EnvironmentUtils.getInternalCacheDir(), "bitmap_pool");
    }

    private final BitmapLruCache mLruCache;
    private final DiskLruCache mDiskLruCache;

    private final ReentrantReadWriteLock mReadWriteLock;

    private boolean released;

    public BitmapPool(@NonNull String directoryPath) {
        this(new File(directoryPath));
    }

    public BitmapPool(@NonNull String directoryPath, int lruCacheMaxSize, long diskLruCacheMaxSize) {
        this(new File(directoryPath), lruCacheMaxSize, diskLruCacheMaxSize);
    }

    public BitmapPool() {
        this(getDefaultDirectory());
    }

    public BitmapPool(@NonNull File directory) {
        this(directory, getDefaultLruCacheMaxSize(), getDefaultDiskLruCacheMaxSize());
    }

    public BitmapPool(@NonNull File directory, int lruCacheMaxSize, long diskLruCacheMaxSize) {
        mReadWriteLock = new ReentrantReadWriteLock(true);
        mReadWriteLock.writeLock().lock();
        try {
            if ((!directory.isDirectory()) || (!directory.canWrite())) {
                throw new RuntimeException("Cache dir is not available.");
            }
            mLruCache = new BitmapLruCache(lruCacheMaxSize);
            mLruCache.setOnEntryRemovedListener(new BitmapLruCache.OnEntryRemovedListener() {
                @Override
                public void onEntryRemoved(boolean evicted, @NonNull String key,
                                           @NonNull Bitmap oldValue, @Nullable Bitmap newValue) {
                    putDiskLruCache(key, oldValue);
                    BitmapUtils.recycle(oldValue);
                }
            });
            try {
                mDiskLruCache = DiskLruCache.open(directory, 0, 1, diskLruCacheMaxSize);
            }
            catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        finally {
            released = false;
            mReadWriteLock.writeLock().unlock();
        }
    }

    public void put(@NonNull String name, @NonNull Bitmap bitmap) {
        mReadWriteLock.writeLock().lock();
        try {
            checkReleased();
            mLruCache.put(name, bitmap);
        }
        finally {
            mReadWriteLock.writeLock().unlock();
        }
    }

    @Nullable
    public Bitmap get(@NonNull String name) {
        mReadWriteLock.readLock().lock();
        Bitmap result;
        try {
            checkReleased();
            result = mLruCache.get(name);
            if (result == null) {
                result = getDiskLruCache(name);
            }
        }
        finally {
            mReadWriteLock.readLock().unlock();
        }
        return result;
    }

    private void checkReleased() {
        if (released) {
            throw new IllegalStateException("The current instance has been released, " +
                    "you can create a new instance for use.");
        }
    }

    public void release() {
        release(true);
    }

    public void release(boolean clear) {
        mReadWriteLock.writeLock().lock();
        try {
            checkReleased();
            releaseLruCache();
            releaseDiskLruCache(clear);
        }
        finally {
            released = true;
            mReadWriteLock.writeLock().unlock();
        }
    }

    public void flush() {
        mReadWriteLock.writeLock().lock();
        try {
            checkReleased();
            releaseLruCache();
        }
        finally {
            mReadWriteLock.writeLock().unlock();
        }
    }

    private void releaseLruCache() {
        mLruCache.trimToSize(0);
    }

    private void releaseDiskLruCache(boolean clear) {
        try {
            if (!isDiskLruCacheReleased()) {
                if (clear) {
                    mDiskLruCache.delete();
                }
                else {
                    mDiskLruCache.close();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isDiskLruCacheReleased() {
        return mDiskLruCache.isClosed();
    }

    private void putDiskLruCache(@NonNull String name, @NonNull Bitmap bitmap) {
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(name);
            OutputStream outputStream = editor.newOutputStream(0);
            bitmap.compress(BitmapPool.CACHE_COMPRESS_FORMAT, 100, outputStream);
            editor.commit();
            mDiskLruCache.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    private Bitmap getDiskLruCache(@NonNull String name) {
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(name);
            InputStream inputStream = snapshot.getInputStream(0);
            return BitmapFactory.decodeStream(inputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getLruCacheSize() {
        checkReleased();
        return mLruCache.size();
    }

    public int getLruCacheMaxSize() {
        checkReleased();
        return mLruCache.maxSize();
    }

    public long getDiskLruCacheSize() {
        checkReleased();
        return mDiskLruCache.size();
    }

    public long getDiskLruCacheMaxSize() {
        checkReleased();
        return mDiskLruCache.getMaxSize();
    }

    @NonNull
    public String getDirectoryPath() {
        return getDirectory().getAbsolutePath();
    }

    @NonNull
    public File getDirectory() {
        checkReleased();
        return mDiskLruCache.getDirectory();
    }
    
}
