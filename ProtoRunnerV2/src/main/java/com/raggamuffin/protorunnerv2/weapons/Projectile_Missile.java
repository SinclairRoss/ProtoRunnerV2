package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Burst;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class Projectile_Missile extends Projectile
{
    private enum MissileState
    {
        Docked,
        Priming,
        Released,
        Armed
    }

    MissileState m_State;

    private GameLogic m_Game;
    private WeaponBarrel m_Offset;
    private Vector3 m_Target;
    private Vector3 m_ToTarget;
    private Timer m_LifeSpan;
    private Timer m_ArmingTimer;
    private Timer m_PrimingTimer;
    private double m_EngineOutput;
    private final double LAUNCH_DELAY = 0.3;
    private final double TARGETING_RANGE = 1000;

    private final double FLARE_DISTRACTION_RANGE = 100;
    private final double FLARE_DISTRACTION_RANGE_SQR = FLARE_DISTRACTION_RANGE * FLARE_DISTRACTION_RANGE;
    private Projectile_Flare m_LockedTargetFlare;

    public Projectile_Missile(Weapon origin, GameLogic game, int index)
    {
        super(origin);

        m_Game = game;

        m_Model = ModelType.Missile;
        m_State = MissileState.Docked;

        m_Offset = m_Origin.GetActiveWeaponBarrel();

        LockProjectile();

        m_Velocity.SetVector(0);

        m_LifeSpan = new Timer(2.5);
        m_ArmingTimer = new Timer(0.65);
        m_PrimingTimer = new Timer(LAUNCH_DELAY * index);
        m_Target = new Vector3();
        m_ToTarget = new Vector3();

        m_DragCoefficient = 0.9;

        m_EngineOutput = 12000;
    }

    @Override
    public void Update(double deltaTime)
    {
        switch(m_State)
        {
            case Docked:
            {
                LockProjectile();

                if (!m_Origin.IsTriggerPulled())
                {
                    m_State = MissileState.Priming;
                }

                break;
            }
            case Priming:
            {
                LockProjectile();

                m_PrimingTimer.Update(deltaTime);

                if (m_PrimingTimer.TimedOut())
                {
                    m_State = MissileState.Released;
                    AddChild(new TrailEmitter(this, m_Game));
                    m_Game.GetGameAudioManager().PlaySound(AudioClips.MissileEngaged);
                }

                break;
            }
            case Released:
            {
                ApplyForce(m_Forward, m_EngineOutput);

                m_ArmingTimer.Update(deltaTime);

                if (m_ArmingTimer.TimedOut())
                {
                    m_Velocity.SetVector(m_Origin.GetVelocity());
                    m_State = MissileState.Armed;
                }

                break;
            }
            case Armed:
            {
                m_LifeSpan.Update(deltaTime);

                GameObject target = FindTarget();

                if (target != null)
                {
                    m_Target.SetVector(target.GetPosition());
                }
                else
                {
                    m_Target.SetVector(m_Origin.GetForward());
                    m_Target.Scale(10);
                }

                m_ToTarget.SetVectorDifference(m_Position, m_Target);
                m_ToTarget.Normalise();

                m_Forward.SetVector(m_ToTarget);

                UpdateVectors();

                ApplyForce(m_Forward, m_EngineOutput);

                if (m_Position.J <= 0)
                {
                    m_Position.J = 0;
                    Detonate();
                }

                if (m_LifeSpan.TimedOut())
                {
                    Detonate();
                }

                break;
            }
        }

        super.Update(deltaTime);
    }

    @Override
    public void CleanUp()
    {

    }

    private GameObject FindTarget()
    {
        if(m_LockedTargetFlare == null)
        {
            m_LockedTargetFlare = FindTargetFlare();
        }

        GameObject target = null;

        if(m_LockedTargetFlare == null)
        {
            double highestUtility = Double.MIN_VALUE;

            ArrayList<Vehicle> enemyVehicles = m_Game.GetVehicleManager().GetOpposingTeam(m_Origin.GetAffiliation());
            for (Vehicle possibleTarget : enemyVehicles)
            {
                m_ToTarget.SetVectorDifference(GetPosition(), possibleTarget.GetPosition());
                double utility = 0.0;

                // Phase 1: Calculate utility based on distance from target.
                double DistanceSqr = m_ToTarget.GetLengthSqr();
                utility += 1.0 - MathsHelper.Normalise(DistanceSqr, 0.0, TARGETING_RANGE * TARGETING_RANGE);

                if (utility > highestUtility)
                {
                    highestUtility = utility;
                    target = possibleTarget;
                }
            }
        }
        else
        {
            target = m_LockedTargetFlare;
        }

        return target;
    }

    private Projectile_Flare FindTargetFlare()
    {
        ArrayList<Projectile_Flare> flares =  m_Game.GetBulletManager().GetActiveFlares();

        double closestTargetDistance = Double.MAX_VALUE;
        Projectile_Flare closestFlare = null;

        for(Projectile_Flare flare : flares)
        {
            if (flare.GetAffiliation() != GetAffiliation())
            {
                m_ToTarget.SetVectorDifference(GetPosition(), flare.GetPosition());
                double toFlareSqr = m_ToTarget.GetLengthSqr();

                if (toFlareSqr < FLARE_DISTRACTION_RANGE_SQR &&
                        toFlareSqr < closestTargetDistance)
                {
                    closestFlare = flare;
                    closestTargetDistance = toFlareSqr;
                }
            }
        }

        return closestFlare;
    }

    @Override
    public boolean CollidesWith(GameObject other)
    {
        return false;
    }

    @Override
    public void CollisionResponse(GameObject other)
    {
        Detonate();
    }

    private void LockProjectile()
    {
        m_Position.SetVector(m_Offset.GetPosition());
        m_Position.RotateY(m_Origin.GetOrientation());
        m_Position.Add(m_Origin.GetPosition());

        m_Forward.SetVector(m_Origin.GetAnchor().GetForward());
        m_Forward.Add(0,2,0);
        m_Forward.Normalise();

        m_Up.SetVector(m_Origin.GetAnchor().GetBackward());
        m_Right.SetVector(m_Origin.GetAnchor().GetRight());
    }

    private void Detonate()
    {
        ParticleEmitter_Burst detonationEmitter = new ParticleEmitter_Burst(m_Game, m_BaseColour, m_AltColour, 70);
        detonationEmitter.SetPosition(m_Position);
        detonationEmitter.Burst();

        ForceInvalidation();
    }
}