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


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.github.charlescarley.datalogger.Utils;
import com.github.charlescarley.datalogger.room.ApplicationDatabase;
import com.github.charlescarley.datalogger.room.SetElement;
import com.github.charlescarley.datalogger.room.SetInterface;

import java.util.List;


class SetDispatcher extends RepositoryThread<SetPacket>
{
    private final MutableLiveData<List<SetElement>> m_live;
    private final MutableLiveData<SetElement>       m_elm;


    SetDispatcher( MutableLiveData<List<SetElement>> live, MutableLiveData<SetElement> elm )
    {

        super();

        m_debug = "<SetDispatcher>";
        m_elm   = elm;
        m_live  = live;
    }


    @Override
    protected void handlePacket( @NonNull Packet basePack )
    {

        if (!(basePack instanceof SetPacket))
        {
            Utils.LogF("Invalid packet type");
            return;
        }

        SetPacket packet = (SetPacket) basePack;

        SetInterface si = ApplicationDatabase.getSingleton()._sets();
        switch (packet.getCmd())
        {
        case CLEAR:
            si.clear();
            break;
        case SYNC:
            break;
        case DROP:
            handleDrop(packet, si);
            break;
        case UPDATE1:
            handleUpdate1(packet, si);
            break;
        case UPDATE:
            handleUpdate(packet, si);
            break;
        case PUSH:
            handleInsert(packet, si);
            break;
        }
        m_live.postValue(si.entries());
    }


    private void handleDrop( SetPacket pack, SetInterface sets )
    {

        if (pack.getData() != null)
        {
            List<SetElement> elements = pack.getData();
            sets.drop(elements);
        }
        else
        {
            Utils.LogF("Invalid object in packet for cmd(%d)", pack.getCmd());
        }

    }


    private void handleInsert( SetPacket pack, SetInterface sets )
    {

        if (pack.getData() != null)
        {
            List<SetElement> elements = pack.getData();
            sets.insert(elements);
        }
        else if (pack.getElement() != null)
        {
            SetElement elements = pack.getElement();
            sets.insert(elements);
        }
        else
        {
            Utils.LogF("Invalid object in packet for cmd(%d)", pack.getCmd());
        }

    }


    private void handleUpdate( SetPacket pack, SetInterface sets )
    {

        if (pack.getElement() != null)
        {
            SetElement element = pack.getElement();
            sets.update(element);

            m_elm.postValue(element);
        }
        else
            Utils.LogF("Invalid object in packet for cmd(%d)", pack.getCmd());
    }


    private void handleUpdate1( SetPacket pack, SetInterface sets )
    {
        // Specific update
        // expected:
        //
        // int: id to update
        // int: chart to update

        if (pack != null)
        {
            int id = pack.getUpdateId();
            int ct = pack.getChart();


            if (id > RepositoryCodes.NO_ID && ct >= 0)
            {
                sets.update(id, ct);
                m_elm.postValue(sets.entry(id));
            }
            else
                Utils.LogF("Invalid arguments in packet for cmd(%d) (id: %d,ct:%d)", id, ct);
        }
    }


    void addCommand( DispatchCode cmd, List<SetElement> obj )
    {

        SetPacket pack = (SetPacket) allocate(SetPacket.class);
        pack.setCmd(cmd);
        pack.setData(obj);
        enqueue(pack);
    }


    void addCommand( DispatchCode cmd, SetElement obj )
    {

        SetPacket pack = (SetPacket) allocate(SetPacket.class);
        pack.setCmd(cmd);
        pack.setData(null);
        pack.setElement(obj);
        enqueue(pack);
    }


    void addCommand( DispatchCode cmd, int id, int chart )
    {

        SetPacket pack = (SetPacket) allocate(SetPacket.class);
        pack.setCmd(cmd);
        pack.setUpdateId(id);
        pack.setChart(chart);
        enqueue(pack);
    }
}
