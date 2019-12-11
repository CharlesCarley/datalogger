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
import com.github.charlescarley.datalogger.room.CollectionElement;

import java.util.List;
import java.util.stream.Collectors;


public class CollectionRepository
{
    private static final CollectionRepository m_repo = new CollectionRepository();

    private final MutableLiveData<List<CollectionElement>> m_live;
    private final MutableLiveData<List<CollectionElement>> m_merged;
    private final MutableLiveData<CollectionElement>       m_element;

    private final CollectionDispatcher m_dispatcher;


    private CollectionRepository()
    {

        m_live       = new MutableLiveData<>();
        m_merged     = new MutableLiveData<>();
        m_element    = new MutableLiveData<>();
        m_dispatcher = new CollectionDispatcher(m_live, m_merged);
    }


    public static CollectionRepository get()
    {

        return m_repo;
    }


    public MutableLiveData<List<CollectionElement>> getLiveData()
    {

        return m_live;
    }


    public MutableLiveData<List<CollectionElement>> getMergedData()
    {

        return m_merged;
    }


    public MutableLiveData<CollectionElement> getSingleObservable()
    {

        return m_element;
    }


    public void access( int parent )
    {

        m_dispatcher.addCommand(DispatchCode.SYNC, parent);
        m_dispatcher.dispatch();
    }


    public void merge( int parent )
    {

        m_dispatcher.addCommand(DispatchCode.MERGE, parent);
        m_dispatcher.dispatch();
    }


    public void clear()
    {

        m_dispatcher.addCommand(DispatchCode.CLEAR);
        m_dispatcher.dispatch();
    }


    public void clear( int parent )
    {

        m_dispatcher.addCommand(DispatchCode.CLEAR, parent);
        m_dispatcher.dispatch();
    }


    public void push( CollectionElement ent )
    {

        m_dispatcher.addCommand(DispatchCode.PUSH, ent.getParent(), ent);
        m_dispatcher.dispatch();
    }


    public void push( int parent, List<CollectionElement> ent )
    {

        m_dispatcher.addCommand(DispatchCode.PUSH, parent, ent);
        m_dispatcher.dispatch();
    }


    public void drop( int parent, List<CollectionElement> ent )
    {

        m_dispatcher.addCommand(DispatchCode.DROP, parent, ent);
        m_dispatcher.dispatch();
    }


    public void dropAOI( int parent, List<AdapterObjectInterface> ent )
    {

        drop(parent, ent.parallelStream().
            map(obj -> (CollectionElement) obj).
            collect(Collectors.toList()));
    }
}
