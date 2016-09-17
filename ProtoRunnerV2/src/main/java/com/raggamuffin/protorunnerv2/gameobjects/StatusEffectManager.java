package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   15/09/2016

public class StatusEffectManager
{
    private int m_EffectFlags;

    public StatusEffectManager()
    {
        m_EffectFlags = 0;
    }

    public void ApplyStatusEffect(StatusEffect status)
    {
        m_EffectFlags |= GetStatusEffectFlag(status);
    }

    public void RemoveStatusEffect(StatusEffect status)
    {
        if(HasStatusEffect(status))
        {
            m_EffectFlags ^= GetStatusEffectFlag(status);
        }
    }

    public boolean HasStatusEffect(StatusEffect effect)
    {
        return (m_EffectFlags & GetStatusEffectFlag(effect)) > 0;
    }

    private int GetStatusEffectFlag(StatusEffect effect)
    {
        return 1 << effect.ordinal();
    }

}
