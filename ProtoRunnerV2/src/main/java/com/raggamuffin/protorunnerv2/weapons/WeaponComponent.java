// WeaponComponent
// A component which can be attached to a weapon.

package com.raggamuffin.protorunnerv2.weapons;

public abstract class WeaponComponent
{
    protected Weapon m_Anchor;
    protected boolean m_Active;

    public WeaponComponent(Weapon anchor)
    {
        m_Anchor = anchor;
        m_Active = false;
    }

    public void Activate()
    {
        if(!m_Active)
        {
            m_Active = true;
            OnActivation();
        }
    }

    public void Deactivate()
    {
        if(m_Active)
        {
            m_Active = false;
            OnDeactivation();
        }
    }

    public abstract void Destroy();
    public abstract void Update(double deltaTime);
    public abstract void OnActivation();
    public abstract void OnDeactivation();
}
