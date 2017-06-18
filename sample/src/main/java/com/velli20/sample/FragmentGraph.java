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

package com.velli20.sample;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;

import com.velli20.sample.sample.R;
import com.velli20.materialunixgraph.Line;
import com.velli20.materialunixgraph.LineGraph;
import com.velli20.materialunixgraph.LinePoint;
import com.velli20.materialunixgraph.LinePointDialog;
import com.velli20.materialunixgraph.OnLinePointTouchListener;

public class FragmentGraph extends Fragment {
    public static final String BUNDLE_KEY_START_DATE = "start date";
    public static final String BUNDLE_KEY_END_DATE = "end date";

    private static final int GRAPH_MAX_VERTICAL_VALUE = 120;

    private LineGraph mGraph;
    private long mStarDate;
    private long mEndDate;

    private boolean mShowLinePoints = false;
    private boolean mFillLine = false;
    private boolean mShowVerticalAxisValueLines = true;
    private boolean mShowVerticalAxisLabels = true;
    private boolean mShowIn24HourFormat = true;
    private boolean mIsUserTouchEnabled = true;
    private Random mRandom = new Random();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_graph_layout, container, false);

        if(getArguments() != null) {
            mStarDate = getArguments().getLong(BUNDLE_KEY_START_DATE);
            mEndDate = getArguments().getLong(BUNDLE_KEY_END_DATE);
        }

        mGraph = (LineGraph) v.findViewById(R.id.fragment_graph_line_graph);

        Line line = getDummyLine(mStarDate, mEndDate, false);
        line.setLineColor(Color.parseColor("#00b0ff"));
        line.setFillLine(mFillLine);


        mGraph.addLine(line);

        mGraph.setOnLinePointTouchListener(new OnLinePointTouchListener() {
            @Override
            public void onLinePointClicked(Line line, LinePoint point) {
                /* User has clicked a point on the graph. Create a dialog to show above the touched point */

                LinePointDialog dialog = new LinePointDialog(point.getX(), point.getY());
                dialog.setTitle("Pseudo-Random value");
                dialog.setTitleColor(Color.parseColor("#00b0ff"));
                dialog.setContentText(String.format(Locale.getDefault(), "%s\n%.2f €", getTimeLabel(point.getX()), point.getY()));
                dialog.setContentColor(Color.parseColor("#9e9e9e"));
                mGraph.drawDialog(dialog);
            }
        });
        setHasOptionsMenu(true);
        return v;
    }

    public Line getDummyLine(long startDateInMillis, long endDateInMillis, boolean showPoints) {
        Line line = new Line();

        line.setLineStrokeWidth(2f);
        line.setFillAlpha(60);
        line.setFillLine(mFillLine);

        /* Create y-axis points for the line */
        LinePoint point;
        for (int i = 0; i < 10; i++) {
            long x = startDateInMillis + (((endDateInMillis - startDateInMillis) / 10) * i);

            point = new LinePoint(x, mRandom.nextInt(GRAPH_MAX_VERTICAL_VALUE));
            point.setDrawPoint(showPoints);

            line.addPoint(point);
        }

        return line;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            menu.findItem(R.id.action_show_line_points).setChecked(mShowLinePoints);
            menu.findItem(R.id.action_fill_line).setChecked(mFillLine);
            menu.findItem(R.id.action_show_vertical_axis_value_lines).setChecked(mShowVerticalAxisValueLines);
            menu.findItem(R.id.action_show_vertical_axis_labels).setChecked(mShowVerticalAxisLabels);
            menu.findItem(R.id.action_am_pm_mode).setChecked(!mShowIn24HourFormat);
            menu.findItem(R.id.action_user_touch_enabled).setChecked(mIsUserTouchEnabled);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Line line = null;
        mGraph.dismissDialog();
        mGraph.clearTouchingPoint();

        switch (item.getItemId()) {
            case R.id.action_show_line_points:
                mShowLinePoints = !mShowLinePoints;
                line = getDummyLine(mStarDate, mEndDate, mShowLinePoints);
                getActivity().supportInvalidateOptionsMenu();
                break;
            case R.id.action_fill_line:
                mFillLine = !mFillLine;
                line = getDummyLine(mStarDate, mEndDate, mShowLinePoints);
                getActivity().supportInvalidateOptionsMenu();
                break;
            case R.id.action_show_vertical_axis_value_lines:
                mShowVerticalAxisValueLines = !mShowVerticalAxisValueLines;
                mGraph.setDrawVerticalAxisLabelLines(mShowVerticalAxisValueLines);
                getActivity().supportInvalidateOptionsMenu();
                return true;
            case R.id.action_show_vertical_axis_labels:
                mShowVerticalAxisLabels = !mShowVerticalAxisLabels;
                mGraph.setDrawVerticalAxisLabels(mShowVerticalAxisLabels);
                getActivity().supportInvalidateOptionsMenu();
                return true;
            case R.id.action_am_pm_mode:
                mShowIn24HourFormat = !mShowIn24HourFormat;
                mGraph.setUse24HourFormat(mShowIn24HourFormat);
                getActivity().supportInvalidateOptionsMenu();
                return true;
            case R.id.action_user_touch_enabled:
                mIsUserTouchEnabled = !mIsUserTouchEnabled;
                mGraph.setDrawUserTouchPointEnabled(!item.isChecked());
                getActivity().supportInvalidateOptionsMenu();
                return true;
        }

        if (line != null) {
            mGraph.removeLines();
            mGraph.addLine(line);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String getTimeLabel(long millis) {
        DateFormat formOut = new SimpleDateFormat("E yyyy.MM.dd HH:mm", Locale.getDefault());
        return formOut.format(millis);

    }
}
