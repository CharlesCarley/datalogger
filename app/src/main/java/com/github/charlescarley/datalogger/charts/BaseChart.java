/*
-------------------------------------------------------------------------------

    2019 Charles Carley.

    Advanced Programming: Java - 24569 - CSCI 310-001 - Fall 2019

-------------------------------------------------------------------------------
  This software is provided 'as-is', without any express or implied
  warranty. In no event will the authors be held liable for any damages
  arising from the use of this software.

  Permission is granted to anyone to use this software for any purpose,
  including commercial applications, and to alter it and redistribute it
  freely, subject to the following restrictions:

  1. The origin of this software must not be misrepresented; you must not
     claim that you wrote the original software. If you use this software
     in a product, an acknowledgment in the product documentation would be
     appreciated but is not required.
  2. Altered source versions must be plainly marked as such, and must not be
     misrepresented as being the original software.
  3. This notice may not be removed or altered from any source distribution.
-------------------------------------------------------------------------------
*/
package com.github.charlescarley.datalogger.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import com.github.charlescarley.datalogger.Utils;
import com.github.charlescarley.datalogger.room.CollectionElement;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseChart extends View
{
    protected ArrayList<Point> m_points;

    protected Paint  m_paint;
    protected Rect   m_margins;
    protected Rect   m_bounds;
    protected Axis   m_xAxis;
    protected Axis   m_yAxis;
    protected Matrix m_matrix;
    protected Vector m_baseScale;
    protected Vector m_setScale;
    protected Vector m_scale;
    protected Path   m_margin;

    protected boolean              m_orderedPairs;
    private   ScaleGestureDetector m_scaleGesture;

    private Vector  m_origin;
    private Vector  m_drag;
    private Vector  m_last;
    private Vector  m_dragScale;
    private Vector  m_clickPos;
    private boolean m_canDrag, m_wasScale, m_isInit;


    public BaseChart( Context context, @Nullable AttributeSet attrs )
    {

        super(context, attrs);

        m_wasScale = false;
        m_canDrag  = false;
        m_isInit   = false;
        m_points   = new ArrayList<>(128);
        m_bounds   = new Rect();
        m_paint    = new Paint();

        m_orderedPairs = false;

        m_clickPos  = new Vector();
        m_origin    = new Vector();
        m_baseScale = new Vector(Axis.SCALE, Axis.SCALE);
        m_setScale  = new Vector();
        m_dragScale = new Vector();
        m_scale     = new Vector(Axis.SCALE, Axis.SCALE);

        m_last   = new Vector();
        m_drag   = new Vector();
        m_matrix = new Matrix();
        m_xAxis  = new Axis();
        m_yAxis  = new Axis();

        m_margin       = new Path();
        m_margins      = new Rect(100, 0, 0, 50);
        m_scaleGesture = new ScaleGestureDetector(context, new ScaleDetector());
    }


    @Override
    protected void onSizeChanged( int w, int h, int oldw, int oldh )
    {

        super.onSizeChanged(w, h, oldw, oldh);
        m_isInit = false;
    }


    public void onSetElements( List<CollectionElement> elements )
    {

        if (m_points == null)
        {
            Utils.LogF("no points");
            return;
        }

        if (elements != null && !elements.isEmpty())
        {
            long start = Long.MAX_VALUE;

            // Choose the minimum of all the incoming data
            // which is sorted by color first, then by timestamp ascending
            for (CollectionElement el : elements)
            {
                long day = el.getTimestamp();
                if (start > day)
                    start = day;
            }


            for (CollectionElement col : elements)
            {
                // compute the time relative to the starting element
                float x = (float) Utils.timeDiff(start, col.getTimestamp());
                float y = (float) col.getValueDouble();

                m_xAxis.compare(x);
                m_yAxis.compare(y);

                m_points.add(new Point(x, y, col.getColor()));
            }
        }
        m_isInit = false;

        invalidate();
    }


    public void clearPoints()
    {

        m_isInit = false;
        m_points.clear();
        m_xAxis.reset();
        m_yAxis.reset();
    }


    float wrapAxis( float al, float s )
    {

        float v = 5 + Utils.fmod((al / 5) * s, 5);
        return al / v;
    }


    void displayGrid( Canvas canvas )
    {

        float x1, y1, x2, y2, iv, sv;
        float xl, yl, xr, yr;
        x1 = m_bounds.left;
        x2 = m_bounds.right;
        y1 = m_bounds.top;
        y2 = m_bounds.bottom;

        float x0 = m_xAxis.transformPoint(0);
        float y0 = m_yAxis.transformPoint(0);

        xl = (x2 - x1);
        yl = (y2 - y1);

        if (!m_points.isEmpty())
        {
            m_paint.setStrokeWidth(1);
            m_paint.setColor(Constants.MIN_GRID_LINE);

            sv = wrapAxis(yl, m_scale.y);
            yr = y1 - Utils.fmod(y1, sv);

            for (iv = yr; iv < y2; iv += sv)
            {
                canvas.drawLine(
                    x1,
                    iv,
                    x2,
                    iv,
                    m_paint);
            }

            sv = wrapAxis(xl, m_scale.x);
            xr = x1 - Utils.fmod(x1 - x0, sv);

            for (iv = xr; iv < x2; iv += sv)
            {
                canvas.drawLine(
                    iv,
                    y1,
                    iv,
                    y2,
                    m_paint);
            }
        }

        m_paint.setStrokeWidth(2);
        m_paint.setColor(Constants.MAJ_GRID_LINE);

        canvas.drawLine(
            x1,
            y0,
            x2,
            y0,
            m_paint);

        canvas.drawLine(
            x0,
            y1,
            x0,
            y2,
            m_paint);
    }


    private void getBounds( Canvas canvas, Rect rect )
    {

        canvas.getClipBounds(rect);

        rect.left += m_margins.left;
        rect.top += m_margins.top;
        rect.right -= m_margins.right;
        rect.bottom -= m_margins.bottom;
    }


    protected void beginDraw( Canvas canvas )
    {

        if (!m_isInit)
        {
            m_matrix.reset();
            canvas.setMatrix(m_matrix);
        }

        getDrawingRect(m_bounds);
        m_paint.setStyle(Paint.Style.FILL);
        m_paint.setColor(Constants.DARK);

        canvas.drawRect(m_bounds, m_paint);
        getBounds(canvas, m_bounds);

        final float x1 = m_bounds.left + Constants.DoublePointSize;
        final float x2 = m_bounds.right - Constants.DoublePointSize;
        final float y1 = m_bounds.top + Constants.DoublePointSize;
        final float y2 = m_bounds.bottom - Constants.DoublePointSize;
        final float xl = Math.max(Math.abs(x2 - x1), 2);
        final float yl = Math.max(Math.abs(y2 - y1), 2);

        m_xAxis.scaleToWindowRect(xl);
        m_yAxis.scaleToWindowRect(yl);

        m_baseScale.x = 1.f;
        m_baseScale.y = 0.5f;

        m_scale.x = m_baseScale.x + m_setScale.x;
        m_scale.y = m_baseScale.y + m_setScale.y;

        // provide a min
        if (m_scale.x < 0.001f)
            m_scale.x = 0.001f;
        if (m_scale.y < 0.001f)
            m_scale.y = 0.001f;


        m_xAxis.setScale(m_scale.x);
        m_yAxis.setScale(m_scale.y);

        m_xAxis.setOffset(m_xAxis.length());
        m_yAxis.setOffset(m_yAxis.length() / 2);

        if (!m_isInit)
        {
            m_origin.x = (m_bounds.left + m_bounds.right) / 2;
            m_origin.y = (m_bounds.top + m_bounds.bottom) / 2;

            m_origin.x += m_xAxis.transformPoint(m_xAxis.center());
            m_origin.y += m_yAxis.transformPoint(m_yAxis.center());

            m_last.x = m_origin.x;
            m_last.y = m_origin.y;
            m_isInit = true;
        }

        m_matrix.reset();
        m_matrix.preTranslate(m_origin.x, m_origin.y);
        m_matrix.preScale(1, -1);
        canvas.setMatrix(m_matrix);
        canvas.getClipBounds(m_bounds);

        displayGrid(canvas);
    }


    protected void endDraw( Canvas canvas )
    {

        m_paint.setColor(Constants.MIN_GRID_LINE);
        m_paint.setStyle(Paint.Style.FILL);
        m_paint.setAlpha(128);

        m_matrix.reset();
        canvas.setMatrix(m_matrix);

        canvas.getClipBounds(m_bounds);

        m_margin.reset();

        float x1, y1, x2, y2;
        float mx1, my2;

        x1 = m_bounds.left;
        y1 = m_bounds.top;
        x2 = m_bounds.right;
        y2 = m_bounds.bottom;

        mx1 = m_margins.left;
        my2 = m_margins.bottom;

        m_margin.moveTo(x1, y1);
        m_margin.lineTo(x1 + mx1, y1);
        m_margin.lineTo(x1 + mx1, y2 - my2);
        m_margin.lineTo(x2, y2 - my2);
        m_margin.lineTo(x2, y2);
        m_margin.lineTo(x1, y2);
        m_margin.close();

        canvas.drawPath(m_margin, m_paint);
        m_paint.setAlpha(255);

        m_paint.setColor(Constants.MAJ_GRID_LINE);
        m_paint.setStrokeWidth(1);
        m_paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(m_margin, m_paint);
        m_paint.setStyle(Paint.Style.FILL);

        m_matrix.reset();
        m_matrix.preTranslate(m_origin.x, m_origin.y);
        m_matrix.preScale(1, -1);
        canvas.setMatrix(m_matrix);
        canvas.getClipBounds(m_bounds);
        x1 = m_bounds.left;
        y1 = m_bounds.top;
        x2 = m_bounds.right;
        y2 = m_bounds.bottom;

        float iv, sv, xl, yl;
        float yr, xr;
        float x0 = m_xAxis.transformPoint(0);
        float y0 = m_yAxis.transformPoint(0);

        xl = (x2 - x1);
        yl = (y2 - y1);


        if (!m_points.isEmpty())
        {
            m_paint.setStrokeWidth(1);
            m_paint.setColor(Constants.MIN_GRID_LINE);

            sv = wrapAxis(yl, m_scale.y);
            yr = y1 - Utils.fmod(y1 - y0, sv);

            for (iv = yr; iv < y2; iv += sv)
            {
                if (iv < y1 + my2)
                    continue;

                float val = m_yAxis.reversePoint(iv);
                displayAxisLabel(canvas, val, x1, iv, mx1, true);

                canvas.drawLine(
                    x1,
                    iv,
                    x1 + mx1,
                    iv,
                    m_paint);
            }

            sv = wrapAxis(xl, m_scale.x);
            xr = x1 - Utils.fmod(x1 - x0, sv);

            for (iv = xr; iv < x2; iv += sv)
            {
                if (iv < x1 + mx1)
                    continue;

                float val = m_xAxis.reversePoint(iv);
                displayAxisLabel(canvas, val, iv, y1, my2, false);

                canvas.drawLine(
                    iv,
                    y1,
                    iv,
                    y1 + my2,
                    m_paint);
            }
        }

    }


    private void displayAxisLabel( Canvas canvas, float val, float x, float y, float l, boolean above )
    {

        canvas.save();
        canvas.scale(1, -1);
        m_paint.setTextSize(Constants.LabelTextSize);
        m_paint.setColor(Constants.LABEL);

        if (Math.abs(val) < 0.001)
            val = 0;

        String value = Utils.format("%.02f", val);
        float m = m_paint.measureText(value);
        float d = m_paint.descent();


        if (above)
            canvas.drawText(value, (x + (l - 5)) - m, -y - d, m_paint);
        else
            // display it to the right
            canvas.drawText(value, x + 5, -y - d, m_paint);

        canvas.restore();
    }


    void displayLabel( Canvas canvas, String val, float x, float y )
    {

        canvas.save();
        canvas.scale(1, -1);  // inverted y
        m_paint.setColor(Constants.LABEL);
        m_paint.setTextSize(Constants.LabelTextSize);
        canvas.drawText(val, x, y, m_paint);
        canvas.restore();
    }


    public void onMotionEvent( MotionEvent event )
    {

        if (m_canDrag)
        {
            m_drag.x = (event.getX() - m_clickPos.x);
            m_drag.y = (event.getY() - m_clickPos.y);

            m_origin.x = m_last.x + m_drag.x;
            m_origin.y = m_last.y + m_drag.y;
            invalidate();
        }
    }


    @Override
    public boolean onTouchEvent( MotionEvent event )
    {

        m_scaleGesture.onTouchEvent(event);
        if (m_scaleGesture.isInProgress())
            return true;

        switch (event.getAction())
        {
        case MotionEvent.ACTION_DOWN:
            if (!m_wasScale)
            {
                m_canDrag    = true;
                m_drag.x     = 0;
                m_drag.y     = 0;
                m_clickPos.x = event.getX();
                m_clickPos.y = event.getY();
            }
            m_wasScale = false;
            break;
        case MotionEvent.ACTION_MOVE:
            if (!m_wasScale)
                onMotionEvent(event);
            break;
        case MotionEvent.ACTION_UP:

            if (!m_wasScale)
            {
                m_drag.x  = 0;
                m_drag.y  = 0;
                m_last.x  = m_origin.x;
                m_last.y  = m_origin.y;
                m_canDrag = false;
            }
            m_wasScale = false;
            break;
        }
        return m_canDrag;
    }


    public void resetView()
    {

        m_canDrag  = false;
        m_wasScale = false;
        m_isInit   = false;
        m_last.zero();
        m_drag.zero();
        m_clickPos.zero();
        m_origin.zero();
        m_setScale.zero();
        invalidate();
    }


    public void toggleOrderedPairs()
    {

        m_orderedPairs = !m_orderedPairs;
        invalidate();
    }


    class ScaleDetector implements ScaleGestureDetector.OnScaleGestureListener
    {
        @Override
        public boolean onScale( ScaleGestureDetector detector )
        {

            m_wasScale = true;
            float max = Math.max(Math.max(m_bounds.width(), m_bounds.height()), 2);

            m_dragScale.x = (detector.getCurrentSpanX() - detector.getPreviousSpanX()) / max;
            m_dragScale.y = (detector.getCurrentSpanY() - detector.getPreviousSpanY()) / max;
            m_setScale.x += m_dragScale.x;
            m_setScale.y += m_dragScale.y;

            invalidate();
            return true;
        }


        @Override
        public boolean onScaleBegin( ScaleGestureDetector detector )
        {

            return true;
        }


        @Override
        public void onScaleEnd( ScaleGestureDetector detector )
        {

        }
    }
}



