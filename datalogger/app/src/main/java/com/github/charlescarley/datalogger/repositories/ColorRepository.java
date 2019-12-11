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

import com.github.charlescarley.datalogger.room.ColorElement;

import java.util.List;


public class ColorRepository
{
    private static final ColorRepository                     m_repo = new ColorRepository();
    private final        ColorDispatcher                     m_dispatcher;
    private              MutableLiveData<List<ColorElement>> m_live;


    private ColorRepository()
    {

        m_live       = new MutableLiveData<>();
        m_dispatcher = new ColorDispatcher(m_live);
    }


    public static ColorRepository get()
    {
        // Singleton access to the repo
        return m_repo;
    }


    public MutableLiveData<List<ColorElement>> getLiveData()
    {

        return m_live;
    }


    public void access()
    {

        m_dispatcher.addCommand(DispatchCode.SYNC);
        m_dispatcher.dispatch();
    }


    public void clear()
    {

        m_dispatcher.addCommand(DispatchCode.CLEAR);
        m_dispatcher.dispatch();
    }


    public void push( ColorElement ent )
    {

        m_dispatcher.addCommand(DispatchCode.PUSH, ent);
        m_dispatcher.dispatch();
    }


    public void push( List<ColorElement> ent )
    {

        m_dispatcher.addCommand(DispatchCode.PUSH, ent);
        m_dispatcher.dispatch();
    }
}
