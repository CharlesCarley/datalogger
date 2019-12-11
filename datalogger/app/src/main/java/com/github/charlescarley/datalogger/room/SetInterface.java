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


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SetInterface
{
    @Query("SELECT * from Sets ORDER BY Id DESC")
    List<SetElement> entries();

    @Query("SELECT * from Sets WHERE Id Like:id")
    SetElement entry( int id );

    @Update
    void update( SetElement item );

    @Update
    void update( List<SetElement> elements );

    @Query("UPDATE Sets SET ChartType =:chart WHERE Id LIKE :id")
    void update(int id, int chart);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert( List<SetElement> item );

    @Insert
    void insert( SetElement item );

    @Delete
    void drop( SetElement item );


    @Delete
    void drop( List<SetElement> item );


    @Query("DELETE from Sets")
    void clear();
}

