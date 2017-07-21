package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.ai.AIBehaviours;
import com.raggamuffin.protorunnerv2.ai.AIController;
import com.raggamuffin.protorunnerv2.ai.FireControlBehaviour;
import com.raggamuffin.protorunnerv2.ai.NavigationalBehaviourInfo;
import com.raggamuffin.protorunnerv2.ai.TargetingBehaviour;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Spring1;
import com.raggamuffin.protorunnerv2.utils.Timer;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Weapon_LaserBurner;

public class Vehicle_Drone extends Vehicle
{
    private final double MIN_RESTING_HEIGHT = 4.0;
    private final double MAX_RESTING_HEIGHT = 8.0;

    private Vehicle_Carrier m_Anchor;
    private AIController m_AIController;

    private Spring1 m_VerticalPositionSpring;
    private Timer m_Timer_UpdateRelaxedPosition;
    private double m_RestingHeight;

    public Vehicle_Drone(GameLogic game, Vehicle_Carrier anchor)
    {
        super(game, ModelType.WeaponDrone, anchor.GetPosition(), 1, 1, VehicleClass.Drone, false, null, AffiliationKey.RedTeam);

        m_Anchor = anchor;
        SetPosition(m_Anchor.GetPosition());

        SetColour(m_Anchor.GetColour());

        m_Engine = new Engine_Standard(this, game);
        m_Engine.SetMaxTurnRate(2.0);
        m_Engine.SetMaxEngineOutput(30);
        m_Engine.SetDodgeOutput(0);

        SetScale(0.75, 0.75, 0.75);

        SetDragCoefficient(0.8);

        SelectWeapon(new Weapon_LaserBurner(this, game));
       // m_PrimaryWeapon.SetTargetVector(Vector3.DOWN);
      //  m_PrimaryWeapon.PullTrigger();

        VehicleManager vehicleManager = game.GetVehicleManager();

        NavigationalBehaviourInfo navInfo = new NavigationalBehaviourInfo(0.4, 1.0, 0.7, 0.6);
        m_AIController = new AIController(this, vehicleManager, game.GetBulletManager(), navInfo, AIBehaviours.FollowTheLeader, FireControlBehaviour.None, TargetingBehaviour.None);
        m_AIController.SetLeader(anchor);

        m_VerticalPositionSpring = new Spring1(10);
        m_Timer_UpdateRelaxedPosition = new Timer(2);
        m_Timer_UpdateRelaxedPosition.Start();
    }

    @Override
    public void DrainEnergy(double drain)
    {}

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
        return m_Anchor.IsValid();
    }

    private void UpdateBobbing(double deltaTime)
    {
        if(m_Timer_UpdateRelaxedPosition.HasElapsed())
        {
            m_Timer_UpdateRelaxedPosition.Start();
            m_RestingHeight = MathsHelper.RandomDouble(MIN_RESTING_HEIGHT, MAX_RESTING_HEIGHT);
        }

        double force = m_VerticalPositionSpring.CalculateSpringForce(GetPosition().Y, m_RestingHeight);
        ApplyForce(Vector3.UP, force * deltaTime);
    }

    @Override
    public double GetInnerColourIntensity() { return m_Anchor.GetInnerColourIntensity(); }
}