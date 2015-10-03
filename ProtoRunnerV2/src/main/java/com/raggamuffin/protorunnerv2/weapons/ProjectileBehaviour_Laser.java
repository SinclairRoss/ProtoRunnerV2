package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_HyperLight;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Ray;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Spiral;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ProjectileBehaviour_Laser extends ProjectileBehaviour
{
    private enum LaserState
    {
        Active,
        Deactivating,
        Deactivated
    }

    private LaserState m_State;

    private Weapon m_FiringWeapon;

    private Vector3 m_EndPoint;
    private double m_Scale;
    private final double m_MaxScale;
    private final double m_GrowthRate;
    private Timer m_LaserFadeTimer;
    private double m_MaxLaserWidth;
    private double m_LaserWidth;

    private ParticleEmitter_Ray m_RayEmitter;
    private ParticleEmitter_Spiral m_SpiralEmitter;

    public ProjectileBehaviour_Laser(Projectile anchor, Vector3 muzzlePos, GameLogic game)
    {
        super(anchor);

        m_State = LaserState.Active;

        m_FiringWeapon = m_Anchor.GetFiringWeapon();

        m_EndPoint = new Vector3();
        m_Scale = 0.0;
        m_MaxScale = 50;
        m_GrowthRate = 100;
        m_LaserFadeTimer = new Timer(m_Anchor.GetMaxLifeSpan());
        m_MaxLaserWidth = 0.25;
        m_LaserWidth = m_MaxLaserWidth;

        m_Anchor.SetScale(0.0);

        m_RayEmitter = new ParticleEmitter_Ray(game, m_Anchor.GetBaseColour(), m_Anchor.GetBaseColour(), 1000, 0.3);
        m_RayEmitter.SetParticleSize(15);

        m_SpiralEmitter = new ParticleEmitter_Spiral(game, m_Anchor.GetBaseColour(), m_Anchor.GetAltColour(), 6000, 3.0);
        m_SpiralEmitter.SetParticleSize(40.0);
        m_SpiralEmitter.On();
    }

    @Override
    public void Update(double deltaTime)
    {
        switch(m_State)
        {
            case Active:

                m_Anchor.MaxOutLifeSpan();

                if(!m_Anchor.GetFiringWeapon().IsTriggerPulled())
                    m_State = LaserState.Deactivating;

                break;
            case Deactivating:
                m_LaserFadeTimer.Update(deltaTime);
                m_LaserWidth = MathsHelper.Lerp(m_LaserFadeTimer.GetProgress(), m_MaxLaserWidth, 0);
                m_Anchor.GetBaseColour().Alpha = m_LaserFadeTimer.GetInverseProgress();
                m_Anchor.GetAltColour().Alpha = m_LaserFadeTimer.GetInverseProgress();
                break;
        }

        LockProjectile();
        ScaleLaser(deltaTime);

        m_RayEmitter.SetPosition(m_Anchor.GetPosition());
        m_RayEmitter.SetForward(m_Anchor.GetForward());
        m_RayEmitter.SetRange(m_Scale);
        m_RayEmitter.Update(deltaTime);

        BloomEndPointParticles(deltaTime);
    }

    private void BloomEndPointParticles(double deltaTime)
    {
        m_EndPoint.SetVector(m_Anchor.GetForward());
        m_EndPoint.Scale(m_Scale);
        m_EndPoint.Add(m_Anchor.GetPosition());

        m_SpiralEmitter.SetPosition(m_EndPoint);
        m_SpiralEmitter.SetForward(m_Anchor.GetForward());
        m_SpiralEmitter.Update(deltaTime);
    }

    @Override
    public void CollisionResponce(Vehicle other)
    {
        Vector3 otherPos = other.GetPosition();
        Vector3 pos = m_Anchor.GetPosition();

        double i = otherPos.I - pos.I;
        double j = otherPos.J - pos.J;
        double k = otherPos.K - pos.K;

        m_Scale = Math.sqrt(i*i+j*j+k*k);
    }

    private void LockProjectile()
    {
        m_Anchor.SetForward(m_FiringWeapon.GetForward());
        m_Anchor.SetPosition(m_FiringWeapon.GetPosition());
        m_Anchor.GetVelocity().SetVector(0);
    }

    private void ScaleLaser(double deltaTime)
    {
        m_Scale = MathsHelper.Clamp(m_Scale + (deltaTime * m_GrowthRate), 0, m_MaxScale);

        if(m_Scale < m_MaxScale)
            m_SpiralEmitter.On();
        else
            m_SpiralEmitter.Off();

        double y = m_Anchor.GetPosition().J + 1;
        double j = m_FiringWeapon.GetForward().J * m_Scale;

        if (y + j < 0)
        {
            m_Scale *= Math.abs(y / j);
        }

        m_Anchor.SetScale(m_LaserWidth, m_LaserWidth, m_Scale);
    }

    @Override
    public boolean CollidesWith(Vehicle other)
    {
        return CollisionDetection.RayTrace(m_Anchor.GetPosition(), m_Anchor.GetForward(), m_Scale, other);
    }

    @Override
    public double CalculateDamageOutput(double baseDamage, double deltaTime)
    {
        return baseDamage * deltaTime;
    }

    @Override
    public void CleanUp()
    {
        m_Anchor.SetScale(1.0);
    }
}
