package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Point;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Ray;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
import com.raggamuffin.protorunnerv2.utils.Colour;
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

    private Weapon m_FiringWeapon;
    private WeaponBarrel m_FiringBarrel;

    private double m_DamageOutputPerFrame;

    private ParticleEmitter_Ray m_RayEmitter;
    private ParticleEmitter_Point m_PointEmitter;

    public Projectile_Laser(GameLogic game, Vector3 position, Vector3 initialVelocity, Vector3 forward, Colour colour, double baseDamage, AffiliationKey affiliation, Weapon firingWeapon)
    {
        super(position, initialVelocity, forward, colour, baseDamage, affiliation, ModelType.ParticleLaser);

        m_FiringWeapon = firingWeapon;
        m_FiringBarrel = m_FiringWeapon.GetActiveWeaponBarrel();

        m_State = LaserState.Active;

        m_GrowthRate = 500;
        m_LaserFadeTimer = new Timer(0.5);

        m_LaserLength = 0.0;
        m_MaxLength = 250;
        m_MaxWidth = 0.25;
        m_LaserWidth =  0.25;

        m_BaseDamage = 1000;

        SetScale(0.0);

        m_RayEmitter = new ParticleEmitter_Ray(game, GetColour(), GetColour(), 10, 0.5, 0.3);
        m_RayEmitter.SetForward(forward);

        m_PointEmitter = new ParticleEmitter_Point(game, GetColour(), GetColour(), 2, 2);
    }

    @Override
    public void Update(double deltaTime)
    {
        switch(m_State)
        {
            case Active:
            {
                if (!m_FiringWeapon.IsTriggerPulled() || !m_FiringWeapon.GetAnchor().IsValid())
                {
                    Deactivate();
                }

                break;
            }
            case Deactivating:
            {
                m_LaserWidth = MathsHelper.Lerp(m_LaserFadeTimer.GetProgress(), m_MaxWidth, 0);
                SetAlpha(m_LaserFadeTimer.GetInverseProgress());

                break;
            }
            case Deactivated: { break; }
        }

        LockProjectile();
        ScaleLaser(deltaTime);

        Vector3 pos = GetPosition();

        m_RayEmitter.SetPosition(pos);
        m_RayEmitter.SetForward(GetForward());
        m_RayEmitter.SetLength(m_LaserLength);
        m_RayEmitter.Update(deltaTime);

        m_PointEmitter.SetPosition(pos.X, -1, pos.Z);
        m_PointEmitter.SetVelocity(m_FiringWeapon.GetVelocity());
        m_PointEmitter.Update(deltaTime);

        m_DamageOutputPerFrame = m_BaseDamage * deltaTime;

        super.Update(deltaTime);
    }

    @Override
    public boolean IsValid()
    {
        return !m_LaserFadeTimer.HasElapsed();
    }

    @Override
    public CollisionReport CheckForCollision(GameObject object)
    {
        Vector3 endPoint = new Vector3(GetForward());
        endPoint.Scale(m_LaserLength);
        endPoint.Add(GetPosition());

        CollisionReport report = null;

        if(m_State == LaserState.Active)
        {
            report = CollisionDetection.RayCastSphere(GetPosition(), endPoint, object.GetPosition(), object.GetBoundingRadius());
        }

        return report;
    }

    @Override
    public void CollisionResponse(CollisionReport report)
    {
        m_LaserLength *= report.GetRayEntry();
    }

    @Override
    public void CleanUp()
    {}

    private void ScaleLaser(double deltaTime)
    {
        m_LaserLength = MathsHelper.Clamp(m_LaserLength + (deltaTime * m_GrowthRate), 0, m_MaxLength);

        double Position_Y = GetPosition().Y;
        double Length_Y = m_FiringWeapon.GetForward().Y * m_LaserLength;

        if (Position_Y + Length_Y < -1.0)
        {
            m_LaserLength *= Math.abs(Position_Y / Length_Y);
            m_LaserLength += 1.0;
            m_PointEmitter.On();
        }
        else
        {
            m_PointEmitter.Off();
        }

        SetScale(m_LaserWidth, m_LaserWidth, m_LaserLength);
    }

    private void LockProjectile()
    {
        SetForward(m_FiringWeapon.GetForward());
        Rotate(m_FiringBarrel.GetRotation());
        SetPosition(m_FiringWeapon.GetPosition());
        SetVelocity(0);
    }

    @Override
    public double GetDamageOutput() { return m_DamageOutputPerFrame; }

    private void Deactivate()
    {
        m_State = LaserState.Deactivating;
        m_LaserFadeTimer.Start();
    }
}