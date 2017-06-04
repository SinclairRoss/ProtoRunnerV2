package com.raggamuffin.protorunnerv2.ObjectEffect;

// Author: Sinclair Ross
// Date:   22/05/2017

import android.util.Log;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;

import java.util.ArrayList;

public class ObjectEffectController
{
    private final GameLogic m_GameLogic;

    private ArrayList<ArrayList<ObjectEffect>> m_ActiveEffects;
    private ArrayList<ArrayList<ObjectEffect>> m_InvalidEffects;

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
    }

    public void Update(double deltaTime)
    {
        int numEffectTypes = m_ActiveEffects.size();
        for(int i = 0; i < numEffectTypes; ++i)
        {
            ArrayList<ObjectEffect> effects = m_ActiveEffects.get(i);

            int numEffects = effects.size();
            for (int j = 0; j < numEffects; ++j)
            {
                ObjectEffect effect = effects.get(j);

                if(effect.IsValid())
                {
                    effect.Update(deltaTime);
                }
                else
                {
                    int effectTypeIndex = effect.GetEffectType().ordinal();

                    ArrayList<ObjectEffect> activeEffects = m_ActiveEffects.get(effectTypeIndex);
                    activeEffects.remove(effect);

                    ArrayList<ObjectEffect> invalidEffects = m_InvalidEffects.get(effectTypeIndex);
                    invalidEffects.add(effect);

                    m_GameLogic.RemoveObjectFromRenderer(effect);

                    --j;
                    --numEffects;
                }
            }

            Log.e("Num effects: " + numEffects, "");
        }
    }

    public ObjectEffect CreateEffect(ObjectEffectType type, GameObject anchor)
    {
        ObjectEffect effect;

        ArrayList<ObjectEffect> invalidEffects = m_InvalidEffects.get(type.ordinal());
        if(invalidEffects.size() > 0)
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

        m_GameLogic.AddObjectToRenderer(effect);

        return effect;
    }

    private ObjectEffect CreateEffectOfType(ObjectEffectType type)
    {
        ObjectEffect effect = null;
        switch (type)
        {
            case DamageMarker:
            {
                effect = new ObjectEffect_DamageMarker(m_GameLogic);
                break;
            }
            case SpawnPillar:
            {
                effect = new ObjectEffect_SpawnPillar();
                break;
            }
        }

        return effect;
    }
}
