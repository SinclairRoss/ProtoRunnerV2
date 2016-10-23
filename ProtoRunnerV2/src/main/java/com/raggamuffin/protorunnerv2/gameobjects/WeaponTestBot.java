package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Weapon;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;
import com.raggamuffin.protorunnerv2.weapons.Weapon_PunkShot;

public class WeaponTestBot extends Vehicle
{
    private Timer m_Timer;
    private boolean m_On;
    private Vector3 m_Target;
    private VehicleManager m_VManager;

    public WeaponTestBot(GameLogic game)
    {
        super(game, ModelType.Ring);

        m_BoundingRadius = 2.0;

        SetAffiliation(AffiliationKey.RedTeam);

        SetBaseColour(Colours.CalvinOrange);
        m_Position.SetVector(0);

        m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(0);//2
        m_Engine.SetMaxEngineOutput(0);
        m_Engine.SetAfterBurnerOutput(0);

        m_Target = new Vector3(0, -1, 1);
        m_Target.Normalise();

        Weapon tester = new Weapon_PunkShot(this, game);
        SelectWeapon(tester);
        tester.SetTargetVector(m_Forward);

        m_Timer = new Timer(10.0);
        m_Timer.MaxOutTimer();
        m_On = false;

        m_PrimaryWeapon.OpenFire();

        m_LasersOn = true;

        m_VManager = game.GetVehicleManager();
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
