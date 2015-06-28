package com.raggamuffin.protorunnerv2.gameobjects;

public class ExhibitionCameraAnchor extends GameObject
{
	public ExhibitionCameraAnchor()
	{
		super(null, null);
		
		m_Position.SetVector(0.0, 40.0, 0.0);
		m_Forward.SetVector(0.0,  -1.0, 0.0);
		m_Up.SetVector(0.0,0.0,1.0);
	}

	@Override
	public boolean IsValid() 
	{
		return true;
	}
}
