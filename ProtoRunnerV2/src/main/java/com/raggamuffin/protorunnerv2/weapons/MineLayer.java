package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.Vehicles.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class MineLayer extends Weapon
{
    public MineLayer(Vehicle anchor, GameLogic game)
    {
        super(anchor, game);

        m_Damage = 100;
        m_Drain = 25;
        m_MuzzleVelocity = 0.0;
        m_Accuracy = 1.0;
        m_LifeSpan = 5.0;
        m_ProjectileFadeInTime = 1.0;

        m_AudioClip = AudioClips.MissileSpawned;

        m_FireMode = new FireControl_Pulse(1.5, 0.2, 4);

        m_ProjectileTemplate = new ProjectileTemplate(this, m_Anchor.GetVehicleInfo(), ModelType.Mine, GetAffiliation(),
                m_MuzzleVelocity, m_Damage, m_LifeSpan, m_ProjectileFadeInTime, m_ProjectileFadeOutTime, 2.0,
                ProjectileBehaviourType.Mine, game, SpecialProjectileBehaviourType.Explode);

        AddMuzzle(0, 0, -1);

        SetColour(Colours.Yellow);
    }
}
