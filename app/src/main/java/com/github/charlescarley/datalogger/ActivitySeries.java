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
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.github.charlescarley.datalogger.charts.AreaChart;
import com.github.charlescarley.datalogger.charts.BarChart;
import com.github.charlescarley.datalogger.charts.BaseChart;
import com.github.charlescarley.datalogger.charts.LineGraph;
import com.github.charlescarley.datalogger.charts.ScatterPlot;
import com.github.charlescarley.datalogger.models.CollectionViewModel;
import com.github.charlescarley.datalogger.models.SeriesViewModel;
import com.github.charlescarley.datalogger.models.SetViewModel;
import com.github.charlescarley.datalogger.room.AdapterObjectInterface;
import com.github.charlescarley.datalogger.room.CollectionElement;
import com.github.charlescarley.datalogger.room.SeriesElement;
import com.github.charlescarley.datalogger.room.SetElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.charlescarley.datalogger.Utils.LogErrorF;


public class ActivitySeries extends ActivityBase implements SetListClickListener
{
    private MenuItem m_addMenuItem, m_delMenuItem;
    private ListView            m_list;
    private SetListAdapter      m_adapter;
    private SetViewModel        m_setModel;
    private SeriesViewModel     m_seriesModel;
    private CollectionViewModel m_collectionModel;
    private int                 m_item, m_curChart;
    private BaseChart m_chart;
    private boolean   m_toggle;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);

        m_addMenuItem = null;
        m_delMenuItem = null;
        m_list        = null;
        m_chart       = null;
        m_toggle      = false;
        m_curChart    = -1;

        m_adapter = new SetListAdapter<>(this, this, R.layout.set_list_item);

        m_collectionModel = ViewModelProviders.of(this).get(CollectionViewModel.class);
        m_seriesModel     = ViewModelProviders.of(this).get(SeriesViewModel.class);
        m_setModel        = ViewModelProviders.of(this).get(SetViewModel.class);

        SetElement elm = m_setModel.getSingleObserver().getValue();
        if (elm != null)
            m_item = elm.getId();
        else
            Utils.LogErrorF("Missing Set element");

        m_collectionModel.getMergedObservable().observe(this, this::onCollectionsChange);
        m_seriesModel.getObservable().observe(this, this::populate);
        m_setModel.observeSingle(this, m_setModel.getSingleObserver().getValue(), this::onSetEdit);
    }


    private void onCollectionsChange( List<CollectionElement> collectionElements )
    {

        Utils.LogF("Collection Changed %d entries", collectionElements.size());
        if (m_chart !=null)
        {
            m_chart.clearPoints();
            m_chart.onSetElements(collectionElements);
        }
    }


    private void onSetEdit( SetElement setElement )
    {

        Utils.LogF("SetElement Changed %d", setElement.getId());

        setActionBarTitle(setElement.getName(), true);

        m_seriesModel.access(m_item);

        m_collectionModel.notifySetChange(m_item);
        if (m_list != null)
            chartFactoryCreate(setElement.getChartType());
    }


    private void populate( List<SeriesElement> seriesElements )
    {

        Utils.LogF("SeriesElement Changed %d", seriesElements.size());
        if (seriesElements.isEmpty())
        {
            m_list = null;
            m_curChart    = -1;
            setContentView(R.layout.no_content);

            TextView tv = findViewById(R.id.m_subtext);
            if (tv != null)
                tv.setText(R.string.no_content_sub_text_series);
        }
        else
        {
            if (m_list == null)
            {
                setContentView(R.layout.activity_series);
                m_list = findViewById(R.id.m_list);
            }

            if (m_list != null)
            {
                m_adapter.update(seriesElements);
                m_list.setAdapter(m_adapter);
                m_collectionModel.notifySetChange(m_item);

                SetElement elm = m_setModel.getSingleObserver().getValue();
                if (elm != null)
                    chartFactoryCreate(elm.getChartType());
                else
                    chartFactoryCreate(Codes.CT_SCATTER);
            }
            else
            {
                Utils.LogF("Could not find m_list in R.layout.activity_series");
            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {

        getMenuInflater().inflate(R.menu.set_context_menu, menu);

        MenuItem edit = menu.add(0, R.id.EDIT, 0, getString(R.string.menu_edit));
        edit.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        edit.setIcon(R.drawable.ic_edit);
        edit.setOnMenuItemClickListener(this::onEdit);

        m_addMenuItem = menu.add(0, R.id.ADD, 0, getString(R.string.menu_add));
        m_addMenuItem.setIcon(R.drawable.ic_add);
        m_addMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        m_addMenuItem.setOnMenuItemClickListener(this::onAddNew);

        m_delMenuItem = menu.add(0, R.id.DELETE, 0, getString(R.string.menu_delete));
        m_delMenuItem.setIcon(R.drawable.ic_delete);
        m_delMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        m_delMenuItem.setVisible(false);

        Utils.bindListener(menu, R.id.GEN_TEST, this::onGenTests);

        m_adapter.setNormalStateMenu(m_addMenuItem);
        m_adapter.setEditStateMenu(m_delMenuItem);

        return super.onCreateOptionsMenu(menu);
    }


    private boolean onEdit( MenuItem menuItem )
    {

        SetElement elm = m_setModel.getSingleObserver().getValue();
        if (elm != null)
        {
            Intent intent = new Intent(this, ActivityEditSet.class);
            intent.putExtra(Codes.ARG1, elm);
            startActivity(intent);
        }
        return true;
    }


    private boolean onGenTests( MenuItem menuItem )
    {

        List<SeriesElement> elms = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i < Codes.GEN_MAX; ++i)
        {
            SeriesElement elm = new SeriesElement();
            elm.setName("Series: " + (i + 1));

            int c = Color.argb(255, r.nextInt(255), r.nextInt(255), r.nextInt(255));
            elm.setColor(c);
            elm.setParent(m_item);
            elm.setType(0);
            elms.add(elm);
        }

        m_seriesModel.add(elms);
        return true;
    }


    private boolean onAddNew( MenuItem menuItem )
    {

        SetElement elm = m_setModel.getSingleObserver().getValue();
        if (elm != null)
        {
            Intent intent = new Intent(this, ActivityEditSeries.class);
            intent.putExtra(Codes.ARG1, elm);
            startActivity(intent);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item )
    {

        if (item.getItemId() == R.id.CLEAR)
            m_seriesModel.clear();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onNormalClick( AdapterObjectInterface item )
    {

        SeriesElement elm = (SeriesElement) item;
        if (elm != null)
        {
            Intent intent = new Intent(this, ActivityCollection.class);
            intent.putExtra(Codes.ARG1, elm);
            startActivity(intent);
        }
        else
        {
            LogErrorF("Null item in ActivitySeries::onNormalClick");
        }

    }


    public void onArrowClicked( View view )
    {

        if (m_list != null)
        {
            if (m_toggle)
                m_list.setVisibility(View.VISIBLE);
            else
                m_list.setVisibility(View.GONE);
            m_toggle = !m_toggle;
        }

    }


    @Override
    public void onEditClick( List<AdapterObjectInterface> elements )
    {

        m_seriesModel.drop(elements);
    }


    @Override
    public void onUpdateItem( View view, AdapterObjectInterface item )
    {

    }


    private void chartFactoryCreate( int id )
    {

        if (m_curChart == id)
        {
            Utils.LogF("Current chart is already %d, skipping ", id);
            return;
        }

        BaseChart baseChart = null;
        switch (id)
        {
        case Codes.CT_SCATTER:
            baseChart = new ScatterPlot(this, null);
            break;
        case Codes.CT_AREA:
            baseChart = new AreaChart(this, null);
            break;
        case Codes.CT_LINE:
            baseChart = new LineGraph(this, null);
            break;
        case Codes.CT_BAR:
            baseChart = new BarChart(this, null);
            break;
        default:
            break;
        }

        if (baseChart != null)
        {
            m_curChart = id;

            ViewGroup graphRoot = findViewById(R.id.m_graphRoot);
            if (m_chart != null)
                graphRoot.removeView(m_chart);

            m_chart = baseChart;
            graphRoot.addView(m_chart);

            m_setModel.updateChartType(m_item, id);
            m_collectionModel.notifySetChange(m_item);
        }
    }

    public void onHomeClick( View view )
    {
        if (m_chart != null)
            m_chart.resetView();
    }

    public void onXYClicked( View view )
    {
        if (m_chart != null)
            m_chart.toggleOrderedPairs();
    }


    public void onAreaIconClick( View view )
    {

        chartFactoryCreate(Codes.CT_AREA);
    }


    public void onScatterIconClick( View view )
    {

        chartFactoryCreate(Codes.CT_SCATTER);
    }


    public void onBarIconClick( View view )
    {

        chartFactoryCreate(Codes.CT_BAR);
    }


    public void onLineIconClick( View view )
    {

        chartFactoryCreate(Codes.CT_LINE);
    }
}
