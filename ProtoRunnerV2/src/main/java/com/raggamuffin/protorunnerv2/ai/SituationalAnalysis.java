package com.raggamuffin.protorunnerv2.ai;

import android.util.Log;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.gameobjects.Wingman;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

import java.util.ArrayList;

public class SituationalAnalysis
{
    private double m_Bravery;

    private Vehicle m_Anchor;

    private double m_SelfWorth;
    private ArrayList<BraveryFactor> m_BraveryFactors;

    BraveryFactor a, b, c;

    public SituationalAnalysis(AIController controller, AIPersonalityAttributes attributes)
    {
        m_Anchor = controller.GetAnchor();

        m_SelfWorth = attributes.GetSelfWorth();

        m_BraveryFactors = new ArrayList<>();
        m_BraveryFactors.add(new BraveryFactor_DamageTaken(controller.GetAnchor().GetInternalPubSubHub(), attributes));
        m_BraveryFactors.add(new BraveryFactor_Enemies(controller, attributes));
        m_BraveryFactors.add(new BraveryFactor_Friendlies(controller, attributes));

        a = new BraveryFactor_DamageTaken(controller.GetAnchor().GetInternalPubSubHub(), attributes);
        b = new BraveryFactor_Enemies(controller, attributes);
        c = new BraveryFactor_Friendlies(controller, attributes);
    }

    public void Update(double deltaTime)
    {
        m_Bravery = m_SelfWorth;

       // for(BraveryFactor factor : m_BraveryFactors)
       // {
       //     m_Bravery += factor.GetBraveryModifier(deltaTime);
       // }

       // m_Bravery = MathsHelper.Clamp(m_Bravery, 0, 1);

        double valA = a.GetBraveryModifier(deltaTime);
        double valB = b.GetBraveryModifier(deltaTime);
        double valC = c.GetBraveryModifier(deltaTime);

        m_Bravery +=  valA + valB + valC;

//        if(m_Anchor instanceof Wingman)
//        {
//            Log.e("Wingman", "Bravery:     " + m_Bravery);
//            Log.e("Wingman", "SelfWorth:   " + m_SelfWorth);
//            Log.e("Wingman", "DamageTaken: " + valA);
//            Log.e("Wingman", "Enemies:     " + valB);
//            Log.e("Wingman", "Friendlies:  " + valC);
//        }
    }

    public double GetBravery()
    {
        return m_Bravery;
    }
}
