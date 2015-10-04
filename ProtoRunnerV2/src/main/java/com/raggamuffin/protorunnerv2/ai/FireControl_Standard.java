package com.raggamuffin.protorunnerv2.ai;


import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class FireControl_Standard extends FireControl
{
    private Vector3 m_ToTarget;

    private double m_FireArc;
	private double m_Range;
	
	public FireControl_Standard(AIController controller)
	{
        super(controller);

        m_ToTarget = new Vector3();

        m_Range = 100.0f;
        m_FireArc = Math.toRadians(1);
	}

    @Override
    public void Update(double deltaTime)
    {
        m_Weapon.CeaseFire();

        Vehicle target = m_SituationalAwareness.GetTargetSensor().GetTarget();

        if(target == null)
            return;

        if(!target.CanBeTargeted())
            return;

        // Is target within range.
        m_ToTarget.SetVectorDifference(m_Anchor.GetPosition(), target.GetPosition());

        if(m_ToTarget.GetLengthSqr() > m_Range * m_Range)
            return;

        // Is target within firing arc.
        m_ToTarget.Normalise();
        double radiansBetween = Vector3.RadiansBetween(m_ToTarget, m_Anchor.GetForward());

        if(radiansBetween > m_FireArc)
            return;

        m_Weapon.OpenFire();
    }
}
