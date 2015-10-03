package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class ProjectileBehaviour_Standard extends ProjectileBehaviour
{
    private double m_FadeIn;
    private double m_FadeOut;

	public ProjectileBehaviour_Standard(Projectile anchor) 
	{
        super(anchor);

        m_FadeIn = 0.02;
        m_FadeOut = 0.9;

        m_Anchor.GetBaseColour().Alpha = 0.0;
        m_Anchor.GetAltColour().Alpha  = 0.0;
    }

	@Override
	public void Update(double deltaTime) 
	{
        double normLifeSpan = 1 - MathsHelper.Normalise(m_Anchor.GetLifeSpan(), 0, m_Anchor.GetMaxLifeSpan());

        double alpha = normLifeSpan <= m_FadeIn ? MathsHelper.Normalise(normLifeSpan, 0, m_FadeIn) :
                1.0 - MathsHelper.Normalise(normLifeSpan, m_FadeOut, 1.0);

        m_Anchor.GetBaseColour().Alpha = alpha;
        m_Anchor.GetAltColour().Alpha  = alpha;
	}

    @Override
    public boolean CollidesWith(Vehicle other)
    {
        return CollisionDetection.CheckCollisions(m_Anchor, other);
    }

    @Override
    public void CollisionResponce(Vehicle other)
    {
        m_Anchor.ForceInvalidation();
    }

    @Override
    public double CalculateDamageOutput(double baseDamage, double deltaTime)
    {
        return baseDamage;
    }

    @Override
	public void CleanUp() 
	{

	}
}
