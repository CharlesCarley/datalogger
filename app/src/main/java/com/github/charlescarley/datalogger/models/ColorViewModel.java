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

import com.github.charlescarley.datalogger.repositories.ColorRepository;
import com.github.charlescarley.datalogger.room.ColorElement;

import java.util.List;


public class ColorViewModel extends AndroidViewModel
{
    private final MutableLiveData<List<ColorElement>> m_data;


    public ColorViewModel( @NonNull Application application )
    {

        super(application);
        m_data = ColorRepository.get().getLiveData();
    }


    public LiveData<List<ColorElement>> getObservable()
    {

        return m_data;
    }
}
