package com.raggamuffin.protorunnerv2.weapons;

import com.raggamuffin.protorunnerv2.audio.AudioClips;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class Weapon_LaserBurner extends Weapon
{
    public Weapon_LaserBurner(Vehicle anchor, GameLogic game)
    {
        super(anchor, game, ProjectileType.Laser, AudioClips.Laser_Enemy);

        m_Damage = 100;
        m_FireMode = new FireControl_None();

        AddBarrel(0, 0, 0);
    }

    @Override
    public void PullTrigger()
    {
        super.PullTrigger();

        m_Game.GetBulletManager().CreateProjectile(this);
        m_AudioEmitter.Start();
    }

    @Override
    public void Update(double deltaTime)
    {
        m_WeaponComponent.Update(deltaTime);
    }
}
