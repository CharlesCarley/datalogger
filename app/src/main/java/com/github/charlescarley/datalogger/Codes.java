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


public class Codes
{
    static final int    CODE_OK     = 1;
    static final int    CODE_CANCEL = 2;
    static final int    ACT_ADD     = 3;
    static final String ARG1        = "157682";
    static final int    GEN_MAX     = 10;


    // The values here are dependant on the order
    // declared in choices.xml
    static final int CL_INTEGER = 0;
    static final int CL_REAL    = 1;

    static final        int CT_SCATTER = 0;
    static final        int CT_LINE    = 1;
    static final        int CT_BAR     = 2;
    static final        int CT_AREA    = 3;
    // always +1
    public static final int CT_MAX     = 4;
}
