package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public abstract class Projectile extends GameObject
{
    protected double m_BaseDamage;
    private AffiliationKey m_Affiliation;

	public Projectile(Vector3 position, Vector3 initialVelocity, Vector3 forward, Colour colour, double baseDamage, AffiliationKey affiliation, ModelType model)
	{
		super(model, 0.1);

		SetPosition(position);
		SetVelocity(initialVelocity);
		SetForward(forward);

        SetDragCoefficient(0);

        SetColour(colour);

        m_BaseDamage = baseDamage;
        m_Affiliation = affiliation;
	}

    public abstract CollisionReport CheckForCollision(GameObject object);

	public abstract void CollisionResponse(CollisionReport report);

    public double GetDamageOutput() { return m_BaseDamage; }
    public AffiliationKey GetAffiliation() { return m_Affiliation; }
}