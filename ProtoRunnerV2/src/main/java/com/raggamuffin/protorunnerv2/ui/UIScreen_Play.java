package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Runner;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class UIScreen_Play extends UIScreen
{
    private UIObject_StatusBar m_StaminaGauge;

    public UIScreen_Play(GameLogic game, UIManager uiManager)
    {
        super(game, uiManager);
    }

    @Override
    public void Create()
    {
        String label = m_Game.GetContext().getString(R.string.label_stamina);
        m_StaminaGauge = new UIObject_StatusBar(label, Colours.RunnerBlue, Colours.Red, 0, 0.8, m_UIManager);
    }

    @Override
    public void CleanUp()
    {
        m_StaminaGauge = null;
    }

    @Override
    public void Update(double deltaTime)
    {
        Vehicle_Runner player = m_Game.GetVehicleManager().GetPlayer();
        if (player != null)
        {
            m_StaminaGauge.SetValue(player.GetStamina());
        }

        m_StaminaGauge.Update(deltaTime);
    }
}