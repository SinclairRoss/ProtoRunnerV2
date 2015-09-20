package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.weapons.LaserBurner;

public class WeaponTestBot extends Vehicle
{
    private Timer m_Timer;
    private boolean m_On;

    public WeaponTestBot(GameLogic game)
    {
        super(game);

        m_Model = ModelType.WeaponDrone;

        SetAffiliation(AffiliationKey.RedTeam);

        SetBaseColour(Colours.CalvinOrange);
        m_Position.SetVector(0);

        m_Engine = new Engine_Standard(this, game, new EngineUseBehaviour_Drain(this));
        m_Engine.SetMaxTurnRate(0);//2
        m_Engine.SetMaxEngineOutput(0);
        m_Engine.SetAfterBurnerOutput(0);

        SelectWeapon(new LaserBurner(this, game));

        m_Timer = new Timer(5.0);
        m_On = true;

        m_PrimaryWeapon.OpenFire();

        m_LasersOn = true;
    }

    @Override
    public boolean IsValid()
    {
        return true;
    }

    @Override
    public void Update(double deltaTime)
    {
        m_HullPoints = m_MaxHullPoints;
        m_Timer.Update(deltaTime);

        if(m_Timer.TimedOut())
        {
            if(m_On)
            {
                m_PrimaryWeapon.CeaseFire();
                m_On = false;
            }
            else
            {
                m_PrimaryWeapon.OpenFire();
                m_On = true;
            }

            m_Timer.ResetTimer();
        }

        super.Update(deltaTime);
    }
}
