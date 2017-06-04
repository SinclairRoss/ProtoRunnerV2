package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Projectile;

import java.util.ArrayList;

public class Sensor_IncomingDanger extends Sensor
{
    public enum DangerState
    {
        NoDanger,
        DangerLeft,
        DangerRight
    }

	private ArrayList<Projectile> m_Bullets;
	private ArrayList<Projectile> m_IncomingProjectiles;
	private Vector3 m_ReltivePosition;
	private Vector3 m_RelativeVelocity;
    private DangerState m_DangerState;
	
	public Sensor_IncomingDanger(AIController controller, BulletManager bManager) 
	{
		super(controller.GetAnchor(), 10);
		
		m_Bullets = bManager.GetActiveBullets();
		m_IncomingProjectiles = new ArrayList<>();
		m_ReltivePosition = new Vector3();
		m_RelativeVelocity = new Vector3();

        m_DangerState = DangerState.NoDanger;
	}

	@Override
	public void Update() 
	{
        Projectile closestProjectile = null;
        double closetProjectileDistanceSqr = Double.MAX_VALUE;

		m_IncomingProjectiles.clear();
        m_DangerState = DangerState.NoDanger;

        int numProjectiles = m_Bullets.size();
        for(int i = 0; i < numProjectiles; ++i)
		{
            Projectile proj = m_Bullets.get(i);

			if(proj.GetAffiliation() != m_Anchor.GetAffiliation())
            {
                m_ReltivePosition.SetVectorAsDifference(m_Anchor.GetPosition(), proj.GetPosition());
                double toProjectileLengthSqr = m_ReltivePosition.GetLengthSqr();

                if (toProjectileLengthSqr < m_SensorRadius * m_SensorRadius)
                {
                    m_ReltivePosition.Normalise();
                    m_RelativeVelocity.SetVectorAsDifference(m_Anchor.GetVelocity(), proj.GetVelocity());
                    m_RelativeVelocity.Normalise();

                    double dotProduct = Vector3.DotProduct(m_ReltivePosition, m_RelativeVelocity);

                    if (dotProduct < 0)
                    {
                        if (toProjectileLengthSqr < closetProjectileDistanceSqr)
                        {
                            closestProjectile = proj;
                            closetProjectileDistanceSqr = toProjectileLengthSqr;
                            m_IncomingProjectiles.add(proj);
                        }
                    }
                }
            }
		}

        if(closestProjectile != null)
        {
            if(Vector3.Determinant(m_Anchor.GetForward(), m_ReltivePosition) > 0)
            {
                m_DangerState = DangerState.DangerRight;
            }
            else
            {
                m_DangerState = DangerState.DangerLeft;
            }
        }
	}

	public DangerState GetDangerState()
    {
        return m_DangerState;
    }
}
