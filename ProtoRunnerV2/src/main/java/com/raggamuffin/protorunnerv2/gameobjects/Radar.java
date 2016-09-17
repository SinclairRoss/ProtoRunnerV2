package com.raggamuffin.protorunnerv2.gameobjects;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.CollisionDetection;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Radar extends GameObject
{
	private final double MIN_DEPTH = -2.5;
	private final double MAX_DEPTH = -2.0;
	private final double FRAGMENT_SCALE = 0.5;
	private final double FRAGMENT_PADDING = 0.1;
	private final double RANGE = 150.0;
	private final double NORMALISED_FRAGMENT_SIZE;
	private final int RESOLUTION = 7;
	
	private ArrayList<RadarFragment> m_RadarFragments;
	private ArrayList<Vehicle> m_Vehicles;
	
	private Vector3 m_ToVehicle;
	private Vehicle m_Anchor;

	public Radar(Vehicle anchor, GameLogic game)
	{
		super(game, ModelType.Nothing);

		m_RadarFragments = new ArrayList<>();
		m_Vehicles = game.GetVehicleManager().GetVehicles();
		m_Anchor = anchor;
		m_ToVehicle = new Vector3();
		
		double offset = ((RESOLUTION / 2) * (FRAGMENT_SCALE + FRAGMENT_PADDING));
		NORMALISED_FRAGMENT_SIZE = ((FRAGMENT_SCALE + FRAGMENT_PADDING) / (RESOLUTION * (FRAGMENT_SCALE + FRAGMENT_PADDING))) * 1.0;

		double size = RESOLUTION * (FRAGMENT_SCALE + FRAGMENT_PADDING) * 0.5;
		double radius = Math.sqrt((size * size) + (size * size));
		
		for(int x = 0; x < RESOLUTION; x++)
		{
			for(int y = 0; y < RESOLUTION; y++)
			{	
				double posX = (x * (FRAGMENT_SCALE + FRAGMENT_PADDING)) - offset;
				double posY = (y * (FRAGMENT_SCALE + FRAGMENT_PADDING)) - offset;

				RadarFragment fragment = new RadarFragment(game, MIN_DEPTH, MAX_DEPTH, posX, posY, radius);

				fragment.SetScale(FRAGMENT_SCALE);
				
				m_RadarFragments.add(fragment);
				AddChild(fragment);
			}
		}
	}
	
	@Override
	public void Update(double deltaTime)
	{
		super.Update(deltaTime);

        ResetRadar();
        ScanForSignatures();
        UpdateFragments(deltaTime);
	}

    private void ResetRadar()
    {
        for(RadarFragment fragment : m_RadarFragments)
        {
            fragment.Reset();
        }
    }

    private void ScanForSignatures()
    {
        for(Vehicle vehicle : m_Vehicles)
        {
            if(vehicle.GetVehicleClass() == VehicleClass.Drone)
                continue;

            m_ToVehicle.SetVectorDifference(m_Position, vehicle.GetPosition());

            if(m_ToVehicle.GetLengthSqr() < RANGE * RANGE)  // Is within range.
            {
                m_ToVehicle.Scale(1 / RANGE);  // Transform into radar space.

                for (RadarFragment fragment : m_RadarFragments)
                {
                    if(fragment.GetRadarSignature() != RadarSignatureType.Friendly) // Ensures friendly signatures take priority.
                    {
                        if (CollisionDetection.RadarDetection(fragment.GetNormalisedRadarPosition(), m_ToVehicle, NORMALISED_FRAGMENT_SIZE))
                        {
                            fragment.SetSignatureType(GetSignatureType(vehicle));
                            fragment.HeatUp();

                            break;
                        }
                    }
                }
            }
        }
    }

    private void UpdateFragments(double deltaTime)
    {
        for(RadarFragment fragment : m_RadarFragments)
        {
            fragment.Update(deltaTime);
        }
    }

    @Override
    protected void UpdateChildren(double deltaTime)
    {
        ArrayList<GameObject> children = GetChildren();

        for(GameObject child : children)
        {
            Vector3 pos = child.GetPosition();
            pos.I = m_Position.I;
            pos.K = m_Position.K;

            child.Update(deltaTime);
        }
    }

    private RadarSignatureType GetSignatureType(Vehicle vehicle)
	{
        AffiliationKey signatureAffiliation = vehicle.GetAffiliation();

        switch (signatureAffiliation)
        {
            case BlueTeam:
                return RadarSignatureType.Friendly;
            case RedTeam:
                return RadarSignatureType.Foe;
            case Neutral:
                return RadarSignatureType.None;
        }

		return RadarSignatureType.None;
	}

    @Override
    public boolean IsValid()
    {
        return true;
    }
}
