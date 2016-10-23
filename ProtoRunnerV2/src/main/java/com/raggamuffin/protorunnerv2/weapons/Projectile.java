package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.RenderObjectType;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class Projectile extends GameObject
{
    protected double m_BaseDamage;

	public Projectile(GameLogic game, Vector3 position, Vector3 initialVelocity, Vector3 forward, Colour colour, double baseDamage, AffiliationKey affiliation, ModelType model)
	{
		super(game, model);

		m_Position.SetVector(position);
		m_Velocity.SetVector(initialVelocity);
		m_Forward.SetVector(forward);
        UpdateVectorsWithForward(m_Forward);

        SetBaseColour(colour);
        m_AltColour.SetAsInverse(colour);

        SetAffiliation(affiliation);

        m_BaseDamage = baseDamage;

		SetDragCoefficient(1.0);
	}

	@Override
	public boolean IsValid() 
	{
		return !IsForciblyInvalidated();

	}

    public abstract CollisionReport CheckForCollision(GameObject object);

	public abstract void CollisionResponse(CollisionReport report);

    public double GetDamageOutput()
    {
        return m_BaseDamage;
    }
}