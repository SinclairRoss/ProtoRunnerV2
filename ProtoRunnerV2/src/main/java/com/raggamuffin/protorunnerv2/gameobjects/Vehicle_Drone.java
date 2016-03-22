package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Spring1;
import com.raggamuffin.protorunnerv2.weapons.Weapon_LaserBurner;
import com.raggamuffin.protorunnerv2.weapons.Weapon_None;

public class Vehicle_Drone extends Vehicle
{
    private Vehicle_Carrier m_Anchor;
    private AIController m_AIController;

    protected Spring1 m_HoverSpring;

    private Weapon_None m_Unarmed;
    private Weapon_LaserBurner m_Laser;

    public Vehicle_Drone(GameLogic game, Vehicle_Carrier anchor)
    {
        super(game);

        m_Anchor = anchor;

        SetBaseColour(m_Anchor.GetBaseColour());

        m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(2.0);
        m_Engine.SetMaxEngineOutput(300);
        m_Engine.SetDodgeOutput(0);
        m_Mass = 100;

        SetAffiliation(AffiliationKey.RedTeam);

        m_Unarmed = new Weapon_None(this, game);
        m_Laser = new Weapon_LaserBurner(this, game);

        SelectWeapon(m_Laser);

        m_Model = ModelType.WeaponDrone;

        VehicleManager vehicleManager = game.GetVehicleManager();
        m_AIController = new AIController(this, vehicleManager, game.GetBulletManager(), AIBehaviours.FollowTheLeader, FireControlBehaviour.BeamSweep);
        m_AIController.SetLeader(anchor);

        m_HoverSpring = new Spring1(1.0, 0.0, m_Mass);
        m_HoverSpring.SetRelaxedPosition(5.0);

        m_CanBeTargeted = false;
    }

    @Override
    public void Update(double deltaTime)
    {
        m_AIController.Update(deltaTime);

        m_Position.J = m_HoverSpring.Update(deltaTime,  m_Position.J);

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

    }

    public void AllowFiring()
    {
        SelectWeapon(m_Laser);
    }

    public void ForbidFiring()
    {
        SelectWeapon(m_Unarmed);
    }
}