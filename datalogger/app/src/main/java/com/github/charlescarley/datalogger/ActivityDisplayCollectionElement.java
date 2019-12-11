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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.github.charlescarley.datalogger.models.CollectionViewModel;
import com.github.charlescarley.datalogger.room.CollectionElement;


public class ActivityDisplayCollectionElement extends ActivityBase
{

    CollectionViewModel m_collections;
    CollectionElement   m_active;


    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState )
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_collection_element);
        setActionBarTitle(R.string.act_collection_title, true);


        m_collections = ViewModelProviders.of(this).get(CollectionViewModel.class);
        m_collections.getSingleObservable().observe(this, this::onItemChanged);
    }


    private void onItemChanged( CollectionElement collectionElement )
    {
        TextView tv;
        tv = findViewById(R.id.m_value);
        if (tv != null)
            tv.setText(collectionElement.getName());

        tv = findViewById(R.id.m_lastMod);
        if (tv != null)
            tv.setText(Utils.millisecondsToString(collectionElement.getTimestamp()));

        tv = findViewById(R.id.m_note);
        if (tv != null)
            tv.setText(collectionElement.getNote());
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {

        MenuItem edit = menu.add(0, R.id.EDIT, 0, getString(R.string.menu_edit));
        edit.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        edit.setIcon(R.drawable.ic_edit);
        edit.setOnMenuItemClickListener(this::onEdit);

        return super.onCreateOptionsMenu(menu);
    }


    private boolean onEdit( MenuItem menuItem )
    {

        Utils.spawn(this, ActivityEditCollection.class, null);
        return true;
    }
}
