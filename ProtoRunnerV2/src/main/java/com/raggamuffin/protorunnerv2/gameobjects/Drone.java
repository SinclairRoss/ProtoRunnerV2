package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Spring1;
import com.raggamuffin.protorunnerv2.weapons.LaserBurner;

public class Drone extends Vehicle
{
    private Carrier m_Anchor;
    private AIController m_AIController;

    protected Spring1 m_HoverSpring;

    public Drone(GameLogic game, Carrier anchor)
    {
        super(game);

        m_Anchor = anchor;

        m_BaseColour = anchor.GetBaseColour();
        m_AltColour = anchor.GetAltColour();

        m_Engine = new Engine_Standard(this, game, new EngineUseBehaviour_Null());
        m_Engine.SetMaxTurnRate(2.0);
        m_Engine.SetMaxEngineOutput(3000);
        m_Mass = 100;

        SelectWeapon(new LaserBurner(this, game));

        m_Model = ModelType.WeaponDrone;

        SetAffiliation(anchor.GetAffiliation());

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
        return m_Anchor.IsValid();
    }
}