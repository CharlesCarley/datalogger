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
import com.github.charlescarley.datalogger.room.ColorElement;
import com.github.charlescarley.datalogger.room.ColorInterface;

import java.util.List;


class ColorDispatcher extends RepositoryThread<ColorPacket>
{

    private final MutableLiveData<List<ColorElement>> m_live;


    ColorDispatcher( MutableLiveData<List<ColorElement>> live )
    {

        super();
        m_debug = "<ColorDispatcher>";
        m_live  = live;
    }


    @Override
    protected void handlePacket( @NonNull Packet basePack )
    {

        if (!(basePack instanceof ColorPacket))
        {
            Utils.LogF("Invalid packet type");
            return;
        }

        ColorPacket packet = (ColorPacket) basePack;

        ColorInterface ci = ApplicationDatabase.getSingleton()._colors();

        switch (packet.getCmd())
        {
        case DROP_ALL:
        case CLEAR:
            ci.clear();
            break;
        case PUSH:
            handlePush(packet, ci);
            break;
        case DROP:
        case SYNC:
        case UPDATE1:
        case UPDATE:
            break;
        }

        m_live.postValue(ci.entries());
    }


    private void handlePush( ColorPacket packet, ColorInterface colors )
    {

        if (packet.hasData())
        {
            List<ColorElement> elements = packet.getData();
            if (elements != null)
                colors.insert(elements);
            else
                colors.insert(packet.getElement());
        }
        else
        {
            Utils.LogF("Invalid object in packet for cmd(%s)", packet.getCmd().toString());
        }

    }


    void addCommand( DispatchCode cmd )
    {

        ColorPacket pack = (ColorPacket) allocate(ColorPacket.class);
        pack.setCmd(cmd);
        enqueue(pack);
    }


    void addCommand( DispatchCode cmd, List<ColorElement> obj )
    {

        ColorPacket pack = (ColorPacket) allocate(ColorPacket.class);
        pack.setCmd(cmd);
        pack.setData(obj);
        enqueue(pack);
    }


    void addCommand( DispatchCode cmd, ColorElement obj )
    {

        ColorPacket pack = (ColorPacket) allocate(SeriesPacket.class);
        pack.setCmd(cmd);
        pack.setData(obj);
        enqueue(pack);
    }
}
