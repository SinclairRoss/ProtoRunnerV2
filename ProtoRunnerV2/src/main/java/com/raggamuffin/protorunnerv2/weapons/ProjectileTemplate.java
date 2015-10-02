package com.raggamuffin.protorunnerv2.weapons;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

public class ProjectileTemplate
{
    private Weapon m_Origin;

    private ModelType m_Model;
    private AffiliationKey m_Affiliation;

    private double m_MuzzleVelocity;
    private double m_BaseDamage;
    private double m_LifeSpan;
    private double m_BoundingRadius;

    private ProjectileBehaviourType m_Behaviour;
    private SpecialProjectileBehaviourType[] m_SpecialBehaviours;

    private GameLogic m_Game;
    private ParticleManager m_ParticleManager;
    private BulletManager m_BulletManager;

    public ProjectileTemplate(Weapon origin, ModelType model, AffiliationKey affiliation,
                              double muzzleVelocity, double baseDamage, double lifeSpan, double boundingRadius,
                              ProjectileBehaviourType behaviour, GameLogic game, SpecialProjectileBehaviourType... specialBehaviours)
    {
        m_Origin = origin;

        m_Model = model;
        m_Affiliation = affiliation;

        m_MuzzleVelocity = muzzleVelocity;
        m_BaseDamage = baseDamage;
        m_LifeSpan = lifeSpan;
        m_BoundingRadius = boundingRadius;

        m_Behaviour = behaviour;
        m_SpecialBehaviours = specialBehaviours;

        m_Game = game;
        m_ParticleManager = game.GetParticleManager();
        m_BulletManager = game.GetBulletManager();
    }

    public Weapon GetOrigin()
    {
        return m_Origin;
    }

    public ModelType GetModel()
    {
        return m_Model;
    }

    public AffiliationKey GetAffiliation()
    {
        return m_Affiliation;
    }

    public double GetMuzzleVelocity()
    {
        return m_MuzzleVelocity;
    }

    public double GetBaseDamage()
    {
        return m_BaseDamage;
    }

    public double GetLifeSpan()
    {
        return m_LifeSpan;
    }

    public double GetBoundingRadius()
    {
        return m_BoundingRadius;
    }
	
	public ProjectileBehaviour GetBehaviour(Projectile proj)
	{
		switch(m_Behaviour)
		{
			case Standard:
				return new ProjectileBehaviour_Standard(proj);
			case Missile:
				return new ProjectileBehaviour_Missile(proj, m_Game, m_Origin.GetMuzzle(), m_Origin.GetMuzzleIndex());
            case Mine:
                return new ProjectileBehaviour_Mine(proj);
            case ParticleLaser:
                return new ProjectileBehaviour_Laser(proj, m_Origin.GetMuzzle(), m_Game);
            default:
				return null;
		}
	}
	
	public ArrayList<SpecialProjectileBehaviour> GetSpecialBehaviour(Projectile proj)
	{
		ArrayList<SpecialProjectileBehaviour> list = new ArrayList<>();
		
		for(SpecialProjectileBehaviourType behaviour : m_SpecialBehaviours)
		{
			switch(behaviour)
			{
				case AddAttractGraviton:
					list.add(new SpecialProjectileBehaviour_AddAttractGraviton(proj, m_ParticleManager));
				case Explode:
					list.add(new SpecialProjectileBehaviour_Explode(proj, m_ParticleManager, m_BulletManager));
				default:
					break;
			}
		}
		
		return list;
	}
}
