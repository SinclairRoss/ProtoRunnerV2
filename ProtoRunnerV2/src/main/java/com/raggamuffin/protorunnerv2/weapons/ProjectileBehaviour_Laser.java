package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_HyperLight;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Ray;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ProjectileBehaviour_Laser extends ProjectileBehaviour
{
    private Weapon m_FiringWeapon;

    private Vector3 m_EndPoint;
    private double m_Scale;
    private final double m_MaxScale;
    private final double m_GrowthRate;

    private ParticleEmitter_Ray m_RayEmitter;
    private ParticleEmitter_HyperLight m_HyperLightEmitter;
    private boolean m_TouchesSurface;

    public ProjectileBehaviour_Laser(Projectile anchor, Vector3 muzzlePos, GameLogic game)
    {
        super(anchor);

        m_FiringWeapon = m_Anchor.GetFiringWeapon();

        m_EndPoint = new Vector3();
        m_Scale = 0.0;
        m_MaxScale = 50;
        m_GrowthRate = 100;

        m_Anchor.SetScale(0.0);

        m_RayEmitter = new ParticleEmitter_Ray(game, m_Anchor.GetBaseColour(), m_Anchor.GetBaseColour(), 1000, 0.3);
        m_RayEmitter.SetParticleSize(15);
        m_TouchesSurface = false;

        m_HyperLightEmitter = new ParticleEmitter_HyperLight(game, m_Anchor.GetBaseColour(), m_Anchor.GetAltColour(), 4000, 3.0);
        m_HyperLightEmitter.SetParticleSize(30.0);
        m_HyperLightEmitter.On();
    }

    @Override
    public void Update(double deltaTime)
    {
        LockProjectile();
        ScaleLaser(deltaTime);

        m_RayEmitter.SetPosition(m_Anchor.GetPosition());
        m_RayEmitter.SetForward(m_Anchor.GetForward());
        m_RayEmitter.SetRange(m_Scale);
        m_RayEmitter.Update(deltaTime);

        BloomEndPointParticles(deltaTime);

        m_TouchesSurface = false;
    }

    private void BloomEndPointParticles(double deltaTime)
    {
        m_EndPoint.SetVector(m_Anchor.GetForward());
        m_EndPoint.Scale(m_Scale);
        m_EndPoint.Add(m_Anchor.GetPosition());

        m_HyperLightEmitter.SetPosition(m_EndPoint);
        m_HyperLightEmitter.SetForward(m_Anchor.GetForward());
        m_HyperLightEmitter.Update(deltaTime);
    }

    @Override
    public boolean CollidesWith(Vehicle other)
    {
        return CollisionDetection.RayTrace(m_Anchor.GetPosition(), m_Anchor.GetForward(), m_Scale, other);
    }

    @Override
    public void CollisionResponce(Vehicle other)
    {
        m_TouchesSurface = true;

        Vector3 otherPos = other.GetPosition();
        Vector3 pos = m_Anchor.GetPosition();

        double i = otherPos.I - pos.I;
        double j = otherPos.J - pos.J;
        double k = otherPos.K - pos.K;

        m_Scale = Math.sqrt(i*i+j*j+k*k);
    }

    @Override
    public double CalculateDamageOutput(double deltaTime)
    {
        return 0;
    }

    private void LockProjectile()
    {
        m_Anchor.SetForward(m_FiringWeapon.GetForward());
        m_Anchor.SetPosition(m_FiringWeapon.GetPosition());
    }

    private void ScaleLaser(double deltaTime)
    {
        m_Scale = MathsHelper.Clamp(m_Scale + (deltaTime * m_GrowthRate), 0, m_MaxScale);

        if(m_Scale < m_MaxScale)
            m_HyperLightEmitter.On();
        else
            m_HyperLightEmitter.Off();

        double y = m_Anchor.GetPosition().J + 1;
        double j = m_FiringWeapon.GetForward().J * m_Scale;

        if (y + j < 0)
        {
            m_TouchesSurface = true;
            m_Scale *= Math.abs(y / j);
        }

        m_Anchor.SetScale(0.25, 0.25, m_Scale);
    }

    @Override
    public void CleanUp()
    {
        m_Anchor.SetScale(1.0);
    }
}
