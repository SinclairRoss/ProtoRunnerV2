package com.raggamuffin.protorunnerv2.ObjectEffect;

// Author: Sinclair Ross
// Date:   22/05/2017

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

import java.util.ArrayList;

public class ObjectEffectController
{
    private final GameLogic m_GameLogic;

    private final ArrayList<ArrayList<ObjectEffect>> m_ActiveEffects;
    private final ArrayList<ArrayList<ObjectEffect>> m_InvalidEffects;

    private final ArrayList<ObjectEffect_HealthBar> m_ActiveHealthBars;
    private final ArrayList<ObjectEffect_HealthBar> m_InvalidHealthBars;

    public ObjectEffectController(GameLogic game)
    {
        m_GameLogic = game;

        int numEffectTypes = ObjectEffectType.values().length;

        m_ActiveEffects = new ArrayList<>(numEffectTypes);
        m_InvalidEffects = new ArrayList<>(numEffectTypes);

        for(int i = 0; i < numEffectTypes; ++i)
        {
            m_ActiveEffects.add(new ArrayList<ObjectEffect>());
            m_InvalidEffects.add(new ArrayList<ObjectEffect>());
        }

        m_ActiveHealthBars = new ArrayList<>();
        m_InvalidHealthBars = new ArrayList<>();
    }

    public void Update(double deltaTime)
    {
        int numEffectTypes = m_ActiveEffects.size();
        for(int i = 0; i < numEffectTypes; ++i)
        {
            ArrayList<ObjectEffect> activeList = m_ActiveEffects.get(i);
            ArrayList<ObjectEffect> invalidList = m_InvalidEffects.get(i);
            UpdateListSet(deltaTime, activeList, invalidList);
        }

        for (int i = 0; i < m_ActiveHealthBars.size();)
        {
            ObjectEffect_HealthBar effect = m_ActiveHealthBars.get(i);

            effect.Update(deltaTime);

            if(!effect.IsValid())
            {
                m_ActiveHealthBars.remove(effect);
                m_InvalidHealthBars.add(effect);
            }
            else
            {
                ++i;
            }
        }
    }

    private void UpdateListSet(double deltaTime, ArrayList<ObjectEffect> activeList, ArrayList<ObjectEffect> invalidList)
    {
        for (int i = 0; i < activeList.size();)
        {
            ObjectEffect effect = activeList.get(i);

            effect.Update(deltaTime);

            if(!effect.IsValid())
            {
                activeList.remove(effect);
                invalidList.add(effect);
            }
            else
            {
                ++i;
            }
        }
    }


    public ObjectEffect CreateEffect(ObjectEffectType type, Vehicle anchor)
    {
        ObjectEffect effect;

        ArrayList<ObjectEffect> invalidEffects = m_InvalidEffects.get(type.ordinal());
        if(!invalidEffects.isEmpty())
        {
            effect = invalidEffects.remove(invalidEffects.size() - 1);
        }
        else
        {
            effect = CreateEffectOfType(type);
        }

        effect.Initialise(anchor);

        ArrayList<ObjectEffect> activeEffects = m_ActiveEffects.get(type.ordinal());
        activeEffects.add(effect);

        return effect;
    }

    private ObjectEffect CreateEffectOfType(ObjectEffectType type)
    {
        ObjectEffect effect = null;

        switch (type)
        {
            case DamageMarker:
            {
                effect = new ObjectEffect_DamageMarker(m_GameLogic); break;
            }
            case SpawnPillar:
            {
                effect = new ObjectEffect_SpawnPillar();
                break;
            }
            case Shields:
            {
                effect = new ObjectEffect_Shields(m_GameLogic);
                break;
            }
        }

        return effect;
    }

    public ObjectEffect_HealthBar CreateHealthBar(Vehicle anchor)
    {
        ObjectEffect_HealthBar healthBar;

        if(!m_InvalidHealthBars.isEmpty())
        {
            healthBar = m_InvalidHealthBars.remove(m_InvalidHealthBars.size() - 1);
        }
        else
        {
            healthBar = new ObjectEffect_HealthBar(m_GameLogic);
        }

        healthBar.Initialise(anchor);
        m_ActiveHealthBars.add(healthBar);

        return healthBar;
    }

    public ArrayList<ObjectEffect> GetEffectsForType(ObjectEffectType type) { return m_ActiveEffects.get(type.ordinal()); }
    public ArrayList<ObjectEffect_HealthBar> GetHealthBars() { return m_ActiveHealthBars; }
}
