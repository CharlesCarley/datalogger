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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import com.github.charlescarley.datalogger.models.CollectionViewModel;
import com.github.charlescarley.datalogger.models.SeriesViewModel;
import com.github.charlescarley.datalogger.room.AdapterObjectInterface;
import com.github.charlescarley.datalogger.room.CollectionElement;
import com.github.charlescarley.datalogger.room.SeriesElement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class ActivityCollection extends ActivityBase implements SetListClickListener
{
    private MenuItem m_addMenuItem, m_delMenuItem;
    private SetListAdapter<CollectionElement> m_adapter;
    private SeriesElement                     m_element;
    private ListView                          m_list;
    private CollectionViewModel               m_collectionModel;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);

        m_element = getIntent().getParcelableExtra(Codes.ARG1);
        if (m_element != null)
            setActionBarTitle(m_element.getName(), true);
        else
            Utils.LogError("getParcelableExtra, is missing the required argument");

        m_addMenuItem = null;
        m_delMenuItem = null;
        m_list        = null;

        m_adapter = new SetListAdapter<>(this, this, R.layout.collection_list_item);

        m_collectionModel = ViewModelProviders.of(this).get(CollectionViewModel.class);
        m_collectionModel.getObservable().observe(this, this::populate);

        SeriesViewModel seriesModel = ViewModelProviders.of(this).get(SeriesViewModel.class);
        seriesModel.setSingleObserver(this, m_element, this::onSeriesChanged);
    }


    private void onSeriesChanged( SeriesElement element )
    {

        setActionBarTitle(element.getName(), true);
        m_element = element;

        m_collectionModel.access(m_element.getId());
        m_collectionModel.notifySetChange(m_element.getParent());
    }


    private void populate( List<CollectionElement> elements )
    {

        if (elements.isEmpty())
        {
            m_list = null;
            setContentView(R.layout.no_content);

            TextView tv = findViewById(R.id.m_subtext);
            if (tv != null)
                tv.setText(R.string.no_content_sub_text_collection);
        }
        else
        {
            if (m_list == null)
            {

                setContentView(R.layout.activity_list_view);
                m_list = findViewById(R.id.m_list);
            }

            if (m_list != null)
            {
                m_adapter.update(elements);
                m_list.setAdapter(m_adapter);
                m_collectionModel.notifySetChange(m_element.getParent());
            }
            else
            {
                Utils.LogF("Failed to find m_list in R.layout.activity_list_view");
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {

        MenuItem edit = menu.add(0, R.id.EDIT, 0, getString(R.string.menu_edit));
        edit.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        edit.setIcon(R.drawable.ic_edit);
        edit.setOnMenuItemClickListener(this::onEdit);


        MenuItem clear = menu.add(0, R.id.CLEAR, 0, getString(R.string.menu_clear));
        clear.setOnMenuItemClickListener(this::onClear);

        MenuItem TEMP_GEN = menu.add(0, R.id.GEN_TEST, 0, getString(R.string.menu_random_collection));
        TEMP_GEN.setOnMenuItemClickListener(this::onRandCollection);

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


    private boolean onClear( MenuItem menuItem )
    {

        m_collectionModel.clear();
        return true;
    }


    private boolean onEdit( MenuItem menuItem )
    {

        Intent intent = new Intent(this, ActivityEditSeries.class);
        intent.putExtra(Codes.ARG1, m_element);
        startActivityForResult(intent, Codes.CODE_OK);
        return true;
    }


    private boolean onRandCollection( MenuItem menuItem )
    {

        List<CollectionElement> elms = new ArrayList<>();
        Random r = new Random();

        final long Seed1 = 30;
        final long Seed2 = 100;
        final double iDay = 86400552;

        double pivot = 0;

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        pivot = c.getTimeInMillis();

        double step = (360 / (float) Codes.GEN_MAX) * (Math.PI / 180.f);
        double accum = 0;
        double offs = Seed2 * (2 * r.nextDouble() - 1);

        for (int i = 0; i < Codes.GEN_MAX; ++i, accum += step)
        {
            CollectionElement elm = new CollectionElement(m_element);

            double d = ((r.nextDouble() * Seed2) * Math.sin(accum)) + (offs * r.nextDouble());
            if (m_element.getType() == 0)
                elm.setValue(Integer.toString((int) d));
            else
                elm.setValue(Double.toString(d));

            pivot += iDay;
            c.setTimeInMillis((long) (pivot));
            elm.setTimestamp(c.getTimeInMillis());
            elm.setNote(Utils.format("Auto generated index %d of %d", i, Codes.GEN_MAX));
            elms.add(elm);
        }

        m_collectionModel.add(elms);
        return true;
    }


    private boolean onAdd( MenuItem menuItem )
    {

        m_collectionModel.getSingleObservable().setValue(null);
        Utils.spawn(this, ActivityEditCollection.class, m_element);
        return false;
    }


    @Override
    public void onNormalClick( AdapterObjectInterface item )
    {

        m_collectionModel.getSingleObservable().setValue((CollectionElement) item);
        Utils.spawn(this, ActivityDisplayCollectionElement.class, null);
    }


    @Override
    public void onEditClick( List<AdapterObjectInterface> elements )
    {

        m_collectionModel.drop(elements);
    }


    @Override
    public void onUpdateItem( View view, AdapterObjectInterface item )
    {

        TextView date = view.findViewById(R.id.m_dateField);
        if (date != null)
        {
            CollectionElement element = (CollectionElement) item;
            date.setText(Utils.millisecondsToString(element.getTimestamp()));
        }


    }
}
