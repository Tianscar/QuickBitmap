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

import androidx.annotation.NonNull;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class GlobalBitmapPool {

    private BitmapPool mBitmapPool;

    private final ReentrantReadWriteLock mReadWriteLock;

    private enum Singleton {
        INSTANCE;
        private final GlobalBitmapPool instance;
        Singleton() {
            instance = new GlobalBitmapPool();
        }
        public GlobalBitmapPool getInstance() {
            return instance;
        }
    }

    private GlobalBitmapPool() {
        mReadWriteLock = new ReentrantReadWriteLock(true);
        wrap(new BitmapPool());
    }

    private static GlobalBitmapPool getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    public static void wrap(@NonNull BitmapPool bitmapPool) {
        getInstance().mReadWriteLock.writeLock().lock();
        try {
            getInstance().mBitmapPool = bitmapPool;
        }
        finally {
            getInstance().mReadWriteLock.writeLock().unlock();
        }
    }

    @NonNull
    public static BitmapPool get() {
        BitmapPool result;
        getInstance().mReadWriteLock.readLock().lock();
        try {
            result = getInstance().mBitmapPool;
        }
        finally {
            getInstance().mReadWriteLock.readLock().unlock();
        }
        return result;
    }
    
}
