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
package com.github.charlescarley.datalogger;


import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Locale;


public class Utils
{
    private static final String INFO_TAG = "DL_INFO";
    private static final String EXEC_TAG = "DL_EXEC";


    public static void Log( String message )
    {

        Log.i(INFO_TAG, message);
    }


    public static void Log( Exception ex )
    {

        if (ex != null)
            LogError(ex.getLocalizedMessage());
    }


    public static void LogF( String fmt, Object... argv )
    {

        Log.i(INFO_TAG,
              String.format(Locale.getDefault(), fmt, argv)
             );
    }


    static void LogError( String message )
    {

        Log.e(EXEC_TAG, message != null ? message : "unknown, message == null");
    }


    public static void LogErrorF( String fmt, Object... argv )
    {

        Log.e(EXEC_TAG,
              String.format(Locale.getDefault(), fmt, argv)
             );
    }


    static String getTextViewText( @NonNull TextView view )
    {

        return view.getText().toString().trim();
    }


    static void Message( Context ctx, int resourceId )
    {

        if (ctx != null)
        {
            try
            {
                Toast.makeText(ctx, resourceId, Toast.LENGTH_SHORT).show();
            } catch (Exception ex)
            {
                LogError("Message - threw an exception");
                Log(ex);
            }
        }
        else
        {
            LogError("Message - ctx is null");
        }
    }


    static void Message( Context ctx, String str )
    {

        if (ctx != null)
        {
            try
            {
                Toast.makeText(ctx, str, Toast.LENGTH_SHORT).show();
            } catch (Exception ex)
            {
                LogError("Message - threw an exception");
                Log(ex);
            }
        }
        else
        {
            LogError("Message - ctx is null");
        }
    }


    static LayoutInflater getInflater( @NonNull Context ctx )
    {

        LayoutInflater inflater =
            (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null)
            LogError("getInflater - failed to find inflater service");
        return inflater;
    }


    static TextView errorView( @NonNull Context ctx )
    {

        TextView error = new TextView(ctx);
        error.setTextSize(24);
        error.setText(ctx.getString(R.string.undefined));
        return error;
    }


    static void bindListener( @NonNull Menu parent,
                              int id,
                              @NonNull MenuItem.OnMenuItemClickListener listener )
    {

        MenuItem item = parent.findItem(id);
        if (item != null)
            item.setOnMenuItemClickListener(listener);
        else
            LogError("The requested menu item was not found");
    }


    static void spawn( Context ctx, Class cls, @Nullable Parcelable arg )
    {

        Intent intent = new Intent(ctx, cls);
        if (arg != null)
            intent.putExtra(Codes.ARG1, arg);

        ctx.startActivity(intent);
    }


    static String millisecondsToString( long ticks )
    {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ticks);

        calendar.set(Calendar.SECOND, 0);

        int m = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int y = calendar.get(Calendar.YEAR);
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int mm = calendar.get(Calendar.MINUTE);
        int hl = h % 12;
        if (hl == 0)
            hl += 12;

        return String.format(Locale.getDefault(),
                             "%d/%d/%d %d:%02d %s",
                             m, d, y, hl, mm, h < 12 ? "AM" : "PM");
    }


    static String millisecondsToTimeString( long ticks )
    {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ticks);

        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        int hl = h % 12;
        if (hl == 0)
            hl += 12;

        return String.format(Locale.getDefault(), "%d:%02d %s", hl, m, h < 12 ? "AM" : "PM");
    }


    static String millisecondsToDateString( long ticks )
    {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ticks);

        int m = calendar.get(Calendar.MONTH) + 1;
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int y = calendar.get(Calendar.YEAR);

        return String.format(Locale.getDefault(), "%d/%d/%d", m, d, y);
    }


    public static double timeToDays( long a )
    {

        double sec = a * 0.001;
        double hps = sec * 0.000277778;
        return hps / 24.0;
    }


    public static double timeDiffH( long a, long b )
    {

        double dif = Math.max(a, b) - Math.min(a, b);

        // 1 / 3600
        double sec = dif * 0.001;
        return sec * 0.000277778;
    }


    public static double timeDiff( long a, long b )
    {

        return timeDiffH(a, b) / 24.0;
    }


    public static float clamp( float v, float mi, float ma )
    {

        return v > ma ? ma : v < mi ? mi : v;
    }


    public static float fmod( float n, float d )
    {
        if (Math.abs(d) > 0)
        {
            float r = n / d;
            float wp = (int) r;
            float fp = (r - wp);
            return fp * d;
        }
        return 0;
    }


    public static boolean hasFractionalPart( float v )
    {
        float wp = (int) v;
        float fp = v - wp;
        return  Math.abs(fp) >= 0.01;
    }

    public static String orderedPairFormat( float x, float y )
    {
        boolean a = hasFractionalPart(x);
        boolean b = hasFractionalPart(y);
        if (a && b)
            return format("(%.02f, %.02f)", x, y);
        else if (!a && b)
            return format("(%.0f, %.02f)", x, y);
        else if (a)
            return format("(%.02f, %.0f)", x, y);
        return format("(%.0f, %.0f)", x, y);
    }

    public static String format( String s, Object... arguments )
    {

        try
        {
            return String.format(s, arguments);
        } catch (Exception e)
        {
            Log(e);
        }
        return s;
    }
}
