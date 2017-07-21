// Author: 	Sinclair Ross
// Date:	23/09/2014
// Notes:	This thread class is responsible for updating the application logic that is passed into it.

package com.raggamuffin.protorunnerv2.master;

import android.os.Handler;
import android.os.Message;

import com.raggamuffin.protorunnerv2.gamelogic.ApplicationLogic;
import com.raggamuffin.protorunnerv2.utils.FrameRateCounter;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class GameThread extends Thread
{
	private boolean m_Running; 
    private boolean m_Paused; 
    
    private ControlScheme m_ControlScheme;
    private ApplicationLogic m_Logic;
    private Handler m_Handler;

    private FrameRateCounter m_LogicDurationCounter;
    private Timer m_Timer;
    private double m_MaxDeltaTime;

	public GameThread(ApplicationLogic logic, ControlScheme scheme, Handler handler)
	{
		super();

		m_Logic 		= logic;
		m_ControlScheme = scheme;
		m_Handler 		= handler;

        double framePeriod = 1.0 / 60.0;
        m_Timer = new Timer(framePeriod);
        m_Timer.Start();

        m_MaxDeltaTime = framePeriod * 3;

		m_Running = true;
	    m_Paused = false;

        m_LogicDurationCounter = new FrameRateCounter();
	}
	
	@Override 
    public void run() 
    {
        while(m_Running)
        {
            if (!m_Paused)
            {
                if (m_Timer.HasElapsed())
                {
                    double deltaTime = m_Timer.GetRunTimeMillis() * 0.001;
                    deltaTime = deltaTime > m_MaxDeltaTime ? m_MaxDeltaTime : deltaTime;

                    m_Timer.Start();

                    m_ControlScheme.Update(deltaTime);

                    //m_LogicDurationCounter.StartFrame();
                    m_Logic.Update(deltaTime);
                   // m_LogicDurationCounter.EndFrame();
                   // m_LogicDurationCounter.LogFrameDuration("Logic", 16L);

                    m_Handler.sendMessage(ComposeMessage(GameActivity.REQUEST_RENDER));
                }
            }
        }
    } 
	
	public Message ComposeMessage(int what)
	{
		Message msg = Message.obtain();
		msg.what 	= what;
	
		return msg;
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
