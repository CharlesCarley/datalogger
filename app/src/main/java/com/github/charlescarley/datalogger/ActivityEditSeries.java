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


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.charlescarley.datalogger.repositories.SeriesRepository;
import com.github.charlescarley.datalogger.room.AdapterObjectInterface;
import com.github.charlescarley.datalogger.room.SeriesElement;
import com.github.charlescarley.datalogger.room.SetElement;

import static com.github.charlescarley.datalogger.Utils.Message;


public class ActivityEditSeries extends BaseActivityOkCancel
{

    private TextView      m_name;
    private Spinner       m_option;
    private SeriesElement m_series;
    private View          m_chooser;
    private boolean       m_edit;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_series);

        // grab the objects
        m_name    = findViewById(R.id.m_name);
        m_option  = findViewById(R.id.m_spinner);
        m_chooser = findViewById(R.id.m_colorChooser);


        m_series = null;
        m_edit   = false;

        AdapterObjectInterface el = getIntent().getParcelableExtra(Codes.ARG1);
        if (el != null)
        {
            if (el instanceof SetElement)
            {
                setActionBarTitle(R.string.act_add_new_series_title, true);

                m_series = new SeriesElement();
                m_series.setParent(el.getId());
                m_series.setColor(el.getColor());
                m_chooser.setBackgroundColor(el.getColor());
            }
            else if (el instanceof SeriesElement)
            {
                setActionBarTitle(R.string.act_edit_series_title, true, el.getName());

                m_series = (SeriesElement) el;
                m_edit   = true;

                m_name.setText(m_series.getName());
                m_option.setSelection(m_series.getType());
                m_chooser.setBackgroundColor(el.getColor());
            }
            else
            {
                Utils.LogError("getParcelableExtra, invalid type");
            }
        }
        else
        {
            Utils.LogError("getParcelableExtra, is missing the required argument");
        }
    }


    @Override
    protected boolean onOK()
    {

        String name = Utils.getTextViewText(m_name);
        if (name.isEmpty())
        {
            Message(this, R.string.toast_name_is_required);
            return false;
        }
        else
        {
            m_series.setName(name);
            m_series.setType(m_option.getSelectedItemPosition());

            if (m_edit)
            {
                SeriesRepository.get().getElementObserver().setValue(m_series);
                SeriesRepository.get().update(m_series);
            }
            else
            {
                // push it to the database
                SeriesRepository.get().push(m_series);
            }
        }
        return true;
    }


    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Codes.CODE_CANCEL)
        {
            m_series.setColor(resultCode);
            m_chooser.setBackgroundColor(resultCode);
        }
    }


    public void onChooseColor( View view )
    {

        Intent input = new Intent(this, ActivityColorChooser.class);
        input.putExtra(Codes.ARG1, m_series.getColor());
        startActivityForResult(input, 0);
    }
}
