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

import com.github.charlescarley.datalogger.repositories.SetRepository;
import com.github.charlescarley.datalogger.room.AdapterObjectInterface;
import com.github.charlescarley.datalogger.room.SetElement;

import java.util.List;

import static com.github.charlescarley.datalogger.Codes.CT_MAX;


public class SetViewModel extends AndroidViewModel
{
    private final MutableLiveData<List<SetElement>> m_data;
    private final MutableLiveData<SetElement>       m_single;
    private final SetRepository                     m_repo;


    public SetViewModel( @NonNull Application application )
    {

        super(application);

        m_repo   = SetRepository.get();
        m_data   = m_repo.getLiveData();
        m_single = m_repo.getElementObserver();
    }


    public void access()
    {
        // notify the observers that data needs accessed
        m_repo.access();
    }


    public void add( SetElement elm )
    {

        m_repo.add(elm);
    }


    public void add( List<SetElement> elements )
    {

        m_repo.add(elements);
    }


    public LiveData<List<SetElement>> getObservable()
    {

        return m_data;
    }


    public MutableLiveData<SetElement> getSingleObserver()
    {

        return m_single;
    }


    public void observeSingle( LifecycleOwner parent,
                               SetElement item,
                               Observer<SetElement> observer )
    {

        m_single.observe(parent, observer);

        if (item != null)
            m_single.setValue(item);
    }


    public void drop( List<AdapterObjectInterface> elements )
    {

        m_repo.dropAOI(elements);
    }



    public void updateChartType( int id, int chartType )
    {
        // the chart member just needs to be updated
        // and no callbacks need to be issued
        if (chartType >= 0 && chartType < CT_MAX)
            m_repo.updateChartType(id, chartType);
    }
}
