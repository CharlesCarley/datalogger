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
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "Collections")
public class CollectionElement implements AdapterObjectInterface
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "Value")
    private String value;

    @ColumnInfo(name = "Timestamp")
    private long timestamp;

    @ColumnInfo(name = "Parent")
    private int parent;

    @ColumnInfo(name = "OwningSet")
    private int owningSet;

    @ColumnInfo(name = "Note")
    private String note;

    @ColumnInfo(name = "DataType")
    private int type;

    @Ignore
    private int state;

    // predefined to be a copy of the parent
    // color
    @ColumnInfo(name = "Color")
    private int color;


    public CollectionElement()
    {

        id        = 0;
        value     = "";
        parent    = 0;
        color     = 0;
        state     = 0;
        note      = "";
        type      = 0;
        owningSet = 0;
    }


    public CollectionElement( SeriesElement element )
    {

        id        = 0;
        value     = "";
        state     = 0;
        parent    = element.getId();
        color     = element.getColor();
        owningSet = element.getParent();
        type      = element.getType();
        note      = "";
    }


    public String getName()
    {

        return value;
    }


    public double getValueDouble()
    {

        double v;
        try
        {
            v = Double.parseDouble(value);
        } catch (Exception e)
        {
            v = 0;
        }
        return v;

    }


    public int getValueInteger()
    {

        return (int) getValueDouble();
    }


    public int getId()
    {

        return id;
    }


    public void setId( int id )
    {

        this.id = id;
    }


    public int getState()
    {

        return state;
    }


    public void setState( int state )
    {

        this.state = state;
    }


    String getValue()
    {

        return value;
    }


    public void setValue( String value )
    {

        this.value = value;
    }


    public int getParent()
    {

        return parent;
    }


    public void setParent( int parent )
    {

        this.parent = parent;
    }


    @Override
    public int getColor()
    {

        return color;
    }


    public void setColor( int color )
    {

        this.color = color;
    }


    public long getTimestamp()
    {

        return timestamp;
    }


    public void setTimestamp( long timestamp )
    {

        this.timestamp = timestamp;
    }


    public int getOwningSet()
    {

        return owningSet;
    }


    public void setOwningSet( int owningSet )
    {

        this.owningSet = owningSet;
    }


    public String getNote()
    {

        return note;
    }


    public void setNote( String note )
    {

        this.note = note;
    }


    public int getType()
    {

        return type;
    }


    public void setType( int type )
    {

        this.type = type;
    }
}
