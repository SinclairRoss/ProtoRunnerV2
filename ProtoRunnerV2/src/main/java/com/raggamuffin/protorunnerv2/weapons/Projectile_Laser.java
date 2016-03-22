package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Ray;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Projectile_Laser extends Projectile
{
    private enum LaserState
    {
        Active,
        Deactivating,
        Deactivated
    }

    private LaserState m_State;

    private final double m_GrowthRate;
    private Timer m_LaserFadeTimer;

    private double m_LaserLength;
    private final double m_MaxLength;
    private double m_MaxWidth;
    private double m_LaserWidth;

    private WeaponBarrel m_FiringBarrel;

    private double m_DamageOutputPerFrame;

    private ParticleEmitter_Ray m_RayEmitter;

    public Projectile_Laser(Weapon origin, GameLogic game)
    {
        super(origin);

        m_FiringBarrel = origin.GetActiveWeaponBarrel();

        m_Model = ModelType.ParticleLaser;

        m_State = LaserState.Active;

        m_GrowthRate = 100;
        m_LaserFadeTimer = new Timer(0.5);

        m_LaserLength = 0.0;
        m_MaxLength = 200;
        m_MaxWidth = 0.25;
        m_LaserWidth =  0.25;

        SetScale(0.0);

        m_RayEmitter = new ParticleEmitter_Ray(game, GetBaseColour(), GetBaseColour(), 1000, 0.3);
        m_RayEmitter.SetParticleSize(15);
    }

    @Override
    public void Update(double deltaTime)
    {
        switch(m_State)
        {
            case Active:
            {
                if (!m_Origin.IsTriggerPulled())
                    m_State = LaserState.Deactivating;

                if (!m_Origin.GetAnchor().IsValid())
                    m_State = LaserState.Deactivating;

                break;
            }
            case Deactivating:
            {
                m_LaserFadeTimer.Update(deltaTime);
                m_LaserWidth = MathsHelper.Lerp(m_LaserFadeTimer.GetProgress(), m_MaxWidth, 0);
                m_BaseColour.Alpha = m_LaserFadeTimer.GetInverseProgress();
                m_AltColour.Alpha = m_LaserFadeTimer.GetInverseProgress();
                m_RayEmitter.SetInitialColour(m_BaseColour);
                m_RayEmitter.SetFinalColour(m_BaseColour);
                break;
            }
        }

        LockProjectile();
        ScaleLaser(deltaTime);

        m_RayEmitter.SetPosition(GetPosition());
        m_RayEmitter.SetForward(GetForward());
        m_RayEmitter.SetRange(m_LaserLength);
        m_RayEmitter.Update(deltaTime);

        m_DamageOutputPerFrame = m_BaseDamage * deltaTime;

        super.Update(deltaTime);
    }

    @Override
    public boolean IsValid()
    {
        if(m_LaserFadeTimer.TimedOut())
            return false;

        return super.IsValid();
    }

    @Override
    public void CleanUp()
    {

    }

    private void ScaleLaser(double deltaTime)
    {
        m_LaserLength = MathsHelper.Clamp(m_LaserLength + (deltaTime * m_GrowthRate), 0, m_MaxLength);

        double y = GetPosition().J ;
        double j = m_Origin.GetForward().J * m_LaserLength;

        if (y + j < 0)
            m_LaserLength *= Math.abs(y / j);

        SetScale(m_LaserWidth, m_LaserWidth, m_LaserLength);
    }

    private void LockProjectile()
    {
        SetForward(m_Origin.GetForward());
        m_Forward.RotateY(m_FiringBarrel.GetRotation());
        SetPosition(m_Origin.GetPosition());
        GetVelocity().SetVector(0);
    }

    @Override
    public boolean CollidesWith(GameObject other)
    {
        return CollisionDetection.RayTrace(GetPosition(), GetForward(), m_LaserLength, other);
    }

    @Override
    public void CollisionResponse(GameObject other)
    {
        Vector3 otherPos = other.GetPosition();
        Vector3 pos = GetPosition();

        double i = otherPos.I - pos.I;
        double j = otherPos.J - pos.J;
        double k = otherPos.K - pos.K;

        m_LaserLength = Math.sqrt(i*i+j*j+k*k);
    }

    @Override
    public double GetDamageOutput()
    {
        return m_DamageOutputPerFrame;
    }
}
