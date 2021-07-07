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

#include <sys/types.h>

#ifndef QUICKBITMAP_BITMAPUTIL_H
#define QUICKBITMAP_BITMAPUTIL_H

typedef struct {
    uint32_t x;
    uint32_t y;
} point;

template<typename color_t>
color_t get_pixel(color_t* pixel_arr, uint32_t x, uint32_t y, uint32_t width) {
    return pixel_arr[y * width + x];
}

template<typename color_t>
void set_pixel(color_t* pixel_arr, uint32_t x, uint32_t y, color_t color, uint32_t width) {
    pixel_arr[y * width + x] = color;
}

uint32_t argb2abgr(uint32_t);

uint16_t argb2rgb565(uint32_t);

u_char argb_alpha(uint32_t);

u_char argb_red(uint32_t);

u_char argb_green(uint32_t);

u_char argb_blue(uint32_t);

#endif