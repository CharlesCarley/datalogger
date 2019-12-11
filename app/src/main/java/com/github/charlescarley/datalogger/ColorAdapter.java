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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.github.charlescarley.datalogger.room.ColorElement;

import java.util.List;

import static com.github.charlescarley.datalogger.Utils.LogError;
import static com.github.charlescarley.datalogger.Utils.LogErrorF;


class ColorAdapter extends BaseAdapter implements View.OnClickListener
{

    private final Context            m_ctx;
    private final ColorClickListener m_listener;
    private       List<ColorElement> m_elements;


    ColorAdapter( Context ctx, ColorClickListener listener )
    {

        m_ctx      = ctx;
        m_elements = null;
        m_listener = listener;
    }


    void updateElements( List<ColorElement> elements )
    {

        m_elements = elements;
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
        ColorElement item = (ColorElement) getItem(position);
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

            newView = inflater.inflate(R.layout.color_elm, parent, false);
        }
        else
            newView = convert;

        ImageView ima = newView.findViewById(R.id.color1);
        ima.setColorFilter(item.getColor());
        ima.setOnClickListener(this);
        ima.setTag(item);
        return newView;
    }


    @Override
    public void onClick( View v )
    {

        Object tag = v.getTag();
        if (tag instanceof ColorElement)
            m_listener.onClick((ColorElement) tag);
        else
            LogError("Tag type mismatch (DatabaseColorItem)");
    }


    interface ColorClickListener
    {
        void onClick( ColorElement item );
    }
}

