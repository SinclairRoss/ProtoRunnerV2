// Author: 	Sinclair Ross.
// Notes:	Application logic is the code that defines what an application does. 
//			This is written as an abstract base class in order to make it easy to switch between game states.

package com.raggamuffin.protorunnerv2.gamelogic;

import android.content.Context;

import com.raggamuffin.protorunnerv2.master.RendererPacket;

public abstract class ApplicationLogic 
{
	protected RendererPacket m_Packet;
	protected Context m_Context;
	
	public ApplicationLogic(Context context, RendererPacket packet) 
	{
		m_Packet = packet;
		m_Context = context;
	}
	
	public abstract void Update(double deltaTime);
	public abstract void Pause();
	public abstract void Resume();
	
	public Context GetContext()
	{
		return m_Context;
	}
}
