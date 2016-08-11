package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Spring3;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Weapon_LaserBurner;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

public class Vehicle_Drone extends Vehicle
{
    private final double RESTING_HEIGHT = 5.0;
    private final double BOBBING_SPEED = 1.0;
    private final double BOBBING_DISTANCE = 4.0;

    private Vehicle_Carrier m_Anchor;
    private AIController m_AIController;

    private double m_VerticalMovementTimer;

    public Vehicle_Drone(GameLogic game, Vehicle_Carrier anchor)
    {
        super(game);

        m_Anchor = anchor;
        m_Position.SetVector(m_Anchor.GetPosition());

        m_BaseColour = game.GetColourManager().GetSecondaryColour();
        m_AltColour = game.GetColourManager().GetPrimaryColour();

        m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(2.0);
        m_Engine.SetMaxEngineOutput(15000);
        m_Engine.SetDodgeOutput(0);
        m_Mass = 100;

        SetAffiliation(AffiliationKey.RedTeam);

        SelectWeapon(new Weapon_LaserBurner(this, game));

        m_Model = ModelType.WeaponDrone;

        VehicleManager vehicleManager = game.GetVehicleManager();
        m_AIController = new AIController(this, vehicleManager, game.GetBulletManager(), AIBehaviours.FollowTheLeader, FireControlBehaviour.BeamSweep);
        m_AIController.SetLeader(anchor);

        m_CanBeTargeted = false;
    }

    @Override
    public void Update(double deltaTime)
    {
        m_AIController.Update(deltaTime);
        UpdateBobbing(deltaTime);

        super.Update(deltaTime);
    }

    @Override
    public boolean IsValid()
    {
        if(!m_Anchor.IsValid())
        {
            m_BurstEmitter.Burst();
            m_PrimaryWeapon.CeaseFire();
            return false;
        }

        return true;
    }

    @Override
    public void CleanUp()
    {
        m_PrimaryWeapon.CleanUp();
    }

    private void UpdateBobbing(double deltaTime)
    {
        m_VerticalMovementTimer += deltaTime * BOBBING_SPEED;
        m_VerticalMovementTimer %= Math.PI * 2;

        m_Position.J = RESTING_HEIGHT + (Math.sin(m_VerticalMovementTimer) * BOBBING_DISTANCE);
    }
}