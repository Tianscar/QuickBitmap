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

#include "fill.h"
#include "bitmaputil.h"
#include <stack>

using namespace std;

void fill_line_right_abgr(uint32_t* pixel_arr, uint32_t x, uint32_t y,
                          uint32_t color, uint32_t width) {
    uint32_t old_color = get_pixel_abgr(pixel_arr, x, y, width);
    if (old_color != color) {
        for(;;) {
            if(x < width) {
                if(get_pixel_abgr(pixel_arr, x, y, width) == old_color) {
                    set_pixel_abgr(pixel_arr, x, y, color, width);
                    x ++;
                }
                else {
                    break;
                }
            }
            else {
                break;
            }
        }
    }
}

void fill_line_left_abgr(uint32_t* pixel_arr, uint32_t x, uint32_t y,
                         uint32_t color, uint32_t width) {
    uint32_t old_color = get_pixel_abgr(pixel_arr, x, y, width);
    if (old_color != color) {
        for(;;) {
            if (get_pixel_abgr(pixel_arr, x, y, width) == old_color) {
                set_pixel_abgr(pixel_arr, x, y, color, width);
                if (x == 0u) {
                    break;
                }
                x -= 1u;
            }
            else {
                break;
            }
        }
    }
}

void fill_line_abgr(uint32_t* pixel_arr, uint32_t x, uint32_t y, uint32_t color, uint32_t width) {
    if (x + 1 < width) {
        if (get_pixel_abgr(pixel_arr, x + 1, y, width) == get_pixel_abgr(pixel_arr, x, y, width)) {
            fill_line_right_abgr(pixel_arr, x + 1, y, color, width);
        }
    }
    fill_line_left_abgr(pixel_arr, x, y, color, width);
}

