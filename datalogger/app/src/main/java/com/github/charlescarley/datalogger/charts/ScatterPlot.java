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
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.github.charlescarley.datalogger.Utils;


public class ScatterPlot extends BaseChart
{

    public ScatterPlot( Context context, @Nullable AttributeSet attrs )
    {

        super(context, attrs);
        m_xAxis.reset();
        m_yAxis.reset();
    }


    @Override
    protected void onDraw( Canvas canvas )
    {

        super.onDraw(canvas);
        beginDraw(canvas);

        if (m_points != null)
        {
            for (Point ele : m_points)
            {
                float x = m_xAxis.transformPoint(ele.x);
                float y = m_yAxis.transformPoint(ele.y);

                if (!canvas.quickReject(x - Constants.HalfPointSize,
                                        y - Constants.HalfPointSize,
                                        x + Constants.HalfPointSize,
                                        y + Constants.HalfPointSize,
                                        Canvas.EdgeType.BW))
                {
                    if (m_orderedPairs)
                    {
                        displayLabel(canvas,
                                     Utils.orderedPairFormat(ele.x, ele.y),
                                     x + 14, -y);
                    }

                    m_paint.setColor((int) ele.c);
                    canvas.drawCircle(x, y, Constants.PointSize, m_paint);
                }
            }
        }
        endDraw(canvas);
    }

}
