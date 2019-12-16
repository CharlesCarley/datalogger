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
package com.github.charlescarley.datalogger.repositories;


import androidx.annotation.NonNull;

import com.github.charlescarley.datalogger.Utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


abstract class RepositoryThread<T> extends Thread
{
    private final Queue<Packet> m_queue;
    private Stack<Packet> m_pool;
    protected String m_debug;


    RepositoryThread()
    {

        m_queue = new LinkedList<>();
        m_pool  = new Stack<>();
        m_debug = "<RepositoryThread>";
    }

    protected void enqueue(Packet packet)
    {
        synchronized (m_queue)
        {
            m_queue.add(packet);
        }
    }

    private boolean hasPackets()
    {
        synchronized (m_queue)
        {
            return !m_queue.isEmpty();
        }
    }

    private Packet nextPacket()
    {
        synchronized (m_queue)
        {
            return m_queue.remove();
        }
    }



    @Override
    public void run()
    {

        while (isAlive())
        {
            try
            {
                if (hasPackets())
                {
                    while (hasPackets())
                    {
                        Packet packet = nextPacket();
                        DispatchCode code = packet.getCmd();

                        Utils.LogF("%s: Processing cmd(%s)", m_debug, code.toString());
                        handlePacket(packet);

                        // put packet back into circulation
                        deallocate(packet);
                    }
                }
                else
                {
                    synchronized (this)
                    {
                        Utils.LogF("%s: Waiting for further commands", m_debug);
                        wait();
                        Utils.LogF("%s: Woke up", m_debug);
                    }
                }
            } catch (Exception e)
            {
                Utils.LogF("%s: Thread Exception %s", m_debug, e.getLocalizedMessage());
            }
        }
    }


    protected abstract void handlePacket( @NonNull Packet packet );


    void dispatch()
    {

        // begin executing on the
        // first dispatch
        if (!isAlive())
            start();
        else
        {
            synchronized (this)
            {
                notify();
            }
        }
    }


    Packet allocate( Class<? extends Packet> classType )
    {
        //Utils.LogF(" ===> Packet.allocate(Stack size: %d)", m_pool.size());
        synchronized (this)
        {
            if (m_pool.isEmpty())
            {
                //Utils.LogF("Empty pool, allocating new Packet %s", classType.getName());

                try
                {
                    Packet obj = (Packet) classType.newInstance();
                    m_pool.push(obj);

                } catch (Exception e)
                {
                    Utils.Log(e);
                }
            }
            return m_pool.pop();
        }
    }


    private void deallocate( Packet pack )
    {
        //Utils.LogF("Adding packet back to pool");
        synchronized (this)
        {
            pack.donePacket();
            m_pool.push(pack);
        }
    }
}
