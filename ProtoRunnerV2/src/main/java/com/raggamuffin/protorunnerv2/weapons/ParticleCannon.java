package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;

public class ParticleCannon extends Weapon
{
    public ParticleCannon(Vehicle anchor, GameLogic game)
    {
        super(anchor, game);

        m_Damage = 100;
        m_Drain = 15;
        m_MuzzleVelocity = 0.0;
        m_Accuracy = 1.0;
        m_LifeSpan = 0.2;
        m_ProjectileFadeInTime = 0.1;
        m_ProjectileFadeOutTime = 0.1;

        m_AudioClip = AudioClips.PulseLaserPunk;

        m_FireMode = new FireControl_Pulse(0.6, 0.06, 1);
        m_ProjectileTemplate = new ProjectileTemplate(this, m_Anchor.GetVehicleInfo(), ModelType.ParticleLaser, GetAffiliation(),
                m_MuzzleVelocity, m_Damage, m_LifeSpan, m_ProjectileFadeInTime, m_ProjectileFadeOutTime, 0.0,
                ProjectileBehaviourType.ParticleLaser, game);

        m_HasLasers = false;

        AddMuzzle( 1, 0, 0);
        AddMuzzle(-1, 0, 0);

        SetColour(Colours.CalvinOrange);

        m_Name = game.GetContext().getString(R.string.weapon_pulse);
    }
}
