package com.github.charlescarley.datalogger;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.github.charlescarley.datalogger.room.ApplicationDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
    @Test
    public void useAppContext()
    {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.github.charlescarley.datalogger", appContext.getPackageName());
    }


    private ApplicationDatabase getDB( Context ctx, String name )
    {

        RoomDatabase.Builder<ApplicationDatabase> builder;

        ApplicationDatabase adb;
        builder = Room.databaseBuilder(ctx, ApplicationDatabase.class, name);
        builder.allowMainThreadQueries();
        adb = builder.build();
        return adb;
    }


    @Test
    public void getAppDataBase()
    {

        Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ApplicationDatabase db;

        try
        {
            db = getDB(ctx, null);
            fail();
        } catch (Exception e)
        {
            Log.d("getAppDataBase", "getAppDataBase ==>", e.fillInStackTrace());
        }

        db = getDB(ctx, "getAppDataBase");
        assertNotEquals(null, db);
    }
}
