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


import com.github.charlescarley.datalogger.room.SetElement;

import java.util.List;


class SetPacket extends Packet
{
    private List<SetElement> m_data;
    private SetElement       m_element;
    private int              m_updateId, m_chart;


    SetPacket()
    {

        super();
        donePacket();
    }


    protected void donePacket()
    {
        // (Re)initialize packet to the default state
        m_cmd      = DispatchCode.DEFAULT;
        m_data     = null;
        m_element  = null;
        m_updateId = -1;
        m_chart    = -1;
    }


    List<SetElement> getData()
    {

        return m_data;
    }


    void setData( List<SetElement> data )
    {

        this.m_data = data;
    }


    SetElement getElement()
    {

        return m_element;
    }


    void setElement( SetElement data )
    {

        this.m_element = data;
    }


    int getUpdateId()
    {

        return m_updateId;
    }


    void setUpdateId( int updateId )
    {

        this.m_updateId = updateId;
    }


    int getChart()
    {

        return m_chart;
    }


    void setChart( int chart )
    {

        this.m_chart = chart;
    }
}
