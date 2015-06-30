package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

public class TutorialCondition_Reboot extends TutorialCondition
{
    public TutorialCondition_Reboot(GameLogic game, String message, TutorialEffect... effects)
    {
        super(game, message, OptionalElement.None, effects);
    }

    @Override
    public boolean ConditionComplete()
    {
        return false;
    }

    @Override
    public double GetProgress()
    {
        return 0;
    }

    @Override
    public void Update(double deltaTime)
    {

    }

    @Override
    public void Initialise()
    {
        super.Initialise();

        m_Game.GetVehicleManager().GetPlayer().SetHullPoints(0);
    }
}
