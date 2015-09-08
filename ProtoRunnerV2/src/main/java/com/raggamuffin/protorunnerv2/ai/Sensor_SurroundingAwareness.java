package com.raggamuffin.protorunnerv2.ai;

import java.util.ArrayList;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class Sensor_SurroundingAwareness extends Sensor
{	
	private ArrayList<Vehicle> m_VehiclesInWorld;

    private ArrayList<Vehicle> m_VehiclesInNeighbourhood;
    private ArrayList<Vehicle> m_FriendlyVehiclesInNeighbourhood;
    private ArrayList<Vehicle> m_EnemyVehiclesInNeighbourhood;

    private Vector3 m_CenterOfMassEnemy;
    private Vector3 m_CenterOfMassFriend;
	
	private Vector3 m_TempVector;
	
	public Sensor_SurroundingAwareness(Vehicle anchor, VehicleManager vManager)
	{
		super(anchor, 10.0);
		
		m_VehiclesInWorld = vManager.GetVehicles();

        m_VehiclesInNeighbourhood = new ArrayList<>();
        m_FriendlyVehiclesInNeighbourhood = new ArrayList<>();
        m_EnemyVehiclesInNeighbourhood = new ArrayList<>();

        m_CenterOfMassFriend = new Vector3();
        m_CenterOfMassEnemy  = new Vector3();

		m_TempVector = new Vector3();
	}

	@Override
	public void Update() 
	{
        m_VehiclesInNeighbourhood.clear();
        m_FriendlyVehiclesInNeighbourhood.clear();
        m_EnemyVehiclesInNeighbourhood.clear();

        m_CenterOfMassFriend.SetVector(0);
        m_CenterOfMassEnemy.SetVector(0);
		
		// Iterate through all minions.
		for(Vehicle object : m_VehiclesInWorld)
		{
			if(object == m_Anchor)
				continue;

			m_TempVector.SetVectorDifference(m_Anchor.GetPosition(), object.GetPosition());

            if (object.GetAffiliation() == m_Anchor.GetAffiliation())
            {
                m_CenterOfMassFriend.Add(object.GetPosition());

                if(m_TempVector.GetLengthSqr() <= (m_SensorRadius * m_SensorRadius))
                {
                    m_VehiclesInNeighbourhood.add(object);
                    m_FriendlyVehiclesInNeighbourhood.add(object);
                }
            }
            else
            {
                m_CenterOfMassEnemy.Add(object.GetPosition());

                if(m_TempVector.GetLengthSqr() <= (m_SensorRadius * m_SensorRadius))
                {
                    m_VehiclesInNeighbourhood.add(object);
                    m_EnemyVehiclesInNeighbourhood.add(object);
                }
            }
		}

        double scale;
        int size;

        size = m_FriendlyVehiclesInNeighbourhood.size();

        if(size == 0)
            size = 1;

        scale = 1.0 / size;
        m_CenterOfMassFriend.Scale(scale);

        size = m_EnemyVehiclesInNeighbourhood.size();

        if(size == 0)
            size = 1;

        scale = 1.0 / size;
        m_CenterOfMassEnemy.Scale(scale);
	}

    public ArrayList<Vehicle> GetVehiclesInNeighbourhood()
    {
        return m_VehiclesInNeighbourhood;
    }

    public ArrayList<Vehicle> GetFriendliesInNeighbourhood()
    {
        return m_FriendlyVehiclesInNeighbourhood;
    }

    public ArrayList<Vehicle> GetEnemiesInNeighbourhood()
    {
        return m_EnemyVehiclesInNeighbourhood;
    }

    public Vector3 GetCenterOfMassEnemy()
    {
        return m_CenterOfMassEnemy;
    }

    public Vector3 GetCenterOfMassFriend()
    {
        return m_CenterOfMassFriend;
    }
}
