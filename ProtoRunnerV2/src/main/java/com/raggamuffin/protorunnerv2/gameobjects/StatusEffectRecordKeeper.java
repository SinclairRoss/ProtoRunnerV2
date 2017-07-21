package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   15/09/2016

import com.raggamuffin.protorunnerv2.ObjectEffect.ObjectEffectController;
import com.raggamuffin.protorunnerv2.ObjectEffect.ObjectEffectType;

public class StatusEffectRecordKeeper
{
    private int m_EffectFlags;

    private ObjectEffectController m_EffectController;
    private Vehicle m_Anchor;

    public StatusEffectRecordKeeper(ObjectEffectController effectController, Vehicle anchor)
    {
        m_EffectFlags = 0;

        m_EffectController = effectController;
        m_Anchor = anchor;
    }

    public void ApplyStatusEffect(StatusEffect status)
    {
        m_EffectFlags |= GetStatusEffectFlag(status);

        switch (status)
        {
            case Shielded:
            {
                m_EffectController.CreateEffect(ObjectEffectType.Shields, m_Anchor);
                break;
            }
        }
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
