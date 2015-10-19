package com.raggamuffin.protorunnerv2.weapons;

import android.util.Log;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter_Burst;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Projectile_Missile extends Projectile
{
    private enum MissileState
    {
        Docked,
        Released,
        Armed
    }

    MissileState m_State;

    private GameLogic m_Game;
    private Vector3 m_Offset;
    private Vector3 m_Target;
    private Vector3 m_ToTarget;
    private Timer m_LifeSpan;
    private Timer m_ArmingTimer;

    public Projectile_Missile(Weapon origin, GameLogic game)
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

        m_Target = new Vector3();
        m_ToTarget = new Vector3();

        m_DragCoefficient = 0.9;
    }

    @Override
    public void Update(double deltaTime)
    {
        switch(m_State)
        {
            case Docked:
                LockProjectile();

                if (!m_Origin.IsTriggerPulled())
                {
                    m_State = MissileState.Released;
                    AddChild(new TrailEmitter(this, m_Game));
                    m_Game.GetGameAudioManager().PlaySound(AudioClips.MissileEngaged);
                }

                break;

            case Released:
                ApplyForce(m_Forward, 4000);

                m_ArmingTimer.Update(deltaTime);

                if (m_ArmingTimer.TimedOut())
                    m_State = MissileState.Armed;

                break;

            case Armed:

                m_LifeSpan.Update(deltaTime);

                m_Target.SetVector(m_Origin.GetForward());
                m_Target.Scale(8);
                m_Target.Add(m_Origin.GetPosition());

                m_ToTarget.SetVectorDifference(m_Position, m_Target);
                m_ToTarget.Normalise();
                m_ToTarget.Scale(0.1);

                m_Forward.Add(m_ToTarget);
                m_Forward.Normalise();

                UpdateVectors();

                ApplyForce(m_Forward, 4000);

                if (m_Position.J <= 0)
                {
                    m_Position.J = 0;
                    Detonate(false);
                }

                if(m_LifeSpan.TimedOut())
                    Detonate(true);

                break;
        }

        Log.e("what", "" + m_State);

        super.Update(deltaTime);
    }

    @Override
    public boolean CollidesWith(Vehicle other)
    {
        return false;
    }

    @Override
    public void CollisionResponse(Vehicle other)
    {
        Detonate(true);
    }

    private void LockProjectile()
    {
        m_Position.SetVector(m_Offset);
        m_Position.RotateY(m_Origin.GetOrientation());
        m_Position.Add(m_Origin.GetPosition());

        m_Forward.SetVector(m_Origin.GetAnchor().GetForward());
        m_Forward.Add(0,1,0);
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