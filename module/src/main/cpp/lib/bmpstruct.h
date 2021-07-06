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

#ifndef QUICKBITMAP_BMPSTRUCT_H
#define QUICKBITMAP_BMPSTRUCT_H

struct {
    uint16_t bfType = 0x4D42;
    uint32_t bfSize;
    uint16_t bfReversed1 = 0;
    uint16_t bfReversed2 = 0;
    uint32_t bfOffBits = 54;
} BITMAPFILEHEADER;

struct {
    uint32_t biSize = 40;
    uint32_t biWidth;
    uint32_t biHeight;
    uint16_t biPlanes = 1;
    uint16_t biBitCount;
    uint32_t biCompression = 0;
    uint32_t biSizeImage;
    uint32_t biXPelsPerMeter = 3780;
    uint32_t biYPelsPerMeter = 3780;
    uint32_t biClrUsed = 0;
    uint32_t biClrImportant = 0;
} BITMAPINFOHEADER;

struct {
    u_char rgbBlue;
    u_char rgbGreen;
    u_char rgbRed;
    u_char rgbReversed;
} RGBQUAD;

#endif
