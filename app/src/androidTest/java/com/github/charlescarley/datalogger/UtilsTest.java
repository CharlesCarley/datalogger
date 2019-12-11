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

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;


public class UtilsTest
{

    @Test
    public void millisecondsToString()
    {

        String val, expected;
        Calendar calendar = Calendar.getInstance();
        calendar.set(1900, Calendar.JANUARY, 1, 0, 0, 0);

        val      = Utils.millisecondsToString(calendar.getTimeInMillis());
        expected = "1/1/1900 12:00 AM";
        assertEquals(expected, val);

        int i = 0, d;
        for (i = 0; i < 24; ++i)
        {
            calendar.set(1900, Calendar.JANUARY, 1, i, 0, 0);

            val = Utils.millisecondsToString(calendar.getTimeInMillis());
            d = i % 12;
            if (d == 0) d = 12;

            if (i < 12)
                expected = String.format("1/1/1900 %d:00 AM", d);
            else
                expected = String.format("1/1/1900 %d:00 PM", d);

            Utils.LogF("%s ==> %s", expected, val);
            assertEquals(expected, val);
        }
    }
}