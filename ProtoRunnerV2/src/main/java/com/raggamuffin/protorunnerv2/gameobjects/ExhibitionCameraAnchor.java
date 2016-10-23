package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class ExhibitionCameraAnchor extends GameObject
{
	public ExhibitionCameraAnchor(GameLogic game)
	{
		super(game, ModelType.Nothing);
		
		m_Position.SetVector(0.0, 40.0, 0.0);
		m_Forward.SetVector(0.0,  -1.0, 0.0);
		m_Up.SetVector(0.0,0.0,1.0);
	}

	@Override
	public boolean IsValid() 
	{
		return true;
	}

	@Override
	public void CleanUp()
	{}
}
