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
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.github.charlescarley.datalogger.Utils;
import com.github.charlescarley.datalogger.room.CollectionElement;

import java.util.ArrayList;
import java.util.List;


public class AreaChart extends BaseChart
{
    List<ArrayList<Point>> m_subSort;

    private Path m_path;


    public AreaChart( Context context, @Nullable AttributeSet attrs )
    {

        super(context, attrs);
        m_path    = new Path();
        m_subSort = null;
    }


    @Override
    public void onSetElements( List<CollectionElement> elements )
    {


        super.onSetElements(elements);

        if (m_points != null && !m_points.isEmpty())
        {

            m_subSort = new ArrayList<>();

            int lastColor = (int) m_points.get(0).c;
            ArrayList<Point> subPoint = new ArrayList<>();


            for (Point ele : m_points)
            {
                if (ele.c != lastColor)
                {
                    lastColor = (int) ele.c;
                    m_subSort.add(subPoint);

                    subPoint = new ArrayList<>();
                    subPoint.add(ele);
                }
                else
                    subPoint.add(ele);
            }

            if (!subPoint.isEmpty())
                m_subSort.add(subPoint);

        }
    }


    @Override
    protected void onDraw( Canvas canvas )
    {

        super.onDraw(canvas);
        m_paint.setStrokeWidth(1);
        beginDraw(canvas);

        if (m_points != null && !m_points.isEmpty())
        {
            m_path.reset();
            int i, j, k, m = m_subSort.size(), l;

            for (i = 0; i < m; ++i)
            {
                ArrayList<Point> points = m_subSort.get(i);
                ArrayList<Point> npl = null;
                if (i + 1 < m)
                    npl = m_subSort.get(i + 1);

                float min_x, min_y;
                min_x = Float.MAX_VALUE;
                min_y = Float.MAX_VALUE;

                l = points.size();
                for (j = 0; j < l; ++j)
                {
                    Point pt = points.get(j);
                    float px = m_xAxis.transformPoint(pt.x);
                    float py = m_yAxis.transformPoint(pt.y);

                    m_paint.setStyle(Paint.Style.FILL);
                    m_paint.setColor((int) pt.c);
                    m_paint.setAlpha(255);
                    canvas.drawCircle(px, py, Constants.HalfPointSize, m_paint);

                    if (m_orderedPairs)
                        displayLabel(canvas, Utils.orderedPairFormat(pt.x, pt.y), px + 14, -py);

                    if (m_path.isEmpty())
                        m_path.moveTo(px, py);
                    else
                        m_path.lineTo(px, py);

                    if (px < min_x)
                        min_x = px;
                    if (py < min_y)
                        min_y = py;

                    if (j + 1 >= l)
                    {
                        int lastColor = (int) pt.c;

                        if (npl != null)
                        {
                            for (k = npl.size() - 1; k >= 0; --k)
                            {
                                pt = npl.get(k);

                                px = m_xAxis.transformPoint(pt.x);
                                py = m_yAxis.transformPoint(pt.y);
                                m_path.lineTo(px, py);
                            }
                            m_path.close();
                        }
                        else
                        {
                            m_path.lineTo(px, min_y);
                            m_path.lineTo(
                                m_xAxis.transformPoint(points.get(0).x),
                                min_y);
                            m_path.close();
                        }

                        m_paint.setColor(lastColor);
                        m_paint.setStyle(Paint.Style.FILL);
                        m_paint.setAlpha(72);
                        canvas.drawPath(m_path, m_paint);

                        m_paint.setStyle(Paint.Style.STROKE);
                        m_paint.setAlpha(255);
                        canvas.drawPath(m_path, m_paint);
                        m_path.reset();
                    }
                }
            }
            endDraw(canvas);
        }
    }
}