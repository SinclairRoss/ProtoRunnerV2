package com.raggamuffin.protorunnerv2.weapons;

import android.util.Log;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.BurstEmitter;
import com.raggamuffin.protorunnerv2.particles.DirectionalEmitter;
import com.raggamuffin.protorunnerv2.particles.EmissionBehaviour_Timed;
import com.raggamuffin.protorunnerv2.particles.ParticleEmitter;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ProjectileBehaviour_Laser extends ProjectileBehaviour
{
    private Vector3 m_DockedPosition;
    private Vector3 m_Offset;
    private Weapon m_FiringWeapon;

    private Vector3 m_EndPoint;
    private double m_Scale;
    private final double m_MaxScale;
    private final double m_GrowthRate;

    private Timer m_Timer;
    private BurstEmitter m_Emitter;

    public ProjectileBehaviour_Laser(Projectile anchor, Vector3 muzzlePos, GameLogic game)
    {
        super(anchor);

        m_DockedPosition = new Vector3();
        m_Offset = new Vector3(muzzlePos);
        m_FiringWeapon = m_Anchor.GetFiringWeapon();

        m_EndPoint = new Vector3();
        m_Scale = 0.0;
        m_MaxScale = 200;
        m_GrowthRate = 100;

        m_Anchor.SetScale(0.0);

        m_Timer = new Timer(0.5);
        m_Emitter = new BurstEmitter(m_Anchor, game.GetParticleManager());
    }

    @Override
    public void Update(double deltaTime)
    {
        LockProjectile();

        m_Scale = MathsHelper.Clamp(m_Scale + (deltaTime * m_GrowthRate), 0, m_MaxScale);
        m_Anchor.SetScale(m_Scale);

        m_Timer.Update(deltaTime);

   //     if(m_Timer.TimedOut())
     //   {
            m_EndPoint.SetVector(m_Anchor.GetForward());
        m_EndPoint.Output("Forward");
            m_EndPoint.Scale(m_Scale);
            m_EndPoint.Add(m_Anchor.GetPosition());
            m_Emitter.SetPosition(m_EndPoint);
            m_Emitter.Burst();
     //       m_Timer.ResetTimer();
    //    }
    }

    private void LockProjectile()
    {
        m_Anchor.SetForward(m_Anchor.GetFiringWeapon().GetForward());
        m_DockedPosition.SetVector(m_Offset);
        m_DockedPosition.RotateY(m_FiringWeapon.GetOrientation());
        m_DockedPosition.Add(m_FiringWeapon.GetPosition());

        m_Anchor.SetYaw(m_FiringWeapon.GetOrientation());
        m_Anchor.SetPosition(m_DockedPosition);
    }

    @Override
    public boolean UseSimpleCollisionDetection()
    {
        return false;
    }

    @Override
    public void CleanUp()
    {
        m_Anchor.SetScale(1.0);
    }
}
