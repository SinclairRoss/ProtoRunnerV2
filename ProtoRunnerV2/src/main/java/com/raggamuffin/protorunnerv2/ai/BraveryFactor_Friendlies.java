package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class BraveryFactor_Friendlies extends BraveryFactor
{
    private double m_ConfidenceInTeam;
    private SituationalAwareness m_Awareness;

    public BraveryFactor_Friendlies(AIController controller, AIPersonalityAttributes attributes)
    {
        m_ConfidenceInTeam = attributes.GetConfidenceInTeam();
        m_Awareness = controller.GetSituationalAwareness();
    }

    @Override
    public double GetBraveryModifier(double deltaTime)
    {
        double bravery = m_ConfidenceInTeam * m_Awareness.GetSurroundingAwarenessSensor().GetFriendliesInNeighbourhood().size();
        return MathsHelper.Clamp(bravery, -1.0, 1.0);
    }
}
