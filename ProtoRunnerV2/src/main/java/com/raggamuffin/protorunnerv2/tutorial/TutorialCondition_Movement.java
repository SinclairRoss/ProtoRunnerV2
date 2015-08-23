package com.raggamuffin.protorunnerv2.tutorial;

import com.raggamuffin.protorunnerv2.ai.VehicleInfo;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.vehicles.Runner;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;

public class TutorialCondition_Movement extends TutorialCondition
{
    private VehicleManager m_VehicleManager;
    private VehicleInfo.MovementStates m_MovementState;
    private boolean m_ConditionComplete;

    public TutorialCondition_Movement(GameLogic game, String message, VehicleInfo.MovementStates state, TutorialEffect... effects)
    {
        super(game, message, OptionalElement.None, effects);

        m_VehicleManager = m_Game.GetVehicleManager();
        m_MovementState = state;
        m_ConditionComplete = false;
    }

    @Override
    public void Update(double deltaTime)
    {
        Runner player = m_VehicleManager.GetPlayer();

        if(player == null)
            return;

        if(player.GetVehicleInfo().GetMovmentState() != m_MovementState)
            return;

        m_ConditionComplete = true;
    }

    @Override
    public boolean ConditionComplete()
    {
        return m_ConditionComplete;
    }

    @Override
    public double GetProgress()
    {
        return m_ConditionComplete ? 1 : 0;
    }

    @Override
    public void Reset()
    {
        super.Reset();
        m_ConditionComplete = false;
    }
}
