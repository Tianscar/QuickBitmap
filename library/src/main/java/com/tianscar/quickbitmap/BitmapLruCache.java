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
import androidx.collection.LruCache;

public final class BitmapLruCache extends LruCache<String, Bitmap> {

    public interface OnEntryRemovedListener {
        /**
         * Called on entries that have been evicted or removed. This method is
         * invoked when a value is evicted to make space, removed by a call to
         * {@link #remove}, or replaced by a call to {@link #put}. The default
         * implementation does nothing.
         *
         * <p>The method is called without synchronization: other threads may
         * access the cache while this method is executing.
         *
         * @param evicted true if the entry is being removed to make space, false
         *     if the removal was caused by a {@link #put} or {@link #remove}.
         * @param newValue the new value for {@code key}, if it exists. If non-null,
         *     this removal was caused by a {@link #put}. Otherwise it was caused by
         *     an eviction or a {@link #remove}.
         */
        void onEntryRemoved(boolean evicted, @NonNull String key,
                            @NonNull Bitmap oldValue, @Nullable Bitmap newValue);
    }

    private OnEntryRemovedListener mOnEntryRemovedListener;

    /**
     * @param maxSize the maximum sum of the sizes of the entries in this cache.
     */
    public BitmapLruCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
        return value.getByteCount();
    }

    @Override
    protected void entryRemoved(boolean evicted, @NonNull String key,
                                @NonNull Bitmap oldValue, @Nullable Bitmap newValue) {
        if (mOnEntryRemovedListener != null) {
            mOnEntryRemovedListener.onEntryRemoved(evicted, key, oldValue, newValue);
        }
    }

    public void setOnEntryRemovedListener(@Nullable OnEntryRemovedListener mOnEntryRemovedListener) {
        this.mOnEntryRemovedListener = mOnEntryRemovedListener;
    }

    public OnEntryRemovedListener getOnEntryRemovedListener() {
        return mOnEntryRemovedListener;
    }

}
