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

import java.util.List;


@Dao
public interface CollectionInterface
{
    @Query("SELECT * from Collections WHERE Parent LIKE :id ORDER BY Timestamp DESC")
    List<CollectionElement> entries( int id );

    @Query("SELECT * FROM Collections WHERE OwningSet LIKE :id ORDER BY Color DESC, Timestamp ASC")
    List<CollectionElement> mergedEntries( int id );

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert( CollectionElement entity );

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert( List<CollectionElement> list );

    @Delete
    void drop( List<CollectionElement> elements );

    @Delete
    void drop( CollectionElement elements );


    @Query("DELETE from Collections WHERE OwningSet LIKE :id")
    void clearFromSet( int id );


    @Query("DELETE from Collections WHERE Parent LIKE :id")
    void clear( int id );


    @Query("UPDATE Collections Set Color=:color WHERE Parent LIKE :id")
    void updateColor( int id, int color );


    @Query("DELETE from Collections")
    void clear();
}
