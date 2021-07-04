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

package com.tianscar.test;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.tianscar.quickbitmap.BitmapChanger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Bitmap[] bitmap = {Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)};
        bitmap[0].setPixel(0, 0, Color.BLACK);
        /*
        bitmap.setPixel(0, 1, Color.BLUE);
        bitmap.setPixel(1, 1, Color.GREEN);
        bitmap.setPixel(1, 0, Color.YELLOW);

         */
        bitmap[0] = Bitmap.createScaledBitmap(bitmap[0], 2000, 2000, false);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap[0]);
        long time1 = System.currentTimeMillis();
        BitmapChanger bitmapChanger = new BitmapChanger();
        bitmap[0] = bitmapChanger.wrap(bitmap[0], false).fill(100, 100, Color.YELLOW).change();
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap[0] = bitmapChanger.clipOval().change();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap[0] = bitmapChanger.clipRoundRect(50f, 50f).
                        resize(1000, 1300, false).rotateDegrees(45).change();
            }
        }).start();
        time1 = System.currentTimeMillis() - time1;
        long time2 = System.currentTimeMillis();
        bitmap1 = new BitmapChanger(bitmap1, false).fill(1, 1, Color.BLUE).change();
        time2 = System.currentTimeMillis() - time2;
        Log.e("233", "time1: "+time1+" | "+"time2: "+time2);
        ImageView imageView = new ImageView(this);
        setContentView(imageView);
        imageView.setImageBitmap(bitmap[0]);
    }
}