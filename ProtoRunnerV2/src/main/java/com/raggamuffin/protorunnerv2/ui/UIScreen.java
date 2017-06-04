package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;

public abstract class UIScreen 
{
	protected GameLogic m_Game;
	protected UIManager m_UIManager;
	
	public UIScreen(GameLogic game, UIManager uiManager)
	{
		m_Game = game;
		m_UIManager = uiManager;
	}
	
	public abstract void Create();
	public abstract void Destroy();

	public abstract void Update(double deltaTime);
}