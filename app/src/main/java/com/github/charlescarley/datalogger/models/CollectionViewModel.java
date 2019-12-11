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
package com.github.charlescarley.datalogger.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.charlescarley.datalogger.Utils;
import com.github.charlescarley.datalogger.repositories.CollectionRepository;
import com.github.charlescarley.datalogger.repositories.RepositoryCodes;
import com.github.charlescarley.datalogger.room.AdapterObjectInterface;
import com.github.charlescarley.datalogger.room.CollectionElement;

import java.util.List;


public class CollectionViewModel extends AndroidViewModel
{
    private final CollectionRepository m_repo;
    private       int                  m_parent;


    public CollectionViewModel( @NonNull Application application )
    {

        super(application);
        m_repo   = CollectionRepository.get();
        m_parent = RepositoryCodes.NO_ID;
    }


    public LiveData<List<CollectionElement>> getObservable()
    {

        return m_repo.getLiveData();
    }

    public LiveData<List<CollectionElement>> getMergedObservable()
    {

        return m_repo.getMergedData();
    }

    public MutableLiveData<CollectionElement> getSingleObservable()
    {
        return m_repo.getSingleObservable();
    }

    public void access( int id )
    {

        m_parent = id;
        m_repo.access(m_parent);
    }


    public void add( List<CollectionElement> elements )
    {

        if (m_parent != RepositoryCodes.NO_ID)
        {
            m_repo.push(m_parent, elements);
        }
        else
            Utils.Log("Add called without an attached parent");
    }


    public void drop( List<AdapterObjectInterface> elements )
    {

        if (m_parent != RepositoryCodes.NO_ID)
        {
            m_repo.dropAOI(m_parent, elements);
        }
        else
            Utils.Log("Drop called without an attached parent");
    }


    public void clear( )
    {

        if (m_parent != RepositoryCodes.NO_ID)
            m_repo.clear(m_parent);
        else
            Utils.Log("clear called without an attached parent");
    }


    public void notifySetChange(int id)
    {
        m_repo.merge(id);
    }


    public void push( CollectionElement element )
    {
        m_repo.push(element);
    }
}
