package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.weapons.Weapon;

public abstract class FireControl
{
    protected AIController m_Controller;
    protected SituationalAwareness m_SituationalAwareness;
    protected Weapon m_Weapon;
    protected Vehicle m_Anchor;

    public FireControl(AIController controller)
    {
        m_Controller = controller;
        m_SituationalAwareness = m_Controller.GetSituationalAwareness();
        m_Weapon = m_Controller.GetAnchor().GetPrimaryWeapon();
        m_Anchor = m_Controller.GetAnchor();
    }

    public abstract void Update(double deltaTime);
}
