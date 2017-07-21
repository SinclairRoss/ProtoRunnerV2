package com.raggamuffin.protorunnerv2.ai;

// Author: Sinclair Ross
// Date:   06/08/2016

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.gameobjects.VehicleClass;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class AIBehaviour_StickWithThePack extends AIBehaviour
{
    private ArrayList<Vehicle> m_Friendlies;

    private AIBehaviour m_BackupBehaviour;
    private Vector3 m_Goal;

    private VehicleManager m_VehicleManager;

    public AIBehaviour_StickWithThePack(AIController controller, VehicleManager vManager)
    {
        super(controller);

        m_Goal = new Vector3();
        m_BackupBehaviour = new AIBehaviour_Encircle(controller);

        m_VehicleManager = vManager;

        m_Friendlies = new ArrayList<>();
    }

    @Override
    public Vector3 GetNavigationCoordinates()
    {
        UpdateFriendlyList();

        if(m_Friendlies.size() > 0)
        {
            m_Goal.SetVector(0);

            int friendlyCount = m_Friendlies.size();
            for(int i = 0; i < friendlyCount; ++i)
            {
                Vehicle friendly = m_Friendlies.get(i);
                m_Goal.Add(friendly.GetPosition());
            }

            double scalar = 1 / (m_Friendlies.size());
            m_Goal.Scale(scalar);
        }
        else
        {
            m_Goal.SetVector(m_BackupBehaviour.GetNavigationCoordinates());
        }

        return m_Goal;
    }

    private void UpdateFriendlyList()
    {
        m_Friendlies.clear();

        ArrayList<Vehicle> friendlies = m_VehicleManager.GetTeam(m_Controller.GetTeamAffiliationKey());

        int numFriendlies = friendlies.size();
        for(int i = 0; i < numFriendlies; ++i)
        {
            Vehicle candidate = friendlies.get(i);
            if(candidate.GetVehicleClass() == VehicleClass.StandardVehicle
                    && candidate != m_Controller.GetAnchor())
            {
                m_Friendlies.add(candidate);
            }
        }
    }
}
