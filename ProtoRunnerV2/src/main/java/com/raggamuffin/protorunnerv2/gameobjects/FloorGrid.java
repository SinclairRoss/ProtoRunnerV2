package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;

public class FloorGrid extends GameObject
{
	private final double DEPTH = -1.0;

    private GameObject m_Anchor;

	public FloorGrid(GameLogic game, GameObject anchor)
	{
		super(game, ModelType.FloorPanel);

        m_Anchor = anchor;
		m_Colour = m_Anchor.GetColour();
        m_Position.SetVector(m_Anchor.GetPosition());
		m_Position.J = DEPTH;
	}
	
	@Override
	public void Update(double deltaTime)
	{
        m_Position.SetVector(m_Anchor.GetPosition());
        m_Position.J = DEPTH;
	}

	@Override
	public boolean IsValid() 
	{
		return m_Anchor.IsValid();
	}

	@Override
	public void CleanUp()
	{}
}
