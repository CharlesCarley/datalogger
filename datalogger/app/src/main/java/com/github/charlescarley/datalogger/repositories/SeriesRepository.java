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


import androidx.lifecycle.MutableLiveData;

import com.github.charlescarley.datalogger.room.AdapterObjectInterface;
import com.github.charlescarley.datalogger.room.SeriesElement;

import java.util.List;
import java.util.stream.Collectors;


public class SeriesRepository
{
    private static final SeriesRepository                     m_repo = new SeriesRepository();
    private final        MutableLiveData<List<SeriesElement>> m_live;
    private final        MutableLiveData<SeriesElement>       m_element;
    private final        SeriesDispatcher                     m_dispatcher;


    private SeriesRepository()
    {

        m_live       = new MutableLiveData<>();
        m_element    = new MutableLiveData<>();
        m_dispatcher = new SeriesDispatcher(m_live, m_element);
    }


    public static SeriesRepository get()
    {

        return m_repo;
    }


    public MutableLiveData<List<SeriesElement>> getLiveData()
    {

        return m_live;
    }


    public MutableLiveData<SeriesElement> getElementObserver()
    {

        return m_element;
    }


    public void access( int id )
    {

        m_dispatcher.addCommand(DispatchCode.SYNC, id);
        m_dispatcher.dispatch();
    }


    public void clear( int id )
    {

        m_dispatcher.addCommand(DispatchCode.CLEAR, id);
        m_dispatcher.dispatch();
    }


    public void clear()
    {

        m_dispatcher.addCommand(DispatchCode.DROP_ALL);
        m_dispatcher.dispatch();
    }


    public void push( SeriesElement element )
    {

        m_dispatcher.addCommand(DispatchCode.PUSH, element.getParent(), element);
        m_dispatcher.dispatch();
    }


    public void drop( int parent, List<SeriesElement> elements )
    {

        m_dispatcher.addCommand(DispatchCode.DROP, parent, elements);
        m_dispatcher.dispatch();
    }


    public void dropAOI( int parent, List<AdapterObjectInterface> ent )
    {

        drop(parent, ent.parallelStream().
            map(obj -> (SeriesElement) obj).
            collect(Collectors.toList()));
    }


    public void add( int parent, List<SeriesElement> elements )
    {

        m_dispatcher.addCommand(DispatchCode.PUSH, parent, elements);
        m_dispatcher.dispatch();
    }


    public void update( SeriesElement element )
    {

        m_dispatcher.addCommand(DispatchCode.UPDATE, element.getParent(), element);
        m_dispatcher.dispatch();
    }
}
