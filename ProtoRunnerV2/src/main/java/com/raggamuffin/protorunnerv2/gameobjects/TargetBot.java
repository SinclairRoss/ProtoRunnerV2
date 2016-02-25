package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Weapon_LaserBurner;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

public class TargetBot extends Vehicle
{
    private AIController m_AIController;

    public TargetBot(GameLogic game)
    {
        super(game);

        m_AIController = new AIController(this, game.GetVehicleManager(), game.GetBulletManager(), AIBehaviours.EngageTarget, FireControlBehaviour.Standard);


        m_Model = ModelType.Dummy;

        SetAffiliation(AffiliationKey.RedTeam);

        SetBaseColour(Colours.CalvinOrange);
        m_Position.SetVector(0);

        m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(0);//2
        m_Engine.SetMaxEngineOutput(0);
        m_Engine.SetAfterBurnerOutput(0);

        SelectWeapon(new Weapon_None(this, game));

        m_PrimaryWeapon.OpenFire();

        m_LasersOn = true;
    }

    @Override
    public void Update(double deltaTime)
    {
        m_HullPoints = m_MaxHullPoints;
        m_AIController.Update(deltaTime);

        super.Update(deltaTime);
    }

    @Override
    public void CleanUp()
    {

    }
}
