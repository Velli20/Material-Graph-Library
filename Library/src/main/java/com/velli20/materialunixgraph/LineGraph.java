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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;


import java.util.ArrayList;

public class LineGraph extends Graph {
    private static final String DEFAULT_LINE_COLOR = "#00b0ff";
    private static final String DEFAULT_DIALOG_TEXT_COLOR = "#9e9e9e";
    private static final String DEFAULT_DIALOG_OUTLINE_COLOR = "#757575";
    private static final String DEFAULT_DIALOG_BACKGROUND_COLOR = "#FFFFFF";
    private static final String DEFAULT_USER_TOUCH_INDICATOR_LINE_COLOR = "#757575";

    private static final int DEFAULT_DIALOG_TEXT_SIZE = 12;

    private static final float DEFAULT_TOUCH_POINT_LINE_STROKE_WIDTH = 1;
    private static final float DEFAULT_LINE_STROKE_WIDTH = 1.5f;
    private static final float DEFAULT_TOUCH_POINT_RADIUS= 6;

    private static final long ONE_DAY_IN_MILLIS = 1000 * 60 * 60 * 24;


    private int mGraphLinePointRadius;
    private int mGraphLineTouchingPointRadius;
    private int mGraphLineTouchedPointColor;
    private int mGraphLineTouchedIndicatorLineColor;
    private int mDialogOutlineColor;
    private int mDialogBackgroundColor;

    private Paint mGraphLinePaint = new Paint();
    private Paint mGraphLinePointPaint = new Paint();
    private Paint mDialogFramePaint = new Paint();
    private Paint mUserTouchPointLinePaint = new Paint();
    private TextPaint mDialogTextPaint = new TextPaint();

    private OnLinePointTouchListener mListener;

    private PointF mUserTouchingPoint = new PointF();

    private boolean mUserTouching = false;
    private boolean mDrawUserTouchPoint = true;
    private boolean mDrawGraphLineTouchedIndicatorLine = true;

    private ArrayList<Line> mLines = new ArrayList<>();
    private ArrayList<PathHolder> mPathHolders;


    private LinePointDialog mDialog;

    public LineGraph(Context context) {
        super(context);
        initLineGraph(context, null);
    }

