package com.raggamuffin.protorunnerv2.gameobjects;

// Author: Sinclair Ross
// Date:   08/04/2017

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.particles.TrailEmitter;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Vehicle_WarlordDrone extends Vehicle
{
    private final double ORBIT_RADIUS = 15;
    private final double ORBIT_SPEED = 1;

    private Vehicle_Warlord m_Anchor;
    private double m_OrbitCounter;

    private TrailEmitter m_TrailEmitter;
    private Tentacle m_Tentacle;

    public Vehicle_WarlordDrone(GameLogic game, Vehicle_Warlord anchor, int droneNumber, int maxDroneCount)
    {
        super(game, ModelType.EngineDrone, 5.0);

        m_Anchor = anchor;

        SetColour(m_Anchor.GetColour());
        m_TrailEmitter = new TrailEmitter(game, this);
        m_OrbitCounter = ((Math.PI * 2) / maxDroneCount) * droneNumber;

        SetAffiliation(AffiliationKey.RedTeam);

        m_HullPoints = 1;

        UpdatePosition(0);

        SpawnEffect effect = new SpawnEffect(GetColour(), GetPosition());
        game.GetGameObjectManager().AddObject(effect);

        m_Tentacle = new Tentacle(this, m_Anchor, GetColour(), GetColour(), 10, 9000, 30);
        game.GetRopeManager().AddObject(m_Tentacle);

        SetVehicleClass(VehicleClass.Drone);
    }

    public void Update(double deltaTime)
    {
        UpdatePosition(deltaTime);
        UpdateRotation();

        m_TrailEmitter.Update();
    }

    private void UpdatePosition(double deltaTime)
    {
        m_OrbitCounter += ORBIT_SPEED * deltaTime;
        m_OrbitCounter %= Math.PI * 2;

        SetPosition(ORBIT_RADIUS, 0, 0);
        GetPosition().RotateY(m_OrbitCounter);
        Vector3 anchorPos = m_Anchor.GetPosition();
        Translate(anchorPos.X, 0, anchorPos.Z);
    }

    private void UpdateRotation()
    {
        Vector3 forward = GetForward();
        forward.SetAsDifference(GetPosition(), m_Anchor.GetPosition());
        forward.Y = 0;
        forward.Normalise();
        RotateY(Math.PI / 2);
        SetForward(forward);
    }

    @Override
    public void CleanUp()
    {
        super.CleanUp();
        m_Tentacle.KillTentacle();
    }
}