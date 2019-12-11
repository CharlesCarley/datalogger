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

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName ="Colors")
public class ColorElement
{
    @ColumnInfo(name = "Color")
    private int color;


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    private int id;


    public ColorElement()
    {
        color = 0;
        id = 0;
    }

    public int getColor()
    {
        return color;
    }

    public void setColor( int color )
    {
        this.color = color;
    }


    public int getId()
    {

        return id;
    }


    public void setId( int id )
    {

        this.id = id;
    }
}
