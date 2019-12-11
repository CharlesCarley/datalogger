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

import android.view.View;


public abstract class BaseActivityOkCancel extends ActivityBase
{
    // use with ok_cancel.xml, which declares
    // - onOkClicked
    // - onCancelClicked


    public void onOkClicked( View view )
    {
        // Initially set the default code.
        // onOK has the option to override.
        setResult(Codes.CODE_OK);

        if (onOK())
        {
            finish();
        }
    }


    public void onCancelClicked( View view )
    {

        setResult(Codes.CODE_CANCEL);
        onCancel();
        finish();
    }


    protected void onCancel()
    {
        
    }


    protected abstract boolean onOK();
}
