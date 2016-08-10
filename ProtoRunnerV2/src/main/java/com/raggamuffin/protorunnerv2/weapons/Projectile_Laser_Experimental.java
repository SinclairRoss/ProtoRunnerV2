package com.raggamuffin.protorunnerv2.weapons;

import android.util.Log;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Ray;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Projectile_Laser_Experimental extends Projectile
{
    private GameLogic m_Game;

    private Weapon m_FiringWeapon;
    private WeaponBarrel m_FiringBarrel;

    private double m_DamageOutputPerFrame;

    private final double m_Range;
    private final double m_Speed;
    private double m_LaserLength;

    private boolean m_IsBeingPowered;

    private ParticleEmitter_Ray m_RayEmitter;

    public Projectile_Laser_Experimental(Vector3 position, Vector3 initialVelocity, Vector3 forward, Colour colour, double baseDamage, AffiliationKey affiliation, Weapon firingWeapon, GameLogic game)
    {
        super(position, initialVelocity, forward, colour, baseDamage, affiliation, ModelType.ParticleLaser);

        m_Game = game;

        m_FiringWeapon = firingWeapon;
        m_FiringBarrel = m_FiringWeapon.GetActiveWeaponBarrel();

        m_Range = 100.0;
        m_Speed = 100.0;
        m_LaserLength = 0.0;

        m_RayEmitter = new ParticleEmitter_Ray(game, GetBaseColour(), GetBaseColour(), 500, 0.25, 0.3);
        m_RayEmitter.SetForward(forward);
        m_RayEmitter.SetParticleSize(15);

        m_IsBeingPowered = true;
    }

    public void Update(double deltaTime)
    {
        if(m_FiringWeapon.IsTriggerPulled())
        {
            LockLaser();
        }

        m_LaserLength += m_Speed * deltaTime;
        MathsHelper.Clamp(m_LaserLength, 0, m_Range);

        m_Scale.SetVector(0.25, 0.25, m_LaserLength);

        m_RayEmitter.SetPosition(m_Position);
        m_RayEmitter.SetRange(m_LaserLength);
        m_RayEmitter.SetForward(m_Forward);
        m_RayEmitter.Update(deltaTime);

        m_DamageOutputPerFrame = m_BaseDamage * deltaTime;

        super.Update(deltaTime);
    }

    private void LockLaser()
    {
        m_Forward.SetVector(m_FiringWeapon.GetForward());
        m_Forward.RotateY(m_FiringBarrel.GetRotation());
        UpdateVectors();

        m_Velocity.SetVector(m_FiringWeapon.GetVelocity());
        m_Position.SetVector(m_FiringBarrel.GetPosition());
    }

    @Override
    public boolean IsValid()
    {
        if (!m_IsBeingPowered && m_LaserLength < 0.1)
        {
            return false;
        }

        return super.IsValid();
    }

    @Override
    public CollisionReport CheckForCollision(GameObject object)
    {
        Vector3 endPoint = new Vector3(m_Forward);
        endPoint.Scale(m_LaserLength);
        endPoint.Add(m_Position);

        return CollisionDetection.RayCastSphere(m_Position, endPoint, object.GetPosition(), object.GetBoundingRadius());
    }

    @Override
    public void CollisionResponse(CollisionReport report)
    {
        double rayEntry = report.GetRayEntry();
        double rayExit = report.GetRayExit();

        // if the ray lies completely within sphere.
        if(rayEntry < 0.0 && rayExit > 1.0)
        {
            ForceInvalidation();
        }
        else // If ray intersects sphere.
        {
            // If collision in middle of ray.
            if (rayEntry > 0.0 && rayEntry < 1.0 &&
                rayExit  > 0.0 && rayExit  < 1.0)
            {
                Split(report.GetExitPoint());
                CutHead(report.GetRayEntry());
            }
            else    // If collision occurs at an end point of the ray.
            {
                // If the ray enters the sphere.
                if (rayEntry >= 0.0 && rayEntry <= 1.0)
                {
                    CutHead(report.GetRayEntry());
                }

                // If the ray exits the sphere.
                if (rayExit >= 0.0 && rayExit <= 1.0)
                {
                    CutTail(report.GetExitPoint());
                }
            }
        }
    }

    private void CutHead(double entryPoint)
    {
        m_LaserLength *= entryPoint;
    }

    private void CutTail(Vector3 exitPoint)
    {
        m_Position.SetVector(exitPoint);
    }

    private void Split(Vector3 exitPoint)
    {
        Projectile_Laser_Experimental separatedLaser = new Projectile_Laser_Experimental(exitPoint, m_Velocity, m_Forward, m_Colour, m_BaseDamage, GetAffiliation(), m_FiringWeapon, m_Game);
        //separatedLaser.SetHeadPosition(m_HeadNode.GetPosition());
        separatedLaser.TurnOffPower();

        m_Game.GetBulletManager().AddProjectile(separatedLaser);

        Log.e("Laser", "Split");
    }

    public void SetHeadPosition(Vector3 headPosition)
    {
        //m_HeadNode.SetPosition(headPosition);
        m_Scale.SetVector(0.25, 0.25, m_LaserLength);
    }

    @Override
    public void CleanUp()
    {

    }

    @Override
    public double GetDamageOutput()
    {
        return m_DamageOutputPerFrame;
    }


    private void TurnOffPower()
    {
        m_IsBeingPowered = false;
    }
}
