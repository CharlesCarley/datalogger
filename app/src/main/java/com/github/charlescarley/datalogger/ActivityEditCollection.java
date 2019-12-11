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

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.github.charlescarley.datalogger.models.CollectionViewModel;
import com.github.charlescarley.datalogger.room.CollectionElement;
import com.github.charlescarley.datalogger.room.SeriesElement;

import java.util.Calendar;


public class ActivityEditCollection extends BaseActivityOkCancel
{
    private EditText m_text;
    private EditText m_note;
    private Button   m_time;
    private Button   m_date;
    private Calendar m_timeLog;

    private CollectionElement   m_element;
    private CollectionViewModel m_collectionView;


    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_collection);

        m_time = findViewById(R.id.m_time);
        m_date = findViewById(R.id.m_date);
        m_text = findViewById(R.id.m_value);
        m_note = findViewById(R.id.m_note);

        m_timeLog = Calendar.getInstance();
        m_element = null;

        m_collectionView = ViewModelProviders.of(this).get(CollectionViewModel.class);
        m_element        = m_collectionView.getSingleObservable().getValue();

        if (m_element == null)
        {
            setActionBarTitle(R.string.act_new_collection_title, true);

            SeriesElement ele = getIntent().getParcelableExtra(Codes.ARG1);
            if (ele != null)
                m_element = new CollectionElement(ele);
            else
                Utils.LogF("Missing parent argument, no element created.");

            // By default use a consistent time unless explicitly y set
            m_timeLog.set(Calendar.HOUR_OF_DAY, 12);
            m_timeLog.set(Calendar.MINUTE, 0);
            m_timeLog.set(Calendar.SECOND, 0);


            long ticks = m_timeLog.getTimeInMillis();

            m_time.setText(Utils.millisecondsToTimeString(ticks));
            m_date.setText(Utils.millisecondsToDateString(ticks));
        }
        else
        {
            setActionBarTitle(R.string.act_edit_collection_title, true);

            m_text.setText(m_element.getName());
            m_time.setText(Utils.millisecondsToTimeString(m_element.getTimestamp()));
            m_date.setText(Utils.millisecondsToDateString(m_element.getTimestamp()));
            m_timeLog.setTimeInMillis(m_element.getTimestamp());
            m_note.setText(m_element.getNote());
        }

        if (m_element != null)
        {
            int common = EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_SIGNED;
            if (m_element.getType() == 0)
                m_text.setInputType(common);
            else if (m_element.getType() == 1)
                m_text.setInputType(common | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL);
        }

    }


    @Override
    protected boolean onOK()
    {


        if (m_element != null)
        {
            String text = Utils.getTextViewText(m_text);
            if (text.isEmpty())
                Utils.Message(this, R.string.toast_value_is_required);
            else
            {
                m_element.setValue(text);
                m_element.setTimestamp(m_timeLog.getTimeInMillis());


                text = Utils.getTextViewText(m_note);
                m_element.setNote(text);

                m_collectionView.getSingleObservable().setValue(m_element);
                m_collectionView.push(m_element);
            }
        }
        return true;
    }


    public void onDateClicked( View view )
    {

        DialogDatePicker picker = new DialogDatePicker();
        picker.show(getSupportFragmentManager(), getString(R.string.choose_date));
        picker.setOnSetListener(this::onDateSet);
    }


    public void onTimeClicked( View view )
    {

        DialogTimePicker picker = new DialogTimePicker();
        picker.show(getSupportFragmentManager(), getString(R.string.choose_time));
        picker.setOnSetListener(this::onTimeSet);
    }


    private void onDateSet( Calendar calendar, String dateString )
    {

        if (m_date != null)
        {
            m_date.setText(dateString);

            m_timeLog.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            m_timeLog.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            m_timeLog.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        }
    }


    private void onTimeSet( Calendar calendar, String timeString )
    {

        if (m_time != null)
        {
            m_time.setText(timeString);
            m_timeLog.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
            m_timeLog.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
            m_timeLog.set(Calendar.SECOND, 0);
        }
    }
}
