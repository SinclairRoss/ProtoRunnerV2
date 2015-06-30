package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;

public class TutorialCondition_Message extends TutorialCondition
{
    public TutorialCondition_Message(GameLogic game, String message, TutorialEffect... effects)
    {
        super(game, message, OptionalElement.NextButton, effects);
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
}
