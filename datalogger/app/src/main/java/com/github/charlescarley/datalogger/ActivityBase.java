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


import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;

import static com.github.charlescarley.datalogger.Utils.LogError;


public class ActivityBase extends AppCompatActivity implements NavHost
{
    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item )
    {
        // provide the correct behaviour when
        // the back button is clicked.
        // Enabled/Disabled when the title has been set
        // with setActionBarTitle(..., true)
        if (item.getItemId() == android.R.id.home)
        {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setActionBarTitle( int resourceId, boolean backEnabled, Object ... varg)
    {
        // Convenience function, for using
        // a resource id over a string.
        String str = "";
        try {
            str = getString(resourceId, varg);
        }catch (Exception e)
        {
            Utils.Log(e);
        }

        setActionBarTitle(str, backEnabled);
    }


    public void setActionBarTitle( String title, boolean backEnabled )
    {

        ActionBar ab = getSupportActionBar();
        if (ab != null)
        {
            ab.setTitle(title);
            ab.setDisplayHomeAsUpEnabled(backEnabled);
        }
        else
            LogError("setActionBarTitle - no action bar was found");
    }


    @NonNull
    @Override
    public NavController getNavController()
    {

        return null;
    }
}
