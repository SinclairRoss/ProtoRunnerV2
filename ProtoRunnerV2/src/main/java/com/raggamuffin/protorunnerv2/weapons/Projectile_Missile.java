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
    private Vector3 m_Offset;
    private Vector3 m_Target;
    private Vector3 m_ToTarget;
    private double m_TurnRate;
    private Timer m_LifeSpan;
    private Timer m_ArmingTimer;
    private Timer m_PrimingTimer;
    private double m_EngineOutput;
    private final double LAUNCH_DELAY = 0.15;
    private final double TARGETING_RANGE = 1000;
    private final double TARGETING_ARC  = Math.toRadians(90);

    public Projectile_Missile(Weapon origin, GameLogic game, int index)
    {
        super(origin);

        m_Game = game;

        m_Model = ModelType.Missile;
        m_State = MissileState.Docked;

        m_Offset = new Vector3(m_Origin.GetMuzzleOffset());

        LockProjectile();

        m_Velocity.SetVector(0);

        m_LifeSpan = new Timer(5.0);
        m_ArmingTimer = new Timer(0.65);
        m_PrimingTimer = new Timer(LAUNCH_DELAY * index);
        m_Target = new Vector3();
        m_ToTarget = new Vector3();
        m_TurnRate = 20.0;

        m_DragCoefficient = 0.9;

        m_EngineOutput = 12000;
    }

    @Override
    public void Update(double deltaTime)
    {
        switch(m_State)
        {
            case Docked:
                LockProjectile();

                if (!m_Origin.IsTriggerPulled())
                    m_State = MissileState.Priming;

                break;

            case Priming:
                LockProjectile();

                m_PrimingTimer.Update(deltaTime);

                if(m_PrimingTimer.TimedOut())
                {
                    m_State = MissileState.Released;
                    AddChild(new TrailEmitter(this, m_Game));
                    m_Game.GetGameAudioManager().PlaySound(AudioClips.MissileEngaged);
                }

                break;

            case Released:
                ApplyForce(m_Forward, m_EngineOutput);

                m_ArmingTimer.Update(deltaTime);

                if (m_ArmingTimer.TimedOut())
                {
                    m_Velocity.SetVector(m_Origin.GetVelocity());
                    m_State = MissileState.Armed;
                }

                break;

            case Armed:
                m_LifeSpan.Update(deltaTime);

                GameObject target = FindTarget();

                if(target != null)
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
                m_ToTarget.Scale(deltaTime * m_TurnRate);

                m_Forward.Add(m_ToTarget);
                m_Forward.Normalise();

                UpdateVectors();

                ApplyForce(m_Forward, m_EngineOutput);

                if (m_Position.J <= 0)
                {
                    m_Position.J = 0;
                    Detonate(false);
                }

                if(m_LifeSpan.TimedOut())
                    Detonate(true);

                break;
        }

        super.Update(deltaTime);
    }

    @Override
    public void CleanUp()
    {

    }

    private GameObject FindTarget()
    {
        GameObject target = null;

        double highestUtility = Double.MIN_VALUE;

        ArrayList<Vehicle> enemyVehicles = m_Game.GetVehicleManager().GetOpposingTeam(m_Origin.GetAffiliation());
        for(Vehicle possibleTarget : enemyVehicles)
        {
            m_ToTarget.SetVectorDifference(GetPosition(), possibleTarget.GetPosition());
            double utility = 0.0;

            // Phase 1: Calculate utility based on distance from target.
            double DistanceSqr = m_ToTarget.GetLengthSqr();
            utility += 1.0 - MathsHelper.Normalise(DistanceSqr, 0.0, TARGETING_RANGE * TARGETING_RANGE);

            // Phase 2: Calculate utility base on direction to target
            // double deltaHeading = Vector3.RadiansBetween(GetForward(), m_ToTarget);
            // utility += (1.0 - MathsHelper.Normalise(deltaHeading, 0.0, TARGETING_ARC));

            if(utility > highestUtility)
            {
                highestUtility 	= utility;
                target 		= possibleTarget;
            }
        }

        return target;
    }

    @Override
    public boolean CollidesWith(GameObject other)
    {
        return false;
    }

    @Override
    public void CollisionResponse(GameObject other)
    {
        Detonate(true);
    }

    private void LockProjectile()
    {
        m_Position.SetVector(m_Offset);
        m_Position.RotateY(m_Origin.GetOrientation());
        m_Position.Add(m_Origin.GetPosition());

        m_Forward.SetVector(m_Origin.GetAnchor().GetForward());
        m_Forward.Add(0,2,0);
        m_Forward.Normalise();

        m_Up.SetVector(m_Origin.GetAnchor().GetBackward());
        m_Right.SetVector(m_Origin.GetAnchor().GetRight());
    }

    private void Detonate(boolean open)
    {
        ParticleEmitter_Burst detonationEmitter = new ParticleEmitter_Burst(m_Game, m_BaseColour, m_AltColour, 50);
        detonationEmitter.SetPosition(m_Position);

        if(!open)
            detonationEmitter.SetHalfOpenMode();

        detonationEmitter.Burst();

        ForceInvalidation();
    }
}