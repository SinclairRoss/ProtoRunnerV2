package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.utils.Timer;

import java.text.DecimalFormat;

public class InGameMessageHandler
{
    private GameLogic m_Game;

	private boolean m_Active;
	private boolean m_Running;

    private DecimalFormat m_Format;

	private UILabel m_Message;
	private String m_OriginalMessageText;
    private OptionalTextType m_OptionalTextType;
	private Timer m_MessageTimer;
	private int m_Priority;
	
	public InGameMessageHandler(GameLogic game, UIManager uiManager, GameAudioManager audio)
	{
        m_Game = game;

        m_Active = false;
		m_Running = false;

        m_Format = new DecimalFormat("0.00");

        m_Message = new UILabel(audio);
		m_Message.Hide();
        m_OptionalTextType = OptionalTextType.None;
		m_OriginalMessageText = "";
		m_MessageTimer = new Timer(0.0);
        m_Priority = Integer.MIN_VALUE;

		uiManager.AddUIElement(m_Message, false);
	}
	
	public void DisplayMessage(String text, OptionalTextType type, MessageOrientation orientation, int priority, double duration, double delay)
	{	
		if(!m_Active)
			return;
		
		// If the current message is of higher priority than the new incomming message.
		if(m_Priority > priority)
			return;

        if(duration < 0)
            m_MessageTimer.SetLimit(Double.MAX_VALUE);
        else
            m_MessageTimer.SetLimit(duration);

		m_Running = true;
		
		m_Message.Hide();
		
		m_OriginalMessageText = text;
		m_Priority = priority;

        m_OptionalTextType = type;

        m_Message.SetText(m_OriginalMessageText + GenerateFillerText());
		SetUpLabel(orientation);
		m_Message.Show(delay);

		m_MessageTimer.ResetTimer();
	}

    private String GenerateOptionalText()
    {
        switch(m_OptionalTextType)
        {
            case None:
                return "";
            case SecondWindTimeRemaining:
                return m_Format.format(m_Game.GetSecondWindHandler().GetSecondWindTimeRemaining());
            default:
                return "";
        }
    }

    private String GenerateFillerText()
    {
        switch(m_OptionalTextType)
        {
            case None:
                return "";
            case SecondWindTimeRemaining:
                return "4.12";
            default:
                return "";
        }
    }

	public void Update(double deltaTime)
	{
		if(!m_Running)
			return;
		
		if(m_Message.IsAnimationComplete())
			m_Message.SetOutputText(m_OriginalMessageText + GenerateOptionalText());
		
		m_MessageTimer.Update(deltaTime);
		
		if(m_MessageTimer.TimedOut())
			RemoveMessage();
	}
	
	private void SetUpLabel(MessageOrientation orientation)
	{
		switch(orientation)
		{
			case Center:
				m_Message.SetPosition(0, 0);
				m_Message.CentreHorizontal();
				break;
				
			case Left:
				m_Message.SetPosition(-1, 0);
				m_Message.AlignLeft();
				break;
				
			case Right:
				m_Message.SetPosition(1, 0);
				m_Message.AlignRight();
				break;

            case Top:
                m_Message.SetPosition(0, 0.6);
                m_Message.CentreHorizontal();
                break;
		}
	}
	
	public void RemoveMessage()
	{
		m_Message.Hide();
		m_Running = false;
		m_Priority = Integer.MIN_VALUE;
	}
	
	public void Activate()
	{
		m_Active = true;
	}
	
	public void Deactivate()
	{
		m_Active = false;
	}
	
	public void Clear()
	{
		RemoveMessage();
	}
}