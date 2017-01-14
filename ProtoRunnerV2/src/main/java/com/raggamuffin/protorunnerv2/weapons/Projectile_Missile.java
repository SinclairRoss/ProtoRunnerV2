//TODO: THESE MISSILES ARE SHIT... PLEASE FIX THIS.

package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Burst;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer_Accumulation;
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

    private MissileState m_State;

    private WeaponBarrel m_Offset;
    private Vector3 m_Target;
    private Vector3 m_ToTarget;
    private Timer_Accumulation m_LifeSpan;
    private Timer_Accumulation m_ArmingTimer;
    private Timer_Accumulation m_PrimingTimer;
    private double m_EngineOutput;
    private final double LAUNCH_DELAY = 0.3;
    private final double TARGETING_RANGE = 1000;

    private final double FLARE_DISTRACTION_RANGE = 100;
    private final double FLARE_DISTRACTION_RANGE_SQR = FLARE_DISTRACTION_RANGE * FLARE_DISTRACTION_RANGE;
    private Projectile_Flare m_LockedTargetFlare;

    private Weapon m_Origin;

    private GameLogic m_Game;

    public Projectile_Missile(GameLogic game, Vector3 position, Vector3 initialVelocity, Vector3 forward, Colour colour, double baseDamage, int index, AffiliationKey affiliation, Weapon origin)
    {
        super(game, position, initialVelocity, forward, colour, baseDamage, affiliation, ModelType.Missile);

        m_Game = game;

        m_Origin = origin;

        m_State = MissileState.Docked;

        m_Offset = m_Origin.GetActiveWeaponBarrel();

        LockProjectile();

        m_Velocity.SetVector(0);

        m_LifeSpan = new Timer_Accumulation(2.5);
        m_ArmingTimer = new Timer_Accumulation(0.6);
        m_PrimingTimer = new Timer_Accumulation(LAUNCH_DELAY * index);
        m_Target = new Vector3();
        m_ToTarget = new Vector3();

        m_Mass = 10;

        m_EngineOutput = 2000;
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
                    AddObjectToGameObjectManager(new TrailEmitter(m_Game, this));
                    m_Game.GetGameAudioManager().PlaySound(AudioClips.Missile_Engaged); // TODO: Convert to audio emitter.
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
                    m_Target.Scale(50);
                    m_Target.Add(m_Position);
                }

                m_ToTarget.SetVectorDifference(m_Position, m_Target);
                m_ToTarget.Normalise();
                UpdateVectorsWithForward(m_ToTarget);

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

    private GameObject FindTarget()
    {
        GameObject target = m_LockedTargetFlare;

        if(target == null)
        {
            target = FindTargetFlare();

            if (target == null)
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
            if (true)//flare.GetAffiliation() != GetAffiliation())
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
    public CollisionReport CheckForCollision(GameObject object)
    {
        return CollisionDetection.CheckCollisions(this, object);
    }

    @Override
    public void CollisionResponse(CollisionReport report)
    {
        Detonate();
    }

    @Override
    public void CleanUp()
    {

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
