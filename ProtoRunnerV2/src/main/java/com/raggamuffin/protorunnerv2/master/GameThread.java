// Author: 	Sinclair Ross
// Date:	23/09/2014
// Notes:	This thread class is responsible for updating the application logic that is passed into it.

package com.raggamuffin.protorunnerv2.master;

import com.raggamuffin.protorunnerv2.gamelogic.ApplicationLogic;
import com.raggamuffin.protorunnerv2.utils.FrameRateCounter;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

import android.os.Handler;
import android.os.Message;

public class GameThread extends Thread
{
	private Long m_StartTime;
	private Long m_EndTime;
	private Long m_DeltaTime;
	private Long m_FrameRate;
	
	private boolean m_Running; 
    private boolean m_Paused; 
    
    private ControlScheme m_ControlScheme;
    private ApplicationLogic m_Logic;
    private Handler m_Handler;

    private FrameRateCounter m_LogicDurationCounter;
    
	public GameThread(ApplicationLogic logic, ControlScheme scheme, Handler handler)
	{
		super();

		m_Logic 		= logic;
		m_ControlScheme = scheme;
		m_Handler 		= handler;
		
		m_StartTime = 0L;
		m_EndTime 	= 0L;
		m_DeltaTime = 0L;
		m_FrameRate = 1000L / 40;

		m_Running = true;
	    m_Paused = false;

        m_LogicDurationCounter = new FrameRateCounter();
	}
	
	@Override 
    public void run() 
    { 
		m_StartTime = System.currentTimeMillis();
        
        while(m_Running)
        {
            if (!m_Paused)
            {
                m_EndTime = m_StartTime;
                m_StartTime = System.currentTimeMillis();
                m_DeltaTime += m_StartTime - m_EndTime;

                if (m_DeltaTime >= m_FrameRate)
                {
                    m_DeltaTime = MathsHelper.Clamp(m_DeltaTime, 0, m_FrameRate * 2);

                    double deltaTime = ((double) m_DeltaTime) * 0.001;

                    m_ControlScheme.Update(deltaTime);

                    m_LogicDurationCounter.StartFrame();
                    m_Logic.Update(deltaTime);
                    m_LogicDurationCounter.EndFrame();
                    m_LogicDurationCounter.LogFrameDuration("Logic", 16L);
                    m_DeltaTime = 0L;

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
    } 
      
    public void resumeThread()
    {
        m_StartTime = System.currentTimeMillis();
        m_Paused = false;
        m_Logic.Resume();
    }
}
