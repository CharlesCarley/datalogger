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
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.github.charlescarley.datalogger.models.SetViewModel;
import com.github.charlescarley.datalogger.repositories.CollectionRepository;
import com.github.charlescarley.datalogger.repositories.ColorRepository;
import com.github.charlescarley.datalogger.repositories.SeriesRepository;
import com.github.charlescarley.datalogger.repositories.SetRepository;
import com.github.charlescarley.datalogger.room.AdapterObjectInterface;
import com.github.charlescarley.datalogger.room.SetElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.charlescarley.datalogger.Codes.ACT_ADD;
import static com.github.charlescarley.datalogger.Utils.LogError;


public class ActivitySet extends ActivityBase implements SetListClickListener
{
    private MenuItem m_addMenuItem, m_delMenuItem;
    private ListView                   m_list;
    private SetListAdapter<SetElement> m_adapter;
    private SetViewModel               m_setViewModel;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);

        m_addMenuItem = null;
        m_delMenuItem = null;
        m_list        = null;
        m_adapter     = new SetListAdapter<>(this, this, R.layout.set_list_item);


        m_setViewModel = ViewModelProviders.of(this).get(SetViewModel.class);
        m_setViewModel.getObservable().observe(this, this::populate);
        m_setViewModel.access();
    }


    private void populate( @Nullable List<SetElement> elements )
    {

        if (elements != null && elements.isEmpty())
        {
            m_list = null;
            setContentView(R.layout.no_content);
        }
        else
        {
            m_adapter.update(elements);

            if (m_list == null)
            {
                setContentView(R.layout.activity_list_view);
                m_list = findViewById(R.id.m_list);
                if (m_list != null)
                    m_list.setAdapter(m_adapter);
                else
                    LogError("Could not find list in activity_series");
            }
            else
                m_list.setAdapter(m_adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {

        getMenuInflater().inflate(R.menu.main_content_menu, menu);

        Utils.bindListener(menu, R.id.CLEAR, this::onClear);
        Utils.bindListener(menu, R.id.GEN_TEST, this::onGenTests);
        Utils.bindListener(menu, R.id.SETTINGS, this::onDisplaySettings);

        m_addMenuItem = menu.add(0, R.id.ADD, 0, getString(R.string.menu_add));
        m_addMenuItem.setIcon(R.drawable.ic_add);
        m_addMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        m_addMenuItem.setOnMenuItemClickListener(this::onAdd);

        m_delMenuItem = menu.add(0, R.id.DELETE, 0, getString(R.string.menu_delete));
        m_delMenuItem.setIcon(R.drawable.ic_delete);
        m_delMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        m_delMenuItem.setVisible(false);

        m_adapter.setNormalStateMenu(m_addMenuItem);
        m_adapter.setEditStateMenu(m_delMenuItem);

        return super.onCreateOptionsMenu(menu);
    }


    private boolean onDisplaySettings( MenuItem menuItem )
    {

        return true;
    }


    private boolean onGenTests( MenuItem menuItem )
    {

        List<SetElement> elms = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i < Codes.GEN_MAX; ++i)
        {
            SetElement elm = new SetElement();
            elm.setName("Set: " + (i + 1));
            int c = Color.argb(255, r.nextInt(255), r.nextInt(255), r.nextInt(255));
            elm.setColor(c);
            elms.add(elm);
        }

        m_setViewModel.add(elms);
        return true;
    }


    private boolean onAdd( MenuItem menuItem )
    {
        startActivityForResult(new Intent(this, ActivityEditSet.class), ACT_ADD);
        return true;
    }


    private boolean onClear( MenuItem menuItem )
    {

        SetRepository.get().clear();
        CollectionRepository.get().clear();
        ColorRepository.get().clear();
        SeriesRepository.get().clear();
        return true;
    }


    @Override
    public void onNormalClick( AdapterObjectInterface item )
    {

        m_setViewModel.getSingleObserver().setValue((SetElement) item);
        startActivity(new Intent(this, ActivitySeries.class));
    }


    @Override
    public void onEditClick( List<AdapterObjectInterface> elements )
    {

        m_setViewModel.drop(elements);
    }


    @Override
    public void onUpdateItem( View view, AdapterObjectInterface item )
    {

    }
}
