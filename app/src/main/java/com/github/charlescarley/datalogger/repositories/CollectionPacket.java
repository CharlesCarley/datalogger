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
package com.github.charlescarley.datalogger.repositories;


import com.github.charlescarley.datalogger.room.CollectionElement;

import java.util.List;


class CollectionPacket extends Packet
{
    private List<CollectionElement> m_data;
    private CollectionElement       m_element;
    private int                     m_parent;


    CollectionPacket()
    {

        super();
        donePacket();
    }


    protected void donePacket()
    {

        m_cmd     = DispatchCode.DEFAULT;
        m_data    = null;
        m_element = null;
        m_parent  = RepositoryCodes.NO_ID;
    }


    List<CollectionElement> getData()
    {

        return m_data;
    }


    void setData( List<CollectionElement> data )
    {

        this.m_data = data;
    }


    void setData( CollectionElement data )
    {

        this.m_element = data;
    }


    CollectionElement getElement()
    {

        return m_element;
    }


    public int getParent()
    {

        return m_parent;
    }


    public void setParent( int parent )
    {

        this.m_parent = parent;
    }


    public boolean hasData()
    {

        return m_data != null || m_element != null;
    }
}