    public LineGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLineGraph(context, attrs);
    }

    public LineGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLineGraph(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LineGraph(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initLineGraph(context, attrs);
    }

    public void initLineGraph(Context context, AttributeSet attrs){
        mGraphLineTouchingPointRadius = (int) getDpValue(DEFAULT_TOUCH_POINT_RADIUS);
        mGraphLinePointRadius = (int) getDpValue(DEFAULT_TOUCH_POINT_RADIUS);
        mGraphLineTouchedPointColor = -1;
        mGraphLineTouchedIndicatorLineColor = Color.parseColor(DEFAULT_USER_TOUCH_INDICATOR_LINE_COLOR);
        mDialogOutlineColor = Color.parseColor(DEFAULT_DIALOG_OUTLINE_COLOR);
        mDialogBackgroundColor = Color.parseColor(DEFAULT_DIALOG_BACKGROUND_COLOR);

        if(attrs != null && context != null) {
            TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LineGraph, 0, 0);

            try {
                mGraphLineTouchingPointRadius = attributes.getDimensionPixelSize(R.styleable.LineGraph_lineTouchedPointRadius, mGraphLineTouchingPointRadius);
                mGraphLinePointRadius = attributes.getDimensionPixelSize(R.styleable.LineGraph_linePointRadius, mGraphLinePointRadius);
                mGraphLineTouchedPointColor = attributes.getColor(R.styleable.LineGraph_lineTouchedPointColor, -1);
                mGraphLineTouchedIndicatorLineColor = attributes.getColor(R.styleable.LineGraph_lineTouchedPointIndicatorLineColor, mGraphLineTouchedIndicatorLineColor);
                mDrawGraphLineTouchedIndicatorLine = attributes.getBoolean(R.styleable.LineGraph_drawLineTouchedPointIndicatorLine, true);
                mDialogOutlineColor = attributes.getColor(R.styleable.LineGraph_dialogOutlineColor, mDialogOutlineColor);
                mDialogBackgroundColor = attributes.getColor(R.styleable.LineGraph_dialogBackgroundColor, mDialogBackgroundColor);
            } finally {
                attributes.recycle();
            }
        }
        /* Paint that is used for graph lines */
        mGraphLinePaint.setColor(Color.parseColor(DEFAULT_LINE_COLOR));
        mGraphLinePaint.setAntiAlias(true);
        mGraphLinePaint.setStyle(Paint.Style.STROKE);
        mGraphLinePaint.setStrokeWidth(getDpValue(DEFAULT_LINE_STROKE_WIDTH));
        mGraphLinePaint.setStrokeCap(Paint.Cap.ROUND);

        /* Paint for drawing the line points */
        mGraphLinePointPaint.setAntiAlias(true);
        mGraphLinePointPaint.setStyle(Paint.Style.FILL);


        /* Text paint for drawing dialog content */
        mDialogTextPaint.setColor(Color.parseColor(DEFAULT_DIALOG_TEXT_COLOR));
        mDialogTextPaint.setTextSize(getDpValue(DEFAULT_DIALOG_TEXT_SIZE));
        mDialogTextPaint.setAntiAlias(true);
        mDialogTextPaint.setTextAlign(Paint.Align.LEFT);

        /* Paint for drawing dialog outline and background */
        mDialogFramePaint.setAntiAlias(true);

        /* Paint for drawing dash path line vertically across selected point */
        mUserTouchPointLinePaint.setAntiAlias(true);
        mUserTouchPointLinePaint.setColor(mGraphLineTouchedIndicatorLineColor);
        mUserTouchPointLinePaint.setStyle(Paint.Style.STROKE);
        mUserTouchPointLinePaint.setStrokeWidth(getDpValue(DEFAULT_TOUCH_POINT_LINE_STROKE_WIDTH));

        if(!isInEditMode()) {
            Typeface roboto = Typeface.createFromAsset(getResources().getAssets(), "Roboto-Regular.ttf");
            if(roboto != null) {
                mDialogTextPaint.setTypeface(roboto);
            }
        }

        /* Draw dummy line if the view is in edit mode */
        if(isInEditMode()) {
            setStartDate(0);
            setEndDate(ONE_DAY_IN_MILLIS);

            setMaxVerticalAxisValue(90);
            setMinVerticalAxisValue(0);

            Line mLine = new Line();
            mLine.setFillAlpha(60);
            mLine.setFillLine(true);
            mLine.addPoint(new LinePoint(0, 8));
            mLine.addPoint(new LinePoint(getGraphEndDate() / 4, 32));
            mLine.addPoint(new LinePoint(getGraphEndDate()  / 3, 61));
            mLine.addPoint(new LinePoint(getGraphEndDate()  / 2, 50));
            mLine.addPoint(new LinePoint(getGraphEndDate() , 40));
            mLines.add(mLine);

        }
        setWillNotDraw(false);
    }

    public void addLine(Line line) {
        if(line != null) {
            mLines.add(line);
        }
        calculateHorizontalAxisMinMax();

        calculateLinePaths(mLines);
        invalidate();
    }

    public void removeLines() {
        mLines.clear();
        if(mPathHolders != null) {
            mPathHolders.clear();
        }
        invalidate();
    }

    private void calculateHorizontalAxisMinMax() {

        for(Line line : mLines) {
            if(line.getPoints() == null || line.getPoints().isEmpty()) {
                continue;
            }
            int points = line.getPoints().size();

            long newStartDay = line.getPoints().get(0).getX();
            long newEndDay = line.getPoints().get(points -1).getX();
            long currentStartDate = getGraphStartDate();

            if(currentStartDate == 0 || (currentStartDate > 0 && newStartDay < currentStartDate)) {
                setStartDate(newStartDay);
            }
            if(newEndDay > getGraphEndDate()) {
                setEndDate(newEndDay);
            }
        }
    }

    @Override
    public void setStartDate(long startDateInMillis) {
        super.setStartDate(startDateInMillis);
        calculateLinePaths(mLines);
        invalidate();
    }

    @Override
    public void setEndDate(long endDateInMillis) {
        super.setEndDate(endDateInMillis);
        calculateLinePaths(mLines);
        invalidate();
    }

    @Override
    public void setMaxVerticalAxisValue(float max) {
        super.setMaxVerticalAxisValue(max);
        calculateLinePaths(mLines);
    }

    @Override
    public void setMinVerticalAxisValue(float min) {
        super.setMinVerticalAxisValue(min);
        calculateLinePaths(mLines);
    }

    public void setLinePointCircleRadius(float radiusInDp) {
        mGraphLinePointRadius = (int) getDpValue(radiusInDp);
        calculateLinePaths(mLines);
        invalidate();
    }

    public void setUserTouchPointCircleRadius(float radiusInDp) {
        mGraphLineTouchingPointRadius = (int) getDpValue(radiusInDp);
        invalidate();
    }

    public void setDrawUserTouchPointEnabled(boolean enable) {
        mDrawUserTouchPoint = enable;
        invalidate();
    }

    public void setOnLinePointTouchListener(OnLinePointTouchListener l) {
        mListener = l;
        if(l != null) {
            setDrawUserTouchPointEnabled(true);
        }
    }

    public void drawDialog(LinePointDialog dialog) {
        mDialog = dialog;
        invalidate();
    }

    public void dismissDialog() {
        mDialog = null;
        invalidate();
    }

    public void clearTouchingPoint() {
        mUserTouching = false;
        invalidate();
    }

    public boolean isUserTouchEnabled() { return mDrawUserTouchPoint; }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        calculateLinePaths(mLines);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float leftPadding = getPaddingLeft();
        float bottomPadding = getPaddingBottom();
        float topPadding = getPaddingTop();

        float verticalAxisLabelTextSize = getVerticalAxisLabelPaint().getTextSize();
        float graphFrameStrokeWidth = getGraphFramePaint().getStrokeWidth();

        float cyStart = topPadding + verticalAxisLabelTextSize;
        float cyEnd = getHeight() - (getHorizontalAxisLabelPadding() + bottomPadding);

        float cxStart = leftPadding;
        float cxEnd = getWidth() - (getPaddingRight() + graphFrameStrokeWidth);

        float graphCxStart = cxStart + getVerticalAxisLabelPadding();

        if(mPathHolders != null) {
            /* Draw the graph lines */
            drawGraphLines(canvas, mPathHolders, cyStart, cyEnd, graphCxStart + graphFrameStrokeWidth, cxEnd - graphFrameStrokeWidth);
        }

        if(mDialog != null) {
            drawDialog(canvas, mDialog, cyStart, cyEnd, graphCxStart + graphFrameStrokeWidth, cxEnd - graphFrameStrokeWidth);
        }
    }

    private void drawDialog(Canvas canvas, LinePointDialog dialog, float cyStart, float cyEnd, float cxStart, float cxEnd) {
        if(dialog == null || canvas == null) {
            return;
        }
        String title = dialog.getTitle();
        String content = dialog.getContent();

        if(title == null && content == null) {
            return;
        }

        float dialogPadding = getDpValue(8);
        float dialogHeight = dialogPadding * 2;
        float dialogWidth = dialogPadding * 2;
        float titleWidth = 0;
        float titleHeight = 0;
        float contentWidth = 0;
        float offsetY = Math.max(mGraphLinePointRadius * 3, mGraphLineTouchingPointRadius * 3);
        float maxVerticalValue = getMaxVerticalAxisValue();
        float minVerticalAxisValue = getMinVerticalAxisValue();
        long startDate = getGraphStartDate();
        long endDate = getGraphEndDate();

        StaticLayout titleLayout = null;
        StaticLayout contentLayout = null;

        if(title != null && !title.isEmpty()) {
            titleLayout = new StaticLayout(title, mDialogTextPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            titleHeight = titleLayout.getHeight();
            titleWidth = getMultilineTextWidth(titleLayout);
            dialogHeight += titleHeight;
        }
        if(content != null && !content.isEmpty()) {
            contentLayout = new StaticLayout(content, mDialogTextPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            dialogHeight += contentLayout.getHeight();
            contentWidth = getMultilineTextWidth(contentLayout);
        }

        dialogWidth += Math.max(titleWidth, contentWidth);

        /* Calculate position on canvas */
        float horizontalPosition = (float)(((dialog.getHorizontalPosition() - startDate) * (cxEnd - cxStart) / (endDate - startDate) + cxStart) - (dialogWidth / 2));
        float verticalPosition = ((dialog.getVerticalPosition() - maxVerticalValue) * (cyEnd - cyStart) / (minVerticalAxisValue - maxVerticalValue) + cyStart) - offsetY;

        RectF dialogRect = new RectF();
        dialogRect.set(horizontalPosition, (verticalPosition - dialogHeight), (horizontalPosition + dialogWidth), verticalPosition);

        if(dialogRect.right > cxEnd) {
            dialogRect.right = cxEnd;
            dialogRect.left = (dialogRect.right - dialogWidth);
        } else if(dialogRect.left < cxStart) {
            dialogRect.left = cxStart;
            dialogRect.right = (dialogRect.left + dialogWidth);
        }

        if(dialogRect.bottom > cyEnd) {
            dialogRect.bottom = cyEnd;
            dialogRect.top = (dialogRect.bottom - dialogHeight);
        } else if(dialogRect.top < cyStart) {
            /* Align dialog below touching point */
            dialogRect.top = verticalPosition + (offsetY * 2);
            dialogRect.bottom = (dialogRect.top + dialogHeight);
        }


        /* Draw background of the dialog */
        mDialogFramePaint.setColor(mDialogBackgroundColor);
        mDialogFramePaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(dialogRect, getDpValue(2), getDpValue(2), mDialogFramePaint);

        /* Draw outline of the dialog */
        mDialogFramePaint.setColor(mDialogOutlineColor);
        mDialogFramePaint.setStyle(Paint.Style.STROKE);
        mDialogFramePaint.setStrokeWidth(getDpValue(2));
        canvas.drawRoundRect(dialogRect, getDpValue(2), getDpValue(2), mDialogFramePaint);

        if(title != null && titleLayout != null) {
            /* Draw title */
            mDialogTextPaint.setColor(dialog.getTitleColor());
            canvas.save();
            canvas.translate(dialogRect.left + dialogPadding, dialogRect.top + dialogPadding);
            titleLayout.draw(canvas);
            canvas.restore();
        }
        if(content != null && contentLayout != null) {
            mDialogTextPaint.setColor(dialog.getContentColor());
            canvas.save();
            canvas.translate(dialogRect.left + dialogPadding, dialogRect.top + dialogPadding + titleHeight);
            contentLayout.draw(canvas);
            canvas.restore();
        }


    }

    private float getMultilineTextWidth(StaticLayout textLayout) {
        if(textLayout == null) {
            return 0;
        }

        int count = textLayout.getLineCount();
        float length = 0;
        for (int i = 0; i < count; i++) {
            length = Math.max(length, textLayout.getLineWidth(i));
        }

        return length;
    }



    private void drawGraphLines(Canvas canvas, ArrayList<PathHolder> lines, float cyStart, float cyEnd, float cxStart, float cxEnd) {

        if(lines == null || lines.isEmpty()) {
            return;
        }

        for(PathHolder holder : lines) {
            if (holder == null) {
                continue;
            }

            mGraphLinePointPaint.setColor(holder.color);
            mGraphLinePointPaint.setAlpha(255);
            mGraphLinePaint.setStyle(Paint.Style.STROKE);
            mGraphLinePaint.setColor(holder.color);
            mGraphLinePaint.setAlpha(255);
            mGraphLinePaint.setStrokeWidth(getDpValue(holder.strokeWidth));

            canvas.drawPath(holder.pathOutline, mGraphLinePaint);

            if (holder.pathFill != null) {
                mGraphLinePaint.setStyle(Paint.Style.FILL);
                mGraphLinePaint.setAlpha(holder.fillAlpha);
                canvas.drawPath(holder.pathFill, mGraphLinePaint);
            }

            mGraphLinePointPaint.setAlpha(255);
            if(holder.points != null && !holder.points.isEmpty()) {

                for(PointF point : holder.points) {
                    canvas.drawCircle(point.x, point.y, mGraphLinePointRadius, mGraphLinePointPaint);
                }
            }
            if (holder.selected && holder.touchingPoint != null) {
                if(mDrawGraphLineTouchedIndicatorLine) {

                    drawVerticalDashPathLine(canvas, getDpValue(1.5f), holder.touchCy, cyEnd, holder.touchCx, mUserTouchPointLinePaint);
                }
                if(mGraphLineTouchedPointColor != -1) {
                    mGraphLinePointPaint.setColor(mGraphLineTouchedPointColor);
                }
                mGraphLinePointPaint.setAlpha(255);
                canvas.drawCircle(holder.touchCx, holder.touchCy, mGraphLineTouchingPointRadius, mGraphLinePointPaint);
                mGraphLinePointPaint.setAlpha(27);
                canvas.drawCircle(holder.touchCx, holder.touchCy, mGraphLineTouchingPointRadius * 2.5f, mGraphLinePointPaint);

                mGraphLinePointPaint.setColor(holder.color);
                if (mListener != null) {
                    mListener.onLinePointClicked(holder.line, holder.touchingPoint);
                }
            }



        }

    }


    private void calculateLinePaths(ArrayList<Line> lines) {
        if(lines == null || lines.isEmpty()) {
            return;
        }
        float leftPadding = getPaddingLeft();
        float bottomPadding = getPaddingBottom();
        float topPadding = getPaddingTop();
        float verticalAxisLabelTextSize = getVerticalAxisLabelPaint().getTextSize();

        float graphFrameStrokeWidth = getGraphFramePaint().getStrokeWidth();

        float cyStart = topPadding + verticalAxisLabelTextSize;
        float cyEnd = getHeight() - (getHorizontalAxisLabelPadding() + bottomPadding + (graphFrameStrokeWidth / 2)) ;
        float cxStart = leftPadding + getVerticalAxisLabelPadding();
        float cxEnd = getWidth() - (getPaddingRight() + graphFrameStrokeWidth);

        float maxVerticalValue = getMaxVerticalAxisValue();
        float minVerticalAxisValue = getMinVerticalAxisValue();

        long startDate = getGraphStartDate();
        long endDate = getGraphEndDate();


        ArrayList<PathHolder> pathsToDraw = new ArrayList<>();


        float previousTouchingDeltaX = -1;
        PathHolder holderOfTouchedPath = null;

        for (Line line : lines) {

            if (line != null && line.getPoints() != null && !line.getPoints().isEmpty()) {
                PathHolder holder = new PathHolder();
                holder.line = line;
                holder.color = line.getLineColor();
                holder.fillAlpha = line.getLineFillAlpha();
                holder.strokeWidth = line.getLineStrokeWidth();
                holder.pathOutline = new Path();

                boolean moveToFirst = false;
                boolean lineIsOutOfBounds = false;

                float lineCy;
                float lineCx = 0;
                float lineCxStart = -1;
                float lineCyStart = -1;


                for (LinePoint point : line.getPoints()) {
                    float xPercent = (((point.getX() - startDate) * 1.0f) / ((endDate - startDate) * 1.0f));

                    lineCy = (point.getY() - minVerticalAxisValue) * (cyStart - cyEnd) / (maxVerticalValue - minVerticalAxisValue) + cyEnd;
                    lineCx = (xPercent * (cxEnd - cxStart)) + cxStart;

                    /* Avoid drawing below horizontal axis */
                    if(lineCy > cyEnd) {
                        lineCy = cyEnd;
                    } else if(lineCy < cyStart) {
                        lineCy = cyStart;
                    }
                    /* Avoid drawing outside vertical axis lines */
                    if(lineCx < cxStart) {
                        /* Line x value is less than x start value. Skip this iteration */
                        continue;
                    } else if(lineCx > cxEnd) {
                        /* Line x value is higher than x end value. Stop iteration after this iteration*/
                        lineCx = cxEnd;
                        lineIsOutOfBounds = true;
                    }

                    if(lineCxStart == -1) {
                        lineCxStart = lineCx;
                    }
                    if(lineCyStart == -1) {
                        lineCyStart = lineCy;
                    }
                    /* Calculate the closest point related to user touching point on graph */
                    if (mUserTouching) {
                        float touchingDeltaX = (lineCx - mUserTouchingPoint.x);

                        if (touchingDeltaX < 0) {
                            /* Invert if touchingDeltaX value is negative */
                            touchingDeltaX = touchingDeltaX * -1;
                        }
                        if (holderOfTouchedPath == null || touchingDeltaX < previousTouchingDeltaX ) {

                            /* If new deltaX is less than previous, then use this point as touching point */
                            if(holderOfTouchedPath != null) {
                                holderOfTouchedPath.selected = false;
                                holderOfTouchedPath.touchingPoint = null;
                            }

                            previousTouchingDeltaX = touchingDeltaX;
                            holderOfTouchedPath = holder;
                            holder.selected = true;

                            holderOfTouchedPath.touchingPoint = point;
                            holderOfTouchedPath.touchCx = lineCx;
                            holderOfTouchedPath.touchCy = lineCy;

                        }
                    }
                    if(point.getDrawPoint() && !lineIsOutOfBounds) {
                        holder.points.add(new PointF(lineCx, lineCy));
                    }
                    if (!moveToFirst) {

                        holder.pathOutline.moveTo(lineCx, lineCy);
                        moveToFirst = true;
                    }

                    holder.pathOutline.lineTo(lineCx, lineCy);

                    if(lineIsOutOfBounds) {
                        break;
                    }
                }


                if(line.getFillLine()) {
                    holder.pathFill = new Path(holder.pathOutline);

                    holder.pathFill.lineTo(lineCx, cyEnd);
                    holder.pathFill.lineTo(lineCxStart, cyEnd);
                    holder.pathFill.lineTo(lineCxStart, lineCyStart);
                    holder.pathFill.moveTo(lineCxStart, lineCyStart);

                    holder.pathFill.close();
                }

                pathsToDraw.add(holder);

            }

        }

        mPathHolders = pathsToDraw;
    }

    private void drawVerticalDashPathLine(Canvas canvas, float dotSize, float startY, float endY, float cx, Paint paint) {
        float height = endY - startY;

        float interval = (dotSize * 3);
        int dotCount = (int)(height / interval);

        float extraPadding = (height % interval) / dotCount;
        float cy = startY;

        for(int i = 0; i <= dotCount; i++) {

            canvas.drawPoint(cx, cy, paint);
            cy += interval + extraPadding;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if(!mDrawUserTouchPoint) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:
                return false;
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                mUserTouchingPoint.x = event.getX();
                mUserTouchingPoint.y = event.getY();
                mUserTouching = true;
                calculateLinePaths(mLines);
                invalidate();
                return true;

        }
        return super.onTouchEvent(event);
    }

    private class PathHolder {
        Line line;
        Path pathFill;
        Path pathOutline;

        LinePoint touchingPoint;
        boolean selected = false;
        int color;
        int fillAlpha;
        float strokeWidth;

        float touchCy;
        float touchCx;

        ArrayList<PointF> points = new ArrayList<>();
    }


}
