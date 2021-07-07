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
#include <stack>
#include "bitmaputil.h"

#ifndef QUICKBITMAP_FILL_H
#define QUICKBITMAP_FILL_H

using namespace std;

template<typename color_t>
void fill(color_t* pixel_arr, uint32_t x, uint32_t y, color_t color,
          uint32_t bitmap_width, uint32_t bitmap_height) {
    stack<point> point_stack;
    point seed;
    if (get_pixel(pixel_arr, x, y, bitmap_width) != color) {
        point_stack.push(point{x, y});
    }
    while (!point_stack.empty()) {
        seed = point_stack.top();
        point_stack.pop();
        color_t old_color = get_pixel(pixel_arr, seed.x, seed.y, bitmap_width);
        uint32_t x_l = seed.x;
        uint32_t x_r = seed.x;
        if (old_color != color) {
            while (x_l >= 1) {
                if (get_pixel(pixel_arr, x_l - 1u, seed.y, bitmap_width) != old_color) {
                    break;
                }
                x_l -= 1u;
            }
            while (x_r + 1 < bitmap_width) {
                if (get_pixel(pixel_arr, x_r + 1, seed.y, bitmap_width) != old_color) {
                    break;
                }
                x_r ++;
            }
            for (uint32_t detect_x = x_l; detect_x <= x_r; detect_x ++) {
                if (seed.y + 1 < bitmap_height) {
                    if (get_pixel(pixel_arr, detect_x, seed.y + 1, bitmap_width) ==
                        old_color) {
                        if (detect_x < x_r) {
                            if (get_pixel(pixel_arr, detect_x + 1, seed.y + 1,
                                          bitmap_width) != old_color) {
                                point_stack.push(point{detect_x, seed.y + 1});
                            }
                        }
                        else {
                            point_stack.push(point{detect_x, seed.y + 1});
                        }
                    }
                }
                if (seed.y >= 1) {
                    if (get_pixel(pixel_arr, detect_x, seed.y - 1u, bitmap_width) ==
                        old_color) {
                        if (detect_x < x_r) {
                            if (get_pixel(pixel_arr, detect_x + 1, seed.y - 1u,
                                          bitmap_width) != old_color) {
                                point_stack.push(point{detect_x, seed.y - 1u});
                            }
                        }
                        else {
                            point_stack.push(point{detect_x, seed.y - 1u});
                        }
                    }
                }
            }
            uint32_t r_x = seed.x + 1;
            if (r_x < bitmap_width) {
                while (get_pixel(pixel_arr, r_x, seed.y, bitmap_width) == old_color) {
                    set_pixel(pixel_arr, r_x, seed.y, color, bitmap_width);
                    if (r_x == bitmap_width - 1u) break;
                    r_x ++;
                }
            }
            while (get_pixel(pixel_arr, seed.x, seed.y, bitmap_width) == old_color) {
                set_pixel(pixel_arr, seed.x, seed.y, color, bitmap_width);
                if (seed.x == 0u) break;
                seed.x-= 1u;
            }
        }
    }
}

#endif