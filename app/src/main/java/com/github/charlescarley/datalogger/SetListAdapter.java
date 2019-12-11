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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.charlescarley.datalogger.room.AdapterObjectInterface;

import java.util.ArrayList;
import java.util.List;

import static com.github.charlescarley.datalogger.Utils.LogError;
import static com.github.charlescarley.datalogger.Utils.LogErrorF;


class SetListAdapter<T> extends BaseAdapter implements View.OnClickListener, View.OnLongClickListener
{
    private final Context              m_ctx;
    private final SetListClickListener m_listener;
    private final int                  m_normal, m_selected, m_accent, m_layout;
    private MenuItem m_addMenuItem;
    private MenuItem m_delMenuItem;
    private boolean  m_editState;


    private SparseArray<AdapterObjectInterface>    m_elementMap = null;
    private List<? extends AdapterObjectInterface> m_elements;


    SetListAdapter( Context ctx, SetListClickListener listener, int layout )
    {

        m_layout      = layout;
        m_normal      = ctx.getColor(R.color.window1);
        m_selected    = ctx.getColor(R.color.window2);
        m_accent      = ctx.getColor(R.color.colorAccent);
        m_editState   = false;
        m_addMenuItem = null;
        m_delMenuItem = null;

        m_ctx      = ctx;
        m_elements = null;
        m_listener = listener;
    }


    public void setNormalStateMenu( MenuItem item )
    {
        // menu item that is visible in the
        // default edit state.
        m_addMenuItem = item;
    }


    public void setEditStateMenu( MenuItem item )
    {
        // menu item that is visible in the
        // default edit state.
        m_delMenuItem = item;
        m_delMenuItem.setOnMenuItemClickListener(this::dropEditObjects);

    }


    private boolean dropEditObjects( MenuItem menuItem )
    {

        if (m_elementMap != null)
        {
            List<AdapterObjectInterface> elms = new ArrayList<>();
            int i, s = m_elementMap.size();


            for (i = 0; i < s; ++i)
            {
                AdapterObjectInterface val = m_elementMap.get(m_elementMap.keyAt(i));
                if (val != null)
                    elms.add(val);
            }

            m_listener.onEditClick(elms);
            exitEditState();
        }
        return true;
    }


    @Override
    public int getCount()
    {

        if (m_elements == null)
            return 0;
        return m_elements.size();
    }


    @Override
    public Object getItem( int position )
    {

        if (m_elements == null || position > getCount())
            return null;
        return m_elements.get(position);
    }


    @Override
    public long getItemId( int position )
    {

        return 0;
    }


    @Override
    public View getView( int position, View convert, ViewGroup parent )
    {

        View newView;
        AdapterObjectInterface item = (AdapterObjectInterface) getItem(position);
        if (item == null)
        {
            LogErrorF("getItem(%d) returned null", position);
            return Utils.errorView(m_ctx);
        }

        if (convert == null)
        {
            LayoutInflater inflater = Utils.getInflater(m_ctx);
            if (inflater == null)
                return Utils.errorView(m_ctx);

            newView = inflater.inflate(m_layout, parent, false);
            newView.setOnClickListener(this);
            newView.setOnLongClickListener(this);
        }
        else
            newView = convert;

        ImageView ima = newView.findViewById(R.id.set_list_image);
        if (ima != null)
        {
            ima.setOnClickListener(( view ) -> {
                onEnterSubStateView(newView, (AdapterObjectInterface) view.getTag());
            });
            ima.setTag(item);
            ima.setColorFilter(item.getColor());
        }

        TextView tv = newView.findViewById(R.id.set_list_title);
        if (tv != null)
            tv.setText(item.getName());


        newView.setTag(item);
        m_listener.onUpdateItem(newView, item);
        applySubStateView(newView, item);
        return newView;
    }


    private void enterEditState( AdapterObjectInterface item )
    {

        if (!m_editState)
        {
            m_editState = true;
            if (m_elementMap != null)
                m_elementMap.clear();

            if (m_addMenuItem != null)
                m_addMenuItem.setVisible(false);

            if (m_delMenuItem != null)
                m_delMenuItem.setVisible(true);

            if (m_elementMap == null)
                m_elementMap = new SparseArray<>();
        }
    }


    private void exitEditState()
    {

        if (m_editState)
        {
            m_editState = false;

            if (m_addMenuItem != null)
                m_addMenuItem.setVisible(true);

            if (m_delMenuItem != null)
                m_delMenuItem.setVisible(false);
        }
    }


    private void onEnterSubStateView( @NonNull View v,
                                      @NonNull AdapterObjectInterface item )
    {

        enterEditState(item);

        if (m_elementMap.get(item.getId()) == null)
        {
            item.setState(1);
            m_elementMap.put(item.getId(), item);
        }
        else
        {
            item.setState(0);
            m_elementMap.remove(item.getId());
        }

        if (m_elementMap.size() == 0)
            exitEditState();

        applySubStateView(v, item);
    }


    private void applySubStateView( @NonNull View view,
                                    @NonNull AdapterObjectInterface el )
    {
        // Provide some visual indication about the current state
        // of the element. The view needs to be the root layout
        // in order to find sub views.

        ImageView ima = view.findViewById(R.id.set_list_image);
        if (el.getState() == 1)
        {
            view.setBackgroundColor(m_selected);
            ima.setColorFilter(m_accent);
        }
        else
        {
            view.setBackgroundColor(m_normal);
            ima.setColorFilter(el.getColor());
        }
    }


    @Override
    public void onClick( View v )
    {

        Object tag = v.getTag();
        if (tag instanceof AdapterObjectInterface)
            m_listener.onNormalClick((AdapterObjectInterface) tag);
        else
            LogError("Tag type mismatch (SetElement)");
    }


    @Override
    public boolean onLongClick( View v )
    {

        Object tag = v.getTag();
        if (tag instanceof AdapterObjectInterface)
        {
            onEnterSubStateView(v, (AdapterObjectInterface) tag);
            return true;
        }
        else
            LogError("Tag type mismatch (AdapterObjectInterface)");

        return false;
    }


    void update( List<? extends AdapterObjectInterface> elements )
    {

        m_elements = elements;
    }
}

