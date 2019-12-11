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
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.github.charlescarley.datalogger.Utils;


public class BarChart extends BaseChart
{

    public BarChart( Context context, @Nullable AttributeSet attrs )
    {

        super(context, attrs);
    }


    @Override
    protected void onDraw( Canvas canvas )
    {

        super.onDraw(canvas);
        m_paint.setStrokeWidth(1);
        beginDraw(canvas);


        if (m_points != null && !m_points.isEmpty())
        {
            m_paint.setStrokeWidth(1);
            m_paint.setStyle(Paint.Style.STROKE);
            m_paint.setAlpha(255);

            for (Point ele : m_points)
            {
                float px = m_xAxis.transformPoint(ele.x);
                float py = m_yAxis.transformPoint(ele.y);


                m_paint.setStyle(Paint.Style.FILL);
                m_paint.setColor((int) ele.c);
                m_paint.setAlpha(128);

                float p0 = m_yAxis.transformPoint(0);
                canvas.drawRect(px - Constants.PointSize, p0, px + Constants.PointSize, py, m_paint);

                if (m_orderedPairs)
                    displayLabel(canvas, Utils.orderedPairFormat(ele.x, ele.y), px, -py - Constants.LabelTextSize);

                m_paint.setStyle(Paint.Style.STROKE);
                m_paint.setAlpha(255);
                canvas.drawRect(px - Constants.PointSize, p0, px + Constants.PointSize, py, m_paint);
            }
        }
        endDraw(canvas);
    }
}
