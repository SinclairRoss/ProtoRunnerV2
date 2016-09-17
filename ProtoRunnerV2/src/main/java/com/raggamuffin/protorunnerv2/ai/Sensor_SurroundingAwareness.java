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
	
	public Sensor_SurroundingAwareness(Vehicle anchor, VehicleManager vManager)
	{
		super(anchor, 10.0);
		
		m_VehiclesInWorld = vManager.GetVehicles();

        m_VehiclesInNeighbourhood = new ArrayList<>();
        m_FriendlyVehiclesInNeighbourhood = new ArrayList<>();
        m_EnemyVehiclesInNeighbourhood = new ArrayList<>();

        m_CenterOfMassFriend = new Vector3();
        m_CenterOfMassEnemy  = new Vector3();
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
            if(object != m_Anchor &&
                object.GetVehicleClass() == m_Anchor.GetVehicleClass())
            {
                if (object.GetAffiliation() == m_Anchor.GetAffiliation())
                {
                    m_CenterOfMassFriend.Add(object.GetPosition());

                    double distanceToObjectSqr = Vector3.GetDistanceBetweenSqr(m_Anchor.GetPosition(), object.GetPosition());
                    if (distanceToObjectSqr <= (m_SensorRadius * m_SensorRadius))
                    {
                        m_VehiclesInNeighbourhood.add(object);
                        m_FriendlyVehiclesInNeighbourhood.add(object);
                    }
                }
                else
                {
                    m_CenterOfMassEnemy.Add(object.GetPosition());

                    double distanceToObjectSqr = Vector3.GetDistanceBetweenSqr(m_Anchor.GetPosition(), object.GetPosition());
                    if (distanceToObjectSqr <= (m_SensorRadius * m_SensorRadius))
                    {
                        m_VehiclesInNeighbourhood.add(object);
                        m_EnemyVehiclesInNeighbourhood.add(object);
                    }
                }
            }
        }

        CalculateCenterOfMassFriend();
        CalculateCenterOfMassEnemy();
    }

    private void CalculateCenterOfMassFriend()
    {
        int numVehiclesInNeighbourhood = m_FriendlyVehiclesInNeighbourhood.size();

        if(numVehiclesInNeighbourhood == 0)
            numVehiclesInNeighbourhood = 1;

        double scale = 1.0 / numVehiclesInNeighbourhood;
        m_CenterOfMassFriend.Scale(scale);
    }

    private void CalculateCenterOfMassEnemy()
    {
        int numVehiclesInNeighbourhood = m_EnemyVehiclesInNeighbourhood.size();

        if(numVehiclesInNeighbourhood == 0)
            numVehiclesInNeighbourhood = 1;

        double scale = 1.0 / numVehiclesInNeighbourhood;
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
