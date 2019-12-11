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
package com.github.charlescarley.datalogger;

import android.content.Context;
import android.graphics.Color;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.github.charlescarley.datalogger.repositories.SetRepository;
import com.github.charlescarley.datalogger.room.ApplicationDatabase;
import com.github.charlescarley.datalogger.room.SeriesElement;
import com.github.charlescarley.datalogger.room.SetElement;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@RunWith(AndroidJUnit4.class)
public class RepoTest
{
    private static final String TEST_DB = "repo-test";


    static private ApplicationDatabase getDB( Context ctx )
    {

        RoomDatabase.Builder<ApplicationDatabase> builder;

        ApplicationDatabase adb;
        builder = Room.inMemoryDatabaseBuilder(ctx, ApplicationDatabase.class);
        adb     = builder.build();
        return adb;
    }


    @Test
    public void SetTest1()
    {

        Utils.LogF("== > SetTest1");

        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ApplicationDatabase db = getDB(ctx);

        SetRepository setRepo = SetRepository.get();

        SetElement newElement = new SetElement();
        newElement.setName("Hello World");
        newElement.setColor(Color.RED);

        db._sets().insert(newElement);
        List<SetElement> elements = (db._sets().entries());

        assertNotEquals(null, elements);
        assertEquals(1, elements.size());

        for (SetElement ele : elements)
        {
            assertEquals("Hello World", ele.getName());
            assertEquals(Color.RED, ele.getColor());
        }
    }


    private SetElement addAndRetrieveSet( ApplicationDatabase db )
    {

        SetElement newElement = new SetElement();
        newElement.setName("Hello World");
        newElement.setColor(Color.RED);


        assertEquals(0, newElement.getId());
        db._sets().insert(newElement);


        List<SetElement> elements = (db._sets().entries());
        assertNotEquals(null, elements);
        assertEquals(1, elements.size());

        newElement = elements.get(0);
        assertEquals(1, newElement.getId());
        return newElement;
    }


    @Test
    public void SeriesTest()
    {

        Utils.LogF("== > SeriesTest");

        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ApplicationDatabase db = getDB(ctx);


        SetElement set = addAndRetrieveSet(db);


        SeriesElement series = new SeriesElement();

        series.setName("Series");
        series.setParent(set.getId());
        series.setColor(Color.GREEN);
        db._series().insert(series);


        List<SeriesElement> elements = (db._series().entries(set.getId()));


        assertNotEquals(null, elements);
        assertEquals(1, elements.size());

        for (SeriesElement ele : elements)
        {
            assertEquals("Series", ele.getName());
            assertEquals(Color.GREEN, ele.getColor());
        }
    }
}
