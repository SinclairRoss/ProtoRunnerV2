package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ProjectileBehaviour_Detonate extends ProjectileBehaviour
{
	private final double DETONATION_EPSILON_SQR = 1.0;
	
	private Vector3 m_Destination;
	private Vector3 m_ToDestination;
	
	public ProjectileBehaviour_Detonate(Projectile anchor, Vector3 target) 
	{
		super(anchor);
		
		m_Destination = target;
		m_ToDestination = new Vector3();
	}

	@Override
	public void Update(double deltaTime) 
	{
		m_ToDestination.SetVectorDifference(m_Anchor.GetPosition(), m_Destination);
		
		if(m_ToDestination.GetLengthSqr() <= DETONATION_EPSILON_SQR)
			m_Anchor.ForceInvalidation();
	}

	@Override
	public void CleanUp() 
	{

	}

    @Override
    public boolean UseSimpleCollisionDetection()
    {
        return false;
    }
}