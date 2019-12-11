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

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class DialogDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    private DialogDatePickerListener m_listener = null;


    @NonNull
    @Override
    public Dialog onCreateDialog( @Nullable Bundle savedInstanceState )
    {

        Calendar calendar = Calendar.getInstance();

        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        Context ctx = this.getContext();
        if (ctx != null)
            return new DatePickerDialog(ctx, this, y, m, d);
        return super.onCreateDialog(savedInstanceState);
    }


    @Override
    public void onDateSet( DatePicker view, int year, int month, int dayOfMonth )
    {

        Calendar calendar = Calendar.getInstance();

        calendar.set(year,
                     month,
                     dayOfMonth,
                     0, 0);
        calendar.set(Calendar.SECOND, 0);

        if (m_listener != null)
        {
            m_listener.onSet(calendar,
                             Utils.millisecondsToDateString(
                                 calendar.getTimeInMillis()));
        }
    }


    void setOnSetListener( DialogDatePicker.DialogDatePickerListener listener )
    {

        m_listener = listener;
    }


    interface DialogDatePickerListener
    {
        void onSet( Calendar chosen, String strVal );
    }
}