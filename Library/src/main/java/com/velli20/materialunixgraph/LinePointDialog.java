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


public class LinePointDialog {
    private long mHorizontalPosition;
    private float mVerticalPosition;

    private String mTitle;
    private String mContentText;

    private int mTitleColor;
    private int mContentTextColor;


    public LinePointDialog(long horizontalPosition, float verticalPosition) {
        mHorizontalPosition = horizontalPosition;
        mVerticalPosition = verticalPosition;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setContentText(String contentText) {
        mContentText = contentText;
    }

    public void setTitleColor(int color) {
        mTitleColor = color;
    }

    public void setContentColor(int contentColor) {
        mContentTextColor = contentColor;
    }

    public String getTitle() { return mTitle; }

    public String getContent() { return mContentText; }

    public int getTitleColor() { return mTitleColor; }

    public int getContentColor() { return mContentTextColor; }

    public long getHorizontalPosition() { return mHorizontalPosition; }

    public float getVerticalPosition() { return mVerticalPosition; }
}
