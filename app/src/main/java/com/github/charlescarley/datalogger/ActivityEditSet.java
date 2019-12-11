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

import androidx.annotation.Nullable;

import com.github.charlescarley.datalogger.repositories.SetRepository;
import com.github.charlescarley.datalogger.room.SetElement;

import static com.github.charlescarley.datalogger.Utils.Message;


public class ActivityEditSet extends BaseActivityOkCancel
{
    private SetElement m_set;
    private boolean    m_edit;
    private TextView   m_name;
    private View       m_chooser;
    private Spinner    m_chartChoice;


    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_set);

        m_set  = getIntent().getParcelableExtra(Codes.ARG1);
        m_edit = m_set != null;

        if (m_edit)
            setActionBarTitle(R.string.act_edit_set_title, true, m_set.getName());
        else
            setActionBarTitle(R.string.act_add_new_set_title, true);

        m_name        = findViewById(R.id.m_name);
        m_chooser     = findViewById(R.id.m_colorChooser);
        m_chartChoice = findViewById(R.id.m_chartType);

        if (!m_edit)
        {
            m_set = new SetElement();
            m_set.setChartType(m_chartChoice.getSelectedItemPosition());
            m_set.setColor(getColor(R.color.colorAccentDark));
        }
        else
        {
            m_name.setText(m_set.getName());
            m_chartChoice.setSelection(m_set.getChartType());
            m_chooser.setBackgroundColor(m_set.getColor());
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
            m_set.setName(name);
            m_set.setChartType(m_chartChoice.getSelectedItemPosition());

            if (m_edit)
            {
                SetRepository.get().getElementObserver().setValue(m_set);
                SetRepository.get().update(m_set);
            }
            else
                SetRepository.get().add(m_set);
            setResult(Codes.CODE_OK);
        }
        return true;
    }


    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Codes.CODE_CANCEL)
        {
            m_set.setColor(resultCode);
            m_chooser.setBackgroundColor(resultCode);
        }

    }


    public void onChooseColor( View view )
    {

        Intent input = new Intent(this, ActivityColorChooser.class);
        input.putExtra(Codes.ARG1, m_set.getColor());
        startActivityForResult(input, 0);
    }
}
