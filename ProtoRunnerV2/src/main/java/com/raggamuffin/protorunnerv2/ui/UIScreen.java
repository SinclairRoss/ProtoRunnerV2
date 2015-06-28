package com.raggamuffin.protorunnerv2.ui;


import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.ColourManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.Colours;

public abstract class UIScreen 
{	
	private final double ButtonX =  0.8;
	private final double ButtonY =  0.65;
	private final double ButtonPadding = 0.25;

	protected GameLogic m_Game;
	protected UIManager m_UIManager;

    protected InGameMessageHandler m_MessageHandler;

    private int m_NumElements;
	private boolean m_HasBackButton;
	
	public UIScreen(GameLogic Game, UIManager Manager)
	{
		m_Game = Game;
		m_UIManager = Manager;

        m_MessageHandler = new InGameMessageHandler(m_Game, m_UIManager, m_Game.GetGameAudioManager());

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
		UILabel Title = new UILabel(m_Game.GetGameAudioManager());
		Title.SetText(Name);
		Title.SetPosition(m_UIManager.GetScreenRatio() * -0.9, 0.0);
		Title.GetFont().SetAlignment(Font.Alignment.Left);

        m_UIManager.AddUIElement(Title);
		
		return Title;
	}
	
	protected UIProgressBar CreateProgressBar(String Name, double maxVal)
	{
        ColourManager cManager = m_Game.GetColourManager();
		UIProgressBar Bar = new UIProgressBar(2.0, maxVal, cManager.GetAccentingColour(), cManager.GetAccentTintColour(), cManager.GetPrimaryColour(), Name, UIProgressBar.Alignment.Left, m_Game.GetGameAudioManager());
        Bar.SetPosition(m_UIManager.GetScreenRatio() * ButtonX - (Bar.GetMaxLength() * 0.5), ButtonY - (ButtonPadding * m_NumElements));

		m_NumElements ++;

        m_UIManager.AddUIElement(Bar);
		m_UIManager.AddUIElement(Bar.GetLabel());
		
		return Bar;
	}
	
	protected UIButton CreateButton(String Name, UIScreens Screen)
	{
		Publisher Pub = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.SwitchScreen);
		UIButton Button = new UIButton(Pub, m_Game.GetGameAudioManager(), Screen.ordinal());
		
		Button.SetText(Name);
		Button.SetPosition(m_UIManager.GetScreenRatio() * ButtonX, ButtonY - (ButtonPadding * m_NumElements));
		Button.GetFont().SetAlignment(Font.Alignment.Right);
		
		m_UIManager.AddUIElement(Button);
		
		m_NumElements ++;
		
		return Button;
	}

    protected UIButton CreateButton(String text, PublishedTopics topic)
    {
        Publisher Pub = m_Game.GetPubSubHub().CreatePublisher(topic);
        UIButton Button = new UIButton(Pub, m_Game.GetGameAudioManager(),  -1);

        Button.SetText(text);
        Button.SetPosition(m_UIManager.GetScreenRatio() * ButtonX, ButtonY - (ButtonPadding * m_NumElements));
        Button.GetFont().SetAlignment(Font.Alignment.Right);

        m_UIManager.AddUIElement(Button);

        m_NumElements ++;

        return Button;
    }

    protected UIButton CreateBackButton(UIScreens screen)
    {
        Publisher Pub = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.SwitchScreen);
        UIButton Button = new UIButton(Pub, m_Game.GetGameAudioManager(), screen.ordinal());

        Button.SetText(m_Game.GetContext().getString(R.string.button_back));
        Button.SetPosition(m_UIManager.GetScreenRatio() * -0.9, -1.0 + ButtonPadding);
        Button.GetFont().SetAlignment(Font.Alignment.Left);
        Button.GetFont().SetColour(m_Game.GetColourManager().GetAccentingColour());

        m_UIManager.AddUIElement(Button);

        m_HasBackButton = true;

        return Button;
    }
	
	protected UIButton CreateNextButton(UIScreens screen)
	{
		Publisher Pub = m_Game.GetPubSubHub().CreatePublisher(PublishedTopics.SwitchScreen);
		UIButton Button = new UIButton(Pub, m_Game.GetGameAudioManager(), screen.ordinal());

        Button.SetText(m_Game.GetContext().getString(R.string.button_next));
		
		if(m_HasBackButton)
		{
			Button.SetPosition(m_UIManager.GetScreenRatio() * -0.4, -1.0 + ButtonPadding);
			Button.GetFont().SetAlignment(Font.Alignment.Right);
		}
		else
		{
			Button.SetPosition(m_UIManager.GetScreenRatio() * -0.9, -1.0 + ButtonPadding);
			Button.GetFont().SetAlignment(Font.Alignment.Left);
		}

        Button.GetFont().SetColour(Colours.Cyan);
		
		m_UIManager.AddUIElement(Button);
		
		return Button;
	}

	protected UILabel CreateLabel(String text)
	{
		UILabel label = new UILabel(m_Game.GetGameAudioManager());
		label.SetText(text);
		label.SetPosition(m_UIManager.GetScreenRatio() * ButtonX, ButtonY - (ButtonPadding * m_NumElements));
        label.GetFont().SetAlignment(Font.Alignment.Right);

        m_UIManager.AddUIElement(label);
		
		m_NumElements ++;
		
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
