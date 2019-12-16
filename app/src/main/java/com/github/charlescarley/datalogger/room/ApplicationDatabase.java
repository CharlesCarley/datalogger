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
package com.github.charlescarley.datalogger.room;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {
    ColorElement.class,
    SetElement.class,
    SeriesElement.class,
    CollectionElement.class
},
    exportSchema = false, version = 2)
public abstract class ApplicationDatabase extends RoomDatabase
{
    private static final String DATABASE_NAME = "application-db";

    private static ApplicationDatabase m_database = null;


    public static ApplicationDatabase getSingleton()
    {
        // Singleton access to the underlying database
        return m_database;
    }


    public static void register( Context ctx )
    {
        if (m_database == null)
        {
            RoomDatabase.Builder<ApplicationDatabase> builder;
            builder = Room.databaseBuilder(ctx, ApplicationDatabase.class, DATABASE_NAME);
            builder.fallbackToDestructiveMigration();
            m_database = builder.build();
        }
    }

    public abstract ColorInterface _colors();
    public abstract SetInterface _sets();
    public abstract SeriesInterface _series();
    public abstract CollectionInterface _collections();
}