void fill_abgr(uint32_t* pixel_arr, uint32_t x, uint32_t y, uint32_t color,
               uint32_t bitmap_width, uint32_t bitmap_height) {
    stack<point> point_stack;
    point seed;
    if (get_pixel_abgr(pixel_arr, x, y, bitmap_width) != color) {
        point_stack.push(point{x, y});
    }
    for (;;) {
        if (!point_stack.empty()) {
            seed = point_stack.top();
            point_stack.pop();
            uint32_t old_color = get_pixel_abgr(pixel_arr, seed.x, seed.y, bitmap_width);
            uint32_t x_l = seed.x;
            uint32_t x_r = seed.x;
            if (old_color != color) {
                for (;;) {
                    if (x_l >= 1) {
                        if (get_pixel_abgr(pixel_arr, x_l - 1u, seed.y, bitmap_width) != old_color) {
                            break;
                        }
                        x_l -= 1u;
                    }
                    else {
                        break;
                    }
                }
                for (;;) {
                    if (x_r + 1 < bitmap_width) {
                        if (get_pixel_abgr(pixel_arr, x_r + 1, seed.y, bitmap_width) != old_color) {
                            break;
                        }
                        x_r ++;
                    }
                    else {
                        break;
                    }
                }
                for (uint32_t detect_x = x_l; detect_x <= x_r; detect_x ++) {
                    if (seed.y + 1 < bitmap_height) {
                        if (get_pixel_abgr(pixel_arr, detect_x, seed.y + 1, bitmap_width) ==
                            old_color) {
                            if (detect_x < x_r) {
                                if (get_pixel_abgr(pixel_arr, detect_x + 1, seed.y + 1,
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
                        if (get_pixel_abgr(pixel_arr, detect_x, seed.y - 1u, bitmap_width) ==
                            old_color) {
                            if (detect_x < x_r) {
                                if (get_pixel_abgr(pixel_arr, detect_x + 1, seed.y - 1u,
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
            }
            fill_line_abgr(pixel_arr, seed.x, seed.y, color, bitmap_width);
        }
        else {
            break;
        }
    }
}

void fill_line_right_rgb565(uint16_t* pixel_arr, uint32_t x, uint32_t y,
                          uint16_t color, uint32_t width) {
    uint16_t old_color = get_pixel_rgb565(pixel_arr, x, y, width);
    if (old_color != color) {
        for(;;) {
            if (x < width) {
                if(get_pixel_rgb565(pixel_arr, x, y, width) == old_color) {
                    set_pixel_rgb565(pixel_arr, x, y, color, width);
                    x ++;
                }
                else {
                    break;
                }
            }
            else {
                break;
            }
        }
    }
}

void fill_line_left_rgb565(uint16_t* pixel_arr, uint32_t x, uint32_t y,
                         uint16_t color, uint32_t width) {
    uint16_t old_color = get_pixel_rgb565(pixel_arr, x, y, width);
    if (old_color != color) {
        for(;;) {
            if (get_pixel_rgb565(pixel_arr, x, y, width) == old_color) {
                set_pixel_rgb565(pixel_arr, x, y, color, width);
                if (x == 0u) {
                    break;
                }
                x -= 1u;
            }
            else {
                break;
            }
        }
    }
}

void fill_line_rgb565(uint16_t* pixel_arr, uint32_t x, uint32_t y, uint16_t color, uint32_t width) {
    if (x + 1 < width) {
        if (get_pixel_rgb565(pixel_arr, x + 1, y, width) == get_pixel_rgb565(pixel_arr, x, y, width)) {
            fill_line_right_rgb565(pixel_arr, x + 1, y, color, width);
        }
    }
    fill_line_left_rgb565(pixel_arr, x, y, color, width);
}

void fill_rgb565(uint16_t* pixel_arr, uint32_t x, uint32_t y, uint16_t color,
               uint32_t bitmap_width, uint32_t bitmap_height) {
    stack<point> point_stack;
    point seed;
    if (get_pixel_rgb565(pixel_arr, x, y, bitmap_width) != color) {
        point_stack.push(point{x, y});
    }
    for (;;) {
        if (!point_stack.empty()) {
            seed = point_stack.top();
            point_stack.pop();
            uint16_t old_color = get_pixel_rgb565(pixel_arr, seed.x, seed.y, bitmap_width);
            uint32_t x_l = seed.x;
            uint32_t x_r = seed.x;
            if (old_color != color) {
                for (;;) {
                    if (x_l >= 1) {
                        if (get_pixel_rgb565(pixel_arr, x_l - 1u, seed.y, bitmap_width) != old_color) {
                            break;
                        }
                        x_l -= 1u;
                    }
                    else {
                        break;
                    }
                }
                for (;;) {
                    if (x_r + 1 < bitmap_width) {
                        if (get_pixel_rgb565(pixel_arr, x_r + 1, seed.y, bitmap_width) != old_color) {
                            break;
                        }
                        x_r ++;
                    }
                    else {
                        break;
                    }
                }
                for (uint32_t detect_x = x_l; detect_x <= x_r; detect_x ++) {
                    if (seed.y + 1 < bitmap_height) {
                        if (get_pixel_rgb565(pixel_arr, detect_x, seed.y + 1, bitmap_width) ==
                            old_color) {
                            if (detect_x < x_r) {
                                if (get_pixel_rgb565(pixel_arr, detect_x + 1, seed.y + 1,
                                                   bitmap_width) != old_color) {
                                    point_stack.push(point{detect_x, seed.y + 1});
                                }
                            }
                            else {
                                point_stack.push(point{detect_x, seed.y + 1});
                            }
                        }
                    }
                    if (seed.y >= 1u) {
                        if (get_pixel_rgb565(pixel_arr, detect_x, seed.y - 1u, bitmap_width) ==
                            old_color) {
                            if (detect_x < x_r) {
                                if (get_pixel_rgb565(pixel_arr, detect_x + 1, seed.y - 1u,
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
            }
            fill_line_rgb565(pixel_arr, seed.x, seed.y, color, bitmap_width);
        }
        else {
            break;
        }
    }
}

void fill_line_right_alpha8(u_char* pixel_arr, uint32_t x, uint32_t y,
                            u_char color, uint32_t width) {
    u_char old_color = get_pixel_alpha8(pixel_arr, x, y, width);
    if (old_color != color) {
        for(;;) {
            if (x < width) {
                if(get_pixel_alpha8(pixel_arr, x, y, width) == old_color) {
                    set_pixel_alpha8(pixel_arr, x, y, color, width);
                    x ++;
                }
                else {
                    break;
                }
            }
            else {
                break;
            }
        }
    }
}

void fill_line_left_alpha8(u_char* pixel_arr, uint32_t x, uint32_t y,
                           u_char color, uint32_t width) {
    u_char old_color = get_pixel_alpha8(pixel_arr, x, y, width);
    if (old_color != color) {
        for(;;) {
            if (get_pixel_alpha8(pixel_arr, x, y, width) == old_color) {
                set_pixel_alpha8(pixel_arr, x, y, color, width);
                if (x == 0u) {
                    break;
                }
                x -= 1u;
            }
            else {
                break;
            }
        }
    }
}

void fill_line_alpha8(u_char* pixel_arr, uint32_t x, uint32_t y, u_char color, uint32_t width) {
    if (x + 1 < width) {
        if (get_pixel_alpha8(pixel_arr, x + 1, y, width) == get_pixel_alpha8(pixel_arr, x, y, width)) {
            fill_line_right_alpha8(pixel_arr, x + 1, y, color, width);
        }
    }
    fill_line_left_alpha8(pixel_arr, x, y, color, width);
}

void fill_alpha8(u_char* pixel_arr, uint32_t x, uint32_t y, u_char color,
                 uint32_t bitmap_width, uint32_t bitmap_height) {
    stack<point> point_stack;
    point seed;
    if (get_pixel_alpha8(pixel_arr, x, y, bitmap_width) != color) {
        point_stack.push(point{x, y});
    }
    for (;;) {
        if (!point_stack.empty()) {
            seed = point_stack.top();
            point_stack.pop();
            u_char old_color = get_pixel_alpha8(pixel_arr, seed.x, seed.y, bitmap_width);
            uint32_t x_l = seed.x;
            uint32_t x_r = seed.x;
            if (old_color != color) {
                for (;;) {
                    if (x_l >= 1) {
                        if (get_pixel_alpha8(pixel_arr, x_l - 1u, seed.y, bitmap_width) != old_color) {
                            break;
                        }
                        x_l -= 1u;
                    }
                    else {
                        break;
                    }
                }
                for (;;) {
                    if (x_r + 1 < bitmap_width) {
                        if (get_pixel_alpha8(pixel_arr, x_r + 1, seed.y, bitmap_width) != old_color) {
                            break;
                        }
                        x_r ++;
                    }
                    else {
                        break;
                    }
                }
                for (uint32_t detect_x = x_l; detect_x <= x_r; detect_x ++) {
                    if (seed.y + 1 < bitmap_height) {
                        if (get_pixel_alpha8(pixel_arr, detect_x, seed.y + 1, bitmap_width) ==
                            old_color) {
                            if (detect_x < x_r) {
                                if (get_pixel_alpha8(pixel_arr, detect_x + 1, seed.y + 1,
                                                     bitmap_width) != old_color) {
                                    point_stack.push(point{detect_x, seed.y + 1});
                                }
                            }
                            else {
                                point_stack.push(point{detect_x, seed.y + 1});
                            }
                        }
                    }
                    if (seed.y >= 1u) {
                        if (get_pixel_alpha8(pixel_arr, detect_x, seed.y - 1u, bitmap_width) ==
                            old_color) {
                            if (detect_x < x_r) {
                                if (get_pixel_alpha8(pixel_arr, detect_x + 1, seed.y - 1u,
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
            }
            fill_line_alpha8(pixel_arr, seed.x, seed.y, color, bitmap_width);
        }
        else {
            break;
        }
    }
}