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


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "Series")
public class SeriesElement implements Parcelable, AdapterObjectInterface
{
    public static final Creator<SeriesElement> CREATOR = new Creator<SeriesElement>()
    {
        @Override
        public SeriesElement createFromParcel( Parcel in )
        {

            return new SeriesElement(in);
        }


        @Override
        public SeriesElement[] newArray( int size )
        {

            return new SeriesElement[size];
        }
    };


    @PrimaryKey(autoGenerate = true)
    private int    id;
    @ColumnInfo(name = "Parent")
    private int    parent;
    @ColumnInfo(name = "Name")
    private String name;
    @ColumnInfo(name = "Type")
    private int    type;
    @ColumnInfo(name = "Color")
    private int    color;


    // not saved in the database
    @Ignore
    private int state;


    public SeriesElement()
    {

        name   = "";
        parent = 0;
        id     = 0;
        type   = 0;
        color  = 0;
        state  = -1;
    }


    protected SeriesElement( Parcel in )
    {

        id     = in.readInt();
        parent = in.readInt();
        name   = in.readString();
        type   = in.readInt();
        color  = in.readInt();
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


    public int getParent()
    {

        return parent;
    }


    public void setParent( int parent )
    {

        this.parent = parent;
    }


    public String getName()
    {

        return name;
    }


    public void setName( String name )
    {

        this.name = name;
    }


    public int getType()
    {

        return type;
    }


    public void setType( int type )
    {

        this.type = type;
    }


    public int getColor()
    {

        return color;
    }


    public void setColor( int color )
    {

        this.color = color;
    }


    @Override
    public int describeContents()
    {

        return 0;
    }


    @Override
    public void writeToParcel( Parcel dest, int flags )
    {

        dest.writeInt(id);
        dest.writeInt(parent);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeInt(color);
    }


}
