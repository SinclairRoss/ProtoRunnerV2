package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.ColourManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;

public abstract class UIScreen 
{	
	private final double ButtonX =  0.9;
	private final double ButtonY =  0.65;
	private final double ButtonPadding = 0.25;

	protected GameLogic m_Game;
	protected UIManager m_UIManager;

    protected InGameMessageHandler m_MessageHandler;

    private int m_NumElements;
	private boolean m_HasBackButton;
	
	public UIScreen(GameLogic Game, UIManager uiManager)
	{
		m_Game = Game;
		m_UIManager = uiManager;

        m_MessageHandler = new InGameMessageHandler(m_Game, m_UIManager);

        m_NumElements = 0;
		
		m_HasBackButton = false;
	}
	
	public void Create()
	{
		m_NumElements = 0;
		m_HasBackButton = false;
        m_MessageHandler.Activate();
	}
	
	public void Remove()
    {
        m_MessageHandler.Clear();
        m_MessageHandler.Deactivate();
    }

	public void Update(double deltaTime)
    {
        m_MessageHandler.Update(deltaTime);
    }

	protected UILabel CreateTitle(String Name)
	{
		UILabel Title = new UILabel(m_Game.GetGameAudioManager(), m_UIManager);
		Title.SetText(Name);
		Title.SetPosition(-0.9, 0.0);
		Title.GetFont().SetAlignment(Font.Alignment.Left);

        m_UIManager.AddUIElement(Title);
		
		return Title;
	}
	
	protected UIProgressBar CreateProgressBar(String Name, double maxVal)
	{
        ColourManager cManager = m_Game.GetColourManager();
        double barLength = 1.1;
		UIProgressBar bar = new UIProgressBar(barLength, maxVal, cManager.GetAccentingColour(), cManager.GetAccentTintColour(), cManager.GetPrimaryColour(), Name, UIProgressBar.Alignment.Left, m_Game.GetGameAudioManager(), m_UIManager);
        bar.SetPosition(ButtonX - (barLength * 0.5), ButtonY - (ButtonPadding * m_NumElements));
		m_NumElements ++;

        m_UIManager.AddUIElement(bar);
		m_UIManager.AddUIElement(bar.GetLabel());
		
		return bar;
	}
	
	protected UIButton CreateButton(String text, UIScreens screen)
	{
		Publisher Pub = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.SwitchScreen);
		UIButton Button = new UIButton(Pub, m_Game.GetGameAudioManager(), m_UIManager, screen.ordinal());
		
		Button.SetText(text);
		Button.SetPosition(ButtonX, ButtonY - (ButtonPadding * m_NumElements));
		Button.GetFont().SetAlignment(Font.Alignment.Right);
		
		m_UIManager.AddUIElement(Button);
		
		m_NumElements ++;
		
		return Button;
	}

    protected UIButton CreateButton(String text, PublishedTopics topic)
    {
        Publisher Pub = m_Game.GetPubSubHub().CreatePublisher(topic);
        UIButton Button = new UIButton(Pub, m_Game.GetGameAudioManager(), m_UIManager, -1);

        Button.SetText(text);
        Button.SetPosition(ButtonX, ButtonY - (ButtonPadding * m_NumElements));
        Button.GetFont().SetAlignment(Font.Alignment.Right);

        m_UIManager.AddUIElement(Button);

        m_NumElements ++;

        return Button;
    }

    protected UIButton CreateBackButton(UIScreens screen)
    {
        Publisher Pub = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.SwitchScreen);
        UIButton Button = new UIButton(Pub, m_Game.GetGameAudioManager(), m_UIManager, screen.ordinal());

        Button.SetText(m_Game.GetContext().getString(R.string.button_back));
        Button.SetPosition(-0.9, -0.8);
        Button.GetFont().SetAlignment(Font.Alignment.Left);
        Button.GetFont().SetColour(m_Game.GetColourManager().GetAccentingColour());

        m_UIManager.AddUIElement(Button);

        m_HasBackButton = true;

        return Button;
    }
	
	protected UIButton CreateNextButton(UIScreens screen)
	{
		Publisher Pub = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.SwitchScreen);
		UIButton Button = new UIButton(Pub, m_Game.GetGameAudioManager(), m_UIManager, screen.ordinal());

        Button.SetText(m_Game.GetContext().getString(R.string.button_next));

        Button.SetPosition(0.9, -0.8);
        Button.GetFont().SetAlignment(Font.Alignment.Right);

        Button.GetFont().SetColour(m_Game.GetColourManager().GetAccentingColour());
		
		m_UIManager.AddUIElement(Button);
		
		return Button;
	}

    protected UILabel CreateLabel(String text)
    {
        UILabel label = new UILabel(m_Game.GetGameAudioManager(), m_UIManager);
        label.SetText(text);
        label.SetPosition(ButtonX, ButtonY - (ButtonPadding * m_NumElements));
        label.GetFont().SetAlignment(Font.Alignment.Right);

        m_UIManager.AddUIElement(label);

        m_NumElements ++;

        return label;
    }

    protected UILabel CreateLabelSubtext(UILabel parent, String text)
    {
        UILabel label = new UILabel(m_Game.GetGameAudioManager(), m_UIManager);
        label.SetFontSize(0.075);
        label.SetText(text);
        label.SetPosition(ButtonX, parent.GetPosition().J + parent.GetSize().J);
        label.GetFont().SetAlignment(Font.Alignment.Right);
        label.GetFont().SetColour(m_Game.GetColourManager().GetAccentingColour());

        m_UIManager.AddUIElement(label);

        return label;
    }

    public InGameMessageHandler GetMessageHandler()
    {
        return m_MessageHandler;
    }

	protected void ResetNumElements()
	{
		m_NumElements = 0;
	}
}