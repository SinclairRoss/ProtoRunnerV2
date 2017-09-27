// Author: 	Sinclair Ross
// Date:	23/09/2014
// Notes:	This thread class is responsible for updating the application logic that is passed into it.

package com.raggamuffin.protorunnerv2.master;

import com.raggamuffin.protorunnerv2.gamelogic.ApplicationLogic;
import com.raggamuffin.protorunnerv2.utils.FPSCounter;
import com.raggamuffin.protorunnerv2.utils.SystemTimer;

public class GameThread extends Thread
{
	private boolean m_Running; 
    private boolean m_Paused;
    
    private ControlScheme m_ControlScheme;
    private ApplicationLogic m_Logic;

    private SystemTimer m_Timer;
    private double m_MaxDeltaTime;

    private FPSCounter m_FPS;

	public GameThread(ApplicationLogic logic, ControlScheme scheme)
	{
		super();

		m_Logic = logic;
		m_ControlScheme = scheme;

        double framePeriod = 1.0 / 60.0;
        m_Timer = new SystemTimer(framePeriod);
        m_Timer.Start();

        m_MaxDeltaTime = framePeriod * 3;

		m_Running = true;
	    m_Paused = false;

        m_FPS = new FPSCounter("Logic");
	}

	@Override 
    public void run() 
    {
        while(m_Running)
        {
         //   m_FPS.Update();

            if (!m_Paused)
            {
                if (m_Timer.HasElapsed())
                {
                    double runTime = m_Timer.GetRunTimeMillis();
                    double deltaTime = runTime / 1000;

                    TimeKeeper.Instance().Update(deltaTime);

                    deltaTime = deltaTime > m_MaxDeltaTime ? m_MaxDeltaTime : deltaTime;
                    m_Timer.Start();

                 //   Log.e("thread", Double.toString(deltaTime));

                 //   m_FPS.Bump();
                    m_ControlScheme.Update(deltaTime);

                    m_Logic.Update(deltaTime);
                }
            }
        }
    }

	public void DestroyThread()
	{
		m_Running = false;
        m_Logic.Destroy();
	}
    
    public void pauseThread() 
    { 
        m_Paused = true; 
        m_Logic.Pause();

        m_Timer.Stop();
    } 
      
    public void resumeThread()
    {
        m_Paused = false;
        m_Logic.Resume();

        m_Timer.Start();
    }
}
