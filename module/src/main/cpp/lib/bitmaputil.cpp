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

#include "bitmaputil.h"

uint32_t get_pixel_abgr(uint32_t* pixel_arr, uint32_t x, uint32_t y,
                   uint32_t width) {
    return pixel_arr[y * width + x];
}

void set_pixel_abgr(uint32_t* pixel_arr, uint32_t x, uint32_t y, uint32_t color,
               uint32_t width) {
    pixel_arr[y * width + x] = color;
}

uint16_t get_pixel_rgb565(uint16_t* pixel_arr, uint32_t x, uint32_t y,
                          uint32_t width) {
    return pixel_arr[y * width + x];
}

void set_pixel_rgb565(uint16_t* pixel_arr, uint32_t x, uint32_t y, uint16_t color,
                      uint32_t width) {
    pixel_arr[y * width + x] = color;
}

u_char get_pixel_alpha8(u_char * pixel_arr, uint32_t x, uint32_t y,
                        uint32_t width) {
    return pixel_arr[y * width + x];
}

void set_pixel_alpha8(u_char * pixel_arr, uint32_t x, uint32_t y, u_char color,
                      uint32_t width) {
    pixel_arr[y * width + x] = color;
}

uint32_t argb2abgr(uint32_t color) {
    return (argb_alpha(color) & 0xFFu) << 24u | (argb_blue(color) & 0xFFu) << 16u |
    (argb_green(color) & 0xFFu) << 8u | (argb_red(color) & 0xFFu);
}

uint16_t argb2rgb565(uint32_t color) {
    return (argb_red(color) & 0x1Fu) << 11u |
    (argb_green(color) & 0x3Fu) << 5u | (argb_blue(color) & 0x1Fu);
}

uint32_t rgb565_2argb(uint16_t color) {
    u_char red = (color & RGB565_RED) >> 8u;
    u_char green = (color & RGB565_GREEN) >> 3u;
    u_char blue = (color & RGB565_BLUE) << 3u;
    return (0xFF000000 << 24u) + (red << 16u) + (green << 8u) + blue;
}

u_char argb_alpha(uint32_t color) {
    return color >> 24u;
}

u_char argb_red(uint32_t color) {
    return color >> 16u;
}

u_char argb_green(uint32_t color) {
    return color >> 8u;
}

u_char argb_blue(uint32_t color) {
    return color;
}