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


@Entity(tableName = "Sets")
public class SetElement implements Parcelable, AdapterObjectInterface
{

    @Ignore
    public static final Creator<SetElement> CREATOR = new Creator<SetElement>()
    {
        @Override
        public SetElement
        createFromParcel( Parcel in )
        {

            return new SetElement(in);
        }


        @Override
        public SetElement[]
        newArray( int size )
        {

            return new SetElement[size];
        }
    };


    @PrimaryKey(autoGenerate = true)
    private int    id;
    @ColumnInfo(name = "Color")
    private int    color;
    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "ChartType")
    private int chartType;

    @Ignore
    private int state;


    public SetElement()
    {

        id        = 0;
        name      = "";
        color     = 0;
        state     = 0;
        chartType = 0;
    }


    private SetElement( Parcel in )
    {

        id        = in.readInt();
        name      = in.readString();
        color     = in.readInt();
        chartType = in.readInt();
        state     = 0;
    }


    @Override
    public void writeToParcel( Parcel dest, int flags )
    {

        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(color);
        dest.writeInt(chartType);
    }


    @Override
    public int describeContents()
    {

        return 0;
    }


    public int getId()
    {

        return id;
    }


    public void setId( int id )
    {

        this.id = id;
    }


    public String getName()
    {

        return name;
    }


    public void setName( String name )
    {

        this.name = name;
    }


    public int getColor()
    {

        return color;
    }


    public void setColor( int color )
    {

        this.color = color;
    }


    public int getState()
    {

        return state;
    }


    public void setState( int state )
    {

        this.state = state;
    }


    public int getChartType()
    {

        return chartType;
    }


    public void setChartType( int chartType )
    {

        this.chartType = chartType;
    }
}
