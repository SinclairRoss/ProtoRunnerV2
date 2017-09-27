package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class Sensor_SurroundingAwareness extends Sensor
{	
	private ArrayList<Vehicle> m_VehiclesInWorld;

    private ArrayList<Vehicle> m_VehiclesInNeighbourhood;
    private ArrayList<Vehicle> m_FriendlyVehiclesInNeighbourhood;

    private Vector3 m_CenterOfMassFriend;
	
	public Sensor_SurroundingAwareness(Vehicle anchor, VehicleManager vManager)
	{
		super(anchor, 10.0);
		
		m_VehiclesInWorld = vManager.GetVehicles();

        m_VehiclesInNeighbourhood = new ArrayList<>();
        m_FriendlyVehiclesInNeighbourhood = new ArrayList<>();

        m_CenterOfMassFriend = new Vector3();
	}

    @Override
    public void Update()
    {
        m_VehiclesInNeighbourhood.clear();
        m_FriendlyVehiclesInNeighbourhood.clear();

        m_CenterOfMassFriend.SetVector(0);

        int numVehicles = m_VehiclesInWorld.size();
        for(int i = 0; i < numVehicles; ++i)
        {
            Vehicle object = m_VehiclesInWorld.get(i);

            if(object != m_Anchor &&
               object.GetVehicleClass() == m_Anchor.GetVehicleClass())
            {
                if (object.GetAffiliation() == m_Anchor.GetAffiliation())
                {
                    m_CenterOfMassFriend.Add(object.GetPosition());
                }

                double distanceToObjectSqr = Vector3.GetDistanceBetweenSqr(m_Anchor.GetPosition(), object.GetPosition());
                if (distanceToObjectSqr <= (m_SensorRadius * m_SensorRadius))
                {
                    m_VehiclesInNeighbourhood.add(object);
                    if (object.GetAffiliation() == m_Anchor.GetAffiliation())
                    {
                        m_FriendlyVehiclesInNeighbourhood.add(object);
                    }
                }
            }
        }

        CalculateCenterOfMassFriend();
    }

    private void CalculateCenterOfMassFriend()
    {
        int numVehiclesInNeighbourhood = m_FriendlyVehiclesInNeighbourhood.size();

        if(numVehiclesInNeighbourhood == 0)
            numVehiclesInNeighbourhood = 1;

        double scale = 1.0 / numVehiclesInNeighbourhood;
        m_CenterOfMassFriend.Scale(scale);
    }


    public ArrayList<Vehicle> GetVehiclesInNeighbourhood()
    {
        return m_VehiclesInNeighbourhood;
    }

    public ArrayList<Vehicle> GetFriendliesInNeighbourhood()
    {
        return m_FriendlyVehiclesInNeighbourhood;
    }
}
