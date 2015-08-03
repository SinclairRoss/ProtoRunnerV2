package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class BraveryFactor_Enemies extends BraveryFactor
{
    private double m_FearOfTheEnemy;
    private SituationalAwareness m_Awareness;

    public BraveryFactor_Enemies(AIController controller, AIPersonalityAttributes attributes)
    {
        m_FearOfTheEnemy = attributes.GetFearOfEnemy();
        m_Awareness = controller.GetSituationalAwareness();
    }

    @Override
    public double GetBraveryModifier(double deltaTime)
    {
        double bravery = m_FearOfTheEnemy * m_Awareness.GetSurroundingAwarenessSensor().GetFriendliesInNeighbourhood().size();
        return MathsHelper.Clamp(bravery, -1.0, 1.0);
    }
}
