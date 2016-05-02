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
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Projectile_Laser extends Projectile
{
    private GameLogic m_Game;

    private LaserNode m_HeadNode;
    private LaserNode m_TailNode;

    private Vector3 m_IntialVelocity;

    private double m_DamageOutputPerFrame;

    private Timer m_TailLagTimer;
    private final double m_Range;
    private double m_LaserLength;

    private ParticleEmitter_Ray m_RayEmitter;

    public Projectile_Laser(Vector3 position, Vector3 initialVelocity, Vector3 forward, Colour colour, double baseDamage, AffiliationKey affiliation, GameLogic game)
    {
        super(position, initialVelocity, forward, colour, baseDamage, affiliation, ModelType.ParticleLaser);

        m_Game = game;

        m_Range = 100.0;
        double speed = 1.0;

        m_IntialVelocity = new Vector3(initialVelocity);
        m_HeadNode = new LaserNode(position, forward, m_IntialVelocity, speed, m_Range);
        m_TailNode = new LaserNode(position, forward, m_IntialVelocity, speed, m_Range);

        double laserLength = 4;
        double lagTime = laserLength / speed;
        m_TailLagTimer = new Timer(lagTime);

        m_RayEmitter = new ParticleEmitter_Ray(game, GetBaseColour(), GetBaseColour(), 500, 0.25, 0.3);
        m_RayEmitter.SetForward(forward);
        m_RayEmitter.SetParticleSize(15);
    }

    public void Update(double deltaTime)
    {
        m_HeadNode.Update(deltaTime);
        UpdateTail(deltaTime);

        m_LaserLength = CalculateLength();
        m_Scale.SetVector(0.25, 0.25, m_LaserLength);

        m_RayEmitter.SetPosition(m_TailNode.GetPosition());
        m_RayEmitter.SetRange(m_LaserLength);
        m_RayEmitter.Update(deltaTime);

        m_DamageOutputPerFrame = m_BaseDamage * deltaTime;

        super.Update(deltaTime);
    }

    private void UpdateTail(double deltaTime)
    {
        if (m_TailLagTimer.TimedOut())
        {
            m_TailNode.Update(deltaTime);
        }
        else
        {
            m_TailLagTimer.Update(deltaTime);
        }
    }

    @Override
    public boolean IsValid()
    {
        if (m_TailNode.GetDistanceTravelled() >= m_Range)
        {
            return false;
        }

        if (m_TailLagTimer.TimedOut() && m_LaserLength < 0.1)
        {
            return false;
        }

        return super.IsValid();
    }

    @Override
    public CollisionReport CheckForCollision(GameObject object)
    {
        return CollisionDetection.RayCastSphere(m_TailNode.GetPosition(), m_HeadNode.GetPosition(), object.GetPosition(), object.GetBoundingRadius());
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
                CutHead(report.GetEntryPoint());
            }
            else    // If collision occurs at an end point of the ray.
            {
                // If the ray enters the sphere.
                if (rayEntry >= 0.0 && rayEntry <= 1.0)
                {
                    CutHead(report.GetEntryPoint());
                }

                // If the ray exits the sphere.
                if (rayExit >= 0.0 && rayExit <= 1.0)
                {
                    CutTail(report.GetExitPoint());
                }
            }
        }
    }

    private void CutHead(Vector3 entryPoint)
    {
        m_HeadNode.SetPosition(entryPoint);
    }

    private void CutTail(Vector3 exitPoint)
    {
        m_TailNode.SetPosition(exitPoint);
    }

    private void Split(Vector3 exitPoint)
    {
        //TODO decrease distance travelled by length of split section.
        Projectile_Laser separatedLaser = new Projectile_Laser(exitPoint, m_IntialVelocity, m_Forward, m_Colour, m_BaseDamage, GetAffiliation(), m_Game);
        separatedLaser.SetHeadPosition(m_HeadNode.GetPosition());
        separatedLaser.TimeOutTailLag();
        
        m_Game.GetBulletManager().AddProjectile(separatedLaser);

        Log.e("Laser", "Split");
    }

    public void TimeOutTailLag()
    {
        m_TailLagTimer.MaxOutTimer();
    }

    public void SetHeadPosition(Vector3 headPosition)
    {
        m_HeadNode.SetPosition(headPosition);
        m_LaserLength = CalculateLength();
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

    private double CalculateLengthSqr()
    {
        double i = m_HeadNode.GetPosition().I - m_TailNode.GetPosition().I;
        double j = m_HeadNode.GetPosition().J - m_TailNode.GetPosition().J;
        double k = m_HeadNode.GetPosition().K - m_TailNode.GetPosition().K;

        return (i * i) + (j * j) + (k * k);
    }

    private double CalculateLength()
    {
        return Math.sqrt(CalculateLengthSqr());
    }

    @Override
    public Vector3 GetPosition()
    {
        return m_TailNode.GetPosition();
    }
}
