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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.github.charlescarley.datalogger.Utils;
import com.github.charlescarley.datalogger.repositories.RepositoryCodes;
import com.github.charlescarley.datalogger.repositories.SeriesRepository;
import com.github.charlescarley.datalogger.room.AdapterObjectInterface;
import com.github.charlescarley.datalogger.room.SeriesElement;

import java.util.List;


public class SeriesViewModel extends AndroidViewModel
{
    private final SeriesRepository m_repo;
    private       int              m_parent;


    public SeriesViewModel( @NonNull Application application )
    {

        super(application);
        m_parent = RepositoryCodes.NO_ID;
        m_repo   = SeriesRepository.get();
    }


    public void access( int fromParent )
    {
        m_parent = fromParent;
        m_repo.access(fromParent);
    }


    public LiveData<List<SeriesElement>> getObservable()
    {

        return m_repo.getLiveData();
    }

    public void setSingleObserver( LifecycleOwner parent,
                                   SeriesElement item,
                                   Observer<SeriesElement> observer )
    {

        MutableLiveData<SeriesElement> liveData = SeriesRepository.get().getElementObserver();
        liveData.observe(parent, observer);

        // callback immediately, to give observers
        // a chance to update
        liveData.setValue(item);
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


    public void add( List<SeriesElement> elements )
    {
        if (m_parent != RepositoryCodes.NO_ID)
        {
            m_repo.add(m_parent, elements);
        }
        else
            Utils.Log("Add called without an attached parent");
    }



    public void clear()
    {
        if (m_parent != RepositoryCodes.NO_ID)
        {
            m_repo.clear(m_parent);
        }
        else
            Utils.Log("Add called without an attached parent");
    }

}
