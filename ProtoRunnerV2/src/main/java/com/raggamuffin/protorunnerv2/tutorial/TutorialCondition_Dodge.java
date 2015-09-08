package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Runner;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.utils.Timer;

public class TutorialCondition_Dodge extends TutorialCondition
{
    private VehicleManager m_VehicleManager;
    private Timer m_DodgeTimer;

    public TutorialCondition_Dodge(GameLogic game, String message, double time, TutorialEffect... effects)
    {
        super(game, message, OptionalElement.ProgressBar, effects);

        m_VehicleManager = m_Game.GetVehicleManager();
        m_DodgeTimer = new Timer(time);
    }

    @Override
    public void Update(double deltaTime)
    {
        Runner player = m_VehicleManager.GetPlayer();

        if(player == null)
            return;

        if(!player.IsDodging())
            return;

        m_DodgeTimer.Update(deltaTime);
    }

    @Override
    public boolean ConditionComplete()
    {
        return m_DodgeTimer.TimedOut();
    }

    @Override
    public double GetProgress()
    {
        return m_DodgeTimer.GetProgress();
    }
}
