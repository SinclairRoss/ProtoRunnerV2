package com.raggamuffin.protorunnerv2.weapons;

import android.content.Context;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class PanicSwitch extends Weapon
{
    public PanicSwitch(Vehicle anchor, GameLogic game)
    {
        super(anchor, game);

        m_Damage = 999999.0;
        m_MuzzleVelocity = 0.0;
        m_Accuracy = 1.0;
        m_LifeSpan = 5.0;

        m_FireMode = new FireControl_Auto(1.0);
        m_ProjectileTemplate = new ProjectileTemplate(this, m_Anchor.GetVehicleInfo(), ModelType.Ring, GetAffiliation(),
                m_MuzzleVelocity, m_Damage, m_LifeSpan, m_ProjectileFadeInTime,
                ProjectileBehaviourType.Panic, m_ParticleManager, m_BulletManager, m_AudioService, game.GetVehicleManager(), game.GetPubSubHub());

        m_HasLasers = false;

        AddMuzzle( 0, 0, 0);

        SetColour(Colours.Azure);

        m_Name = game.GetContext().getString(R.string.weapon_panic);
    }

    @Override
    public double GetDrain()
    {
        return m_Anchor.GetHullPoints() * 0.3;
    }
}
