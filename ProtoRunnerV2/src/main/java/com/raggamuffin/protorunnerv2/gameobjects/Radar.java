package com.raggamuffin.protorunnerv2.gameobjects;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
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
		super(game.GetPubSubHub(), game.GetGameAudioManager());
		
		m_Model = ModelType.Nothing;
		m_RadarFragments = new ArrayList<RadarFragment>();
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
		
		CalmRadar();
		
		for(Vehicle vehicle : m_Vehicles)
		{	
			m_ToVehicle.SetVectorDifference(m_Position, vehicle.GetPosition());

			// Is within range.
			if(m_ToVehicle.GetLengthSqr() > RANGE * RANGE)
				continue;

			// Transform into radar space.
			m_ToVehicle.Scale(1 / RANGE);	

			for(RadarFragment fragment : m_RadarFragments)
			{
				if(CollisionDetection.RadarDetection(fragment.GetNormalisedRadarPosition(), m_ToVehicle, NORMALISED_FRAGMENT_SIZE))				
				{	
					fragment.SetSignitureType(GetSignatureType(vehicle));
					fragment.HeatUp();
				}
			}
		}
		
		for(RadarFragment fragment : m_RadarFragments)
		{
			fragment.Update(deltaTime);			
		}
	}
	
	private void CalmRadar()
	{
		for(RadarFragment fragment : m_RadarFragments)
		{
			fragment.CoolDown();
		}
	}

	@Override
	public boolean IsValid() 
	{
		return true;
	}
	
	private RadarSignatureType GetSignatureType(Vehicle vehicle)
	{
		if(vehicle == m_Anchor)
			return RadarSignatureType.Player;
		
		if(vehicle.GetAffiliation() == AffiliationKey.BlueTeam)
			return RadarSignatureType.Wingman;
		
		if(vehicle.GetAffiliation() == AffiliationKey.RedTeam)
			return RadarSignatureType.Enemy;
		
		return RadarSignatureType.None;
	}
}
