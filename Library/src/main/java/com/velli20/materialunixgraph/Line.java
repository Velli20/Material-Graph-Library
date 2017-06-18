/*
 *
 *  * MIT License
 *  *
 *  * Copyright (c) [2017] [velli20]
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */

package com.velli20.materialunixgraph;


import android.graphics.Color;

import java.util.ArrayList;

public class Line {
    private ArrayList<LinePoint> mPoints = new ArrayList<>();
    private int mColor = Color.parseColor("#00b0ff");
    private float mLineStrokeWidth = 1.5f;
    private boolean mFill = false;
    private int mFillAlpha = 255;

    public ArrayList<LinePoint> getPoints() {
        return mPoints;
    }

    public void addPoint(LinePoint point) {

        mPoints.add(point);
    }

    public void setFillLine(boolean fill) {
        mFill = fill;
    }

    public void setFillAlpha(int alpha) {
        mFillAlpha = alpha;
    }

    public void setLineColor(int color) {
        mColor = color;
    }

    public void setLineStrokeWidth(float width) {
        mLineStrokeWidth = width;
    }

    public int getLineColor() {
        return mColor;
    }

    public float getLineStrokeWidth() {
        return mLineStrokeWidth;
    }


    public boolean getFillLine() {
        return mFill;
    }

    public int getLineFillAlpha() {
        return mFillAlpha;
    }
}
