package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Runner;
import com.raggamuffin.protorunnerv2.managers.ColourManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class UIScreen_Play extends UIScreen
{
    private UIObject_StatusBar m_StaminaGauge;

	public UIScreen_Play(GameLogic game, UIManager uiManager)
	{
		super(game, uiManager);

        PubSubHub pubSub = m_Game.GetPubSubHub();
        pubSub.SubscribeToTopic(PublishedTopics.GameReady, new GameReadySubscriber());
    }

	@Override
	public void Create() 
	{
        String label = m_Game.GetContext().getString(R.string.label_stamina);
        m_StaminaGauge = new UIObject_StatusBar(label, Colours.EmeraldGreen, Colours.Red, 0, 0.8, m_UIManager);
	}

	@Override
	public void Destroy()
	{
        m_StaminaGauge = null;
	}

	@Override
	public void Update(double deltaTime) 
	{
        Vehicle_Runner player = m_Game.GetVehicleManager().GetPlayer();
        if(player != null)
        {
            m_StaminaGauge.SetValue(player.GetStamina());
        }

        m_StaminaGauge.Update(deltaTime);
	}

    private class GameReadySubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        { }
    }}