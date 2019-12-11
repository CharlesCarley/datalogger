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
package com.github.charlescarley.datalogger.charts;

class Axis
{
    static final float SCALE = 2;

    private float m_min, m_max;
    private float A;
    private float B;
    private float f;
    private float o;


    Axis()
    {

        m_min = Float.MAX_VALUE;
        m_max = Float.MIN_VALUE;
        A     = 1.f;
        B     = 0.f;
        f     = 1;
        o     = 0.f;
    }


    void reset()
    {

        m_min = Float.MAX_VALUE;
        m_max = Float.MIN_VALUE;
    }


    void compare( float v )
    {

        if (v < m_min)
            m_min = v;
        if (v > m_max)
            m_max = v;
    }


    float length()
    {

        return Math.max(m_max - m_min, SCALE);
    }


    void scaleToWindowRect( final float len )
    {
        // Scale the min and max of the input to the window
        // length. Both are constrained to a minimum of SCALE

        // Ax <min(x), max(x)>
        // Ay <min(y), max(y)>
        // R = <l,t,r,b> || <x1, y1, x2, y2>
        // L = (A_[1] - A_[0])
        // A = L / (_2 - _1)
        // B = L / 2

        final float L = length();

        A = L / Math.max(len, SCALE);

        // Save for later multiplication
        A = 1.f / A;

        B = L / 2.f;
    }


    void setOffset( float offs )
    {

        o = offs;
    }


    void setScale( float v )
    {

        if (Math.abs(v) > 0.001)
            f = v;
    }


    float transformPoint( float v )
    {

        return (A * f) * (v - o + B);
    }


    float reversePoint( float p )
    {
        // p = Af(v - o + B)
        //
        //       -(AfB - Afo - p)
        // v =   --------------
        //            Af
        float Af = A * f;
        float AfB = Af * B;
        float Afo = Af * o;

        return -(AfB - Afo - p) / Af;
    }


    float center()
    {

        return (m_min + m_max) / 2.f;
    }
}
