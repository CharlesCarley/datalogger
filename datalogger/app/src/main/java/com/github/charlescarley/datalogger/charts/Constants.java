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
import android.content.res.Resources;
import android.graphics.Color;

import com.github.charlescarley.datalogger.R;


public class Constants
{
    public static float PointSize       = 10;
    public static float DoublePointSize = PointSize * 2;
    public static float HalfPointSize   = PointSize / 2;
    public static float LabelTextSize   = 25;

    public static int ACCENT        = Color.rgb(0, 0, 0);
    public static int DARK          = Color.rgb(128, 128, 128);
    public static int LABEL         = Color.rgb(128, 128, 128);
    public static int MAJ_GRID_LINE = Color.rgb(48, 48, 48);
    public static int MIN_GRID_LINE = Color.rgb(72, 72, 72);


    public static void initializeFromContext( Context ctx )
    {

        Resources res = ctx.getResources();
        PointSize       = res.getDimension(R.dimen.PointSize);
        HalfPointSize   = PointSize / 2;
        DoublePointSize = PointSize * 2;
        LabelTextSize   = res.getDimension(R.dimen.LabelSize);

        ACCENT        = ctx.getColor(R.color.colorAccent);
        DARK          = ctx.getColor(R.color.window1);
        MAJ_GRID_LINE = ctx.getColor(R.color.window_m20);
        MIN_GRID_LINE = ctx.getColor(R.color.window_m10);
        LABEL         = ctx.getColor(R.color.window_p30);
    }
}
