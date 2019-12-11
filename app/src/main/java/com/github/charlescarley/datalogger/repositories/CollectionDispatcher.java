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
import com.github.charlescarley.datalogger.room.CollectionElement;
import com.github.charlescarley.datalogger.room.CollectionInterface;

import java.util.List;


class CollectionDispatcher extends RepositoryThread<CollectionPacket>
{

    private final MutableLiveData<List<CollectionElement>> m_live;
    private final MutableLiveData<List<CollectionElement>> m_merged;


    CollectionDispatcher( MutableLiveData<List<CollectionElement>> live,
                          MutableLiveData<List<CollectionElement>> merged )
    {

        super();
        m_debug  = "<CollectionDispatcher>";
        m_live   = live;
        m_merged = merged;
    }


    @Override
    protected void handlePacket( @NonNull Packet basePack )
    {

        if (!(basePack instanceof CollectionPacket))
        {
            Utils.LogF("Invalid packet type");
            return;
        }

        CollectionPacket packet = (CollectionPacket) basePack;
        CollectionInterface ci = ApplicationDatabase.getSingleton()._collections();

        switch (packet.getCmd())
        {
        case DROP_ALL:
            ci.clear();
            break;
        case CLEAR:
            ci.clear(packet.getParent());
            break;
        case PUSH:
            handlePush(packet, ci);
            break;
        case DROP:
            handleDrop(packet, ci);
            break;
        case SYNC:
        case UPDATE1:
        case UPDATE:
            break;
        }

        if (packet.getCmd() == DispatchCode.MERGE)
            m_merged.postValue(ci.mergedEntries(packet.getParent()));
        else
            m_live.postValue(ci.entries(packet.getParent()));
    }


    private void handlePush( CollectionPacket packet, CollectionInterface collections )
    {

        if (packet.hasData())
        {
            List<CollectionElement> elements = packet.getData();
            if (elements != null)
                collections.insert(elements);
            else
                collections.insert(packet.getElement());
        }
        else
        {
            Utils.LogF("Invalid object in packet for cmd(%s)", packet.getCmd().toString());
        }
    }


    private void handleDrop( CollectionPacket packet, CollectionInterface collections )
    {

        if (packet.hasData())
        {
            List<CollectionElement> elements = packet.getData();
            if (elements != null)
                collections.drop(elements);
            else
                collections.drop(packet.getElement());
        }
        else
        {
            Utils.LogF("Invalid object in packet for cmd(%s)", packet.getCmd().toString());
        }


    }


    void addCommand( DispatchCode cmd )
    {

        CollectionPacket pack = (CollectionPacket) allocate(CollectionPacket.class);
        pack.setCmd(cmd);
        enqueue(pack);
    }


    void addCommand( DispatchCode cmd, int parent )
    {

        CollectionPacket pack = (CollectionPacket) allocate(CollectionPacket.class);
        pack.setCmd(cmd);
        pack.setParent(parent);
        enqueue(pack);
    }


    void addCommand( DispatchCode cmd, int parent, List<CollectionElement> obj )
    {

        CollectionPacket pack = (CollectionPacket) allocate(CollectionPacket.class);
        pack.setCmd(cmd);
        pack.setParent(parent);
        pack.setData(obj);

        enqueue(pack);
    }


    void addCommand( DispatchCode cmd, int parent, CollectionElement obj )
    {

        CollectionPacket pack = (CollectionPacket) allocate(CollectionPacket.class);
        pack.setCmd(cmd);
        pack.setParent(parent);
        pack.setData(obj);

        enqueue(pack);
    }
}
