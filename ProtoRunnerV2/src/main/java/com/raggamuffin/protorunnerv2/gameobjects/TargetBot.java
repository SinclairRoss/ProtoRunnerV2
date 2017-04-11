package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.ai.NavigationalBehaviourInfo;
import com.raggamuffin.protorunnerv2.ai.TargetingBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

public class TargetBot extends Vehicle
{
    private AIController m_AIController;

    public TargetBot(GameLogic game)
    {
        super(game, ModelType.HeagonTube, 1.0);

        SetScale(3, 0, 3);


        NavigationalBehaviourInfo navInfo = new NavigationalBehaviourInfo(0.4, 1.0, 0.7, 0.6);
        m_AIController = new AIController(this, game.GetVehicleManager(), game.GetBulletManager(), navInfo, AIBehaviours.EngageTarget, FireControlBehaviour.Telegraphed, TargetingBehaviour.Standard);

        SetAffiliation(AffiliationKey.RedTeam);

        SetColour(Colours.CalvinOrange);
        SetAlpha(0.4);

        m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(0);//2
        m_Engine.SetMaxEngineOutput(0);
        m_Engine.SetAfterBurnerOutput(0);

        SelectWeapon(new Weapon_None(this, game));

        m_PrimaryWeapon.OpenFire();
    }

    @Override
    public void Update(double deltaTime)
    {
        m_HullPoints = m_MaxHullPoints;
        m_AIController.Update(deltaTime);

        super.Update(deltaTime);
    }
}
