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
import com.github.charlescarley.datalogger.room.SetElement;

import java.util.List;
import java.util.stream.Collectors;


public class SetRepository
{
    private static final SetRepository                     m_repo = new SetRepository();
    private final        MutableLiveData<List<SetElement>> m_live;
    private final        MutableLiveData<SetElement>       m_elm;
    private final        SetDispatcher                     m_dispatcher;


    private SetRepository()
    {

        m_live       = new MutableLiveData<>();
        m_elm        = new MutableLiveData<>();
        m_dispatcher = new SetDispatcher(m_live, m_elm);
    }


    public static SetRepository get()
    {
        // Singleton access to the repo
        return m_repo;
    }


    public MutableLiveData<SetElement> getElementObserver()
    {

        return m_elm;
    }


    public MutableLiveData<List<SetElement>> getLiveData()
    {

        synchronized (m_live)
        {
            return m_live;
        }
    }


    public void access()
    {

        m_dispatcher.addCommand(DispatchCode.SYNC, (List<SetElement>) null);
        m_dispatcher.dispatch();
    }


    public void clear()
    {

        m_dispatcher.addCommand(DispatchCode.CLEAR, (List<SetElement>) null);
        m_dispatcher.dispatch();
    }


    public void add( SetElement ent )
    {

        m_dispatcher.addCommand(DispatchCode.PUSH, ent);
        m_dispatcher.dispatch();
    }


    public void add( List<SetElement> ent )
    {

        m_dispatcher.addCommand(DispatchCode.PUSH, ent);
        m_dispatcher.dispatch();
    }


    public void drop( List<SetElement> ent )
    {

        m_dispatcher.addCommand(DispatchCode.DROP, ent);
        m_dispatcher.dispatch();
    }


    public void dropAOI( List<AdapterObjectInterface> ent )
    {

        drop(ent.parallelStream().
            map(obj -> (SetElement) obj).
            collect(Collectors.toList()));
    }


    public void update( SetElement element )
    {

        m_dispatcher.addCommand(DispatchCode.UPDATE, element);
        m_dispatcher.dispatch();
    }


    public void updateChartType( int id, int chartType )
    {

        m_dispatcher.addCommand(DispatchCode.UPDATE1, id, chartType);
        m_dispatcher.dispatch();
    }
}
