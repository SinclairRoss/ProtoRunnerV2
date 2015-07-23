package com.raggamuffin.protorunnerv2.ui;

import android.util.Log;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.ColourManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;

public class RebootScreen extends UIScreen
{
    private UILabel m_Label;
    private UIProgressBar m_Bar;

    public RebootScreen(GameLogic Game, UIManager uiManager)
    {
        super(Game, uiManager);

        m_Label = null;
        m_Bar = null;
    }

    @Override
    public void Create()
    {
        super.Create();

        m_Label = new UILabel(m_Game.GetGameAudioManager(), m_UIManager);
        m_Label.SetText(m_Game.GetContext().getString(R.string.down_but_not_out_text));
        m_Label.SetPosition(0, 0);
        m_Label.GetFont().SetAlignment(Font.Alignment.Center);

        ColourManager cManager = m_Game.GetColourManager();

        m_Bar = new UIProgressBar(1.5, 1, cManager.GetAccentingColour(), cManager.GetAccentTintColour(), cManager.GetPrimaryColour(), m_Game.GetContext().getString(R.string.empty), UIProgressBar.Alignment.Center, m_Game.GetGameAudioManager(), m_UIManager);
        m_Bar.SetPosition(0, -0.1);
        m_Bar.SnapToValue(1.0);

        m_UIManager.AddUIElement(m_Bar);
        m_UIManager.AddUIElement(m_Bar.GetLabel());

        m_UIManager.AddUIElement(m_Label);
    }

    @Override
    public void Remove()
    {
        super.Remove();

        m_UIManager.RemoveUIElement(m_Label);
        m_Label = null;

        m_UIManager.RemoveUIElement(m_Bar);
        m_Label = null;
    }

    @Override
    public void Update(double deltaTime)
    {
        super.Update(deltaTime);

        if(m_Game.GetVehicleManager().GetPlayer() != null)
            Log.e("Reboot", "WTF!");

        m_Bar.SetValue(m_Game.GetSecondWindHandler().GetInverseProgress());
    }
}
