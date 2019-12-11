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
import com.github.charlescarley.datalogger.room.CollectionInterface;
import com.github.charlescarley.datalogger.room.SeriesElement;
import com.github.charlescarley.datalogger.room.SeriesInterface;

import java.util.List;


class SeriesDispatcher extends RepositoryThread<SeriesPacket>
{
    private final MutableLiveData<List<SeriesElement>> m_live;


    SeriesDispatcher( MutableLiveData<List<SeriesElement>> live, MutableLiveData<SeriesElement> elm )
    {

        super();
        m_debug = "<SeriesDispatcher>";
        m_live  = live;
    }


    @Override
    protected void handlePacket( @NonNull Packet basePack )
    {

        if (!(basePack instanceof SeriesPacket))
        {
            Utils.LogF("Invalid packet type");
            return;
        }

        SeriesPacket packet = (SeriesPacket) basePack;

        SeriesInterface si = ApplicationDatabase.getSingleton()._series();
        CollectionInterface ci = ApplicationDatabase.getSingleton()._collections();

        switch (packet.getCmd())
        {
        case CLEAR:
            si.clear(packet.getParent());
            ci.clearFromSet(packet.getParent());
            break;
        case PUSH:
            handlePush(packet, si);
            break;
        case DROP:
            handleDrop(packet, si, ci);
            break;
        case DROP_ALL:
            si.clear();
            break;
        case UPDATE:
            handleUpdate(packet, si, ci);
            break;
        case SYNC:
        case UPDATE1:
            break;
        }

        m_live.postValue(si.entries(packet.getParent()));
    }


    private void handleDrop( SeriesPacket packet,
                             SeriesInterface series,
                             CollectionInterface collections )
    {

        if (packet.hasData())
        {
            List<SeriesElement> batch = packet.getData();
            if (batch != null)
            {
                // drop all collections that belong to it
                for (SeriesElement el : batch)
                {
                    Utils.LogF("=> Dropped batch %d", el.getId());
                    collections.clear(el.getId());
                }
                series.drop(batch);
            }
            else
            {
                SeriesElement ele = packet.getElement();
                collections.clear(ele.getId());
                series.drop(ele);
            }

        }

    }


    private void handleUpdate( SeriesPacket packet,
                               SeriesInterface series,
                               CollectionInterface collections )
    {

        if (packet.hasData())
        {
            List<SeriesElement> batch = packet.getData();
            if (batch != null)
            {
                for (SeriesElement el : batch)
                    collections.updateColor(el.getId(), el.getColor());
                series.update(batch);
            }
            else
            {
                SeriesElement el = packet.getElement();

                collections.updateColor(el.getId(), el.getColor());
                series.update(el);
            }
        }
    }


    private void handlePush( SeriesPacket packet, SeriesInterface series )
    {

        if (packet.hasData())
        {
            List<SeriesElement> batch = packet.getData();
            if (batch != null)
                series.insert(batch);
            else
            {
                SeriesElement ele = packet.getElement();
                series.insert(ele);
            }
        }
    }


    void addCommand( DispatchCode cmd )
    {

        SeriesPacket pack = (SeriesPacket) allocate(SeriesPacket.class);
        pack.setCmd(cmd);
        enqueue(pack);
    }


    void addCommand( DispatchCode cmd, int parent )
    {

        SeriesPacket pack = (SeriesPacket) allocate(SeriesPacket.class);
        pack.setCmd(cmd);
        pack.setParent(parent);
        enqueue(pack);
    }


    void addCommand( DispatchCode cmd, int parent, List<SeriesElement> obj )
    {

        SeriesPacket pack = (SeriesPacket) allocate(SeriesPacket.class);
        pack.setCmd(cmd);
        pack.setParent(parent);
        pack.setData(obj);

        enqueue(pack);
    }


    void addCommand( DispatchCode cmd, int parent, SeriesElement obj )
    {

        SeriesPacket pack = (SeriesPacket) allocate(SeriesPacket.class);
        pack.setCmd(cmd);
        pack.setParent(parent);
        pack.setData(obj);

        enqueue(pack);
    }
}
