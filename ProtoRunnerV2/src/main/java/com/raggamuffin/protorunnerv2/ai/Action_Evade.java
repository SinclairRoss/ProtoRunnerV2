package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.weapons.Projectile;

import java.util.ArrayList;

public class Action_Evade extends Action
{
    public Action_Evade(AIController controller)
    {
        super(controller);
    }

    @Override
    public void Update(double deltaTime)
    {
        if(MathsHelper.RandomDouble(0, 1) >= 0.5)
            m_Controller.GetAnchor().DodgeLeft();
        else
            m_Controller.GetAnchor().DodgeRight();
    }

    @Override
    public double CalculateUtility()
    {
        ArrayList<Projectile> danger = m_Controller.GetSituationalAwareness().GetDangerSensor().GetIncomingProjectiles();

        return danger.size() > 0 ? 1 : 0;
    }
}
