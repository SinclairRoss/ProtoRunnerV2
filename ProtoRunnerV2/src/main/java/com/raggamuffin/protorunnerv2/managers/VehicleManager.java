package com.raggamuffin.protorunnerv2.managers;

import android.util.Log;

import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.BeeperBot;
import com.raggamuffin.protorunnerv2.gameobjects.Dummy;
import com.raggamuffin.protorunnerv2.gameobjects.SpawnEffect;
import com.raggamuffin.protorunnerv2.gameobjects.TargetBot;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.gameobjects.VehicleType;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Bit;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Carrier;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Drone;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_LaserStar;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Runner;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_ShieldBearer;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_SweeperBot;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Tank;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_TentacleController;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Warlord;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_Wingman;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle_WarlordDrone;
import com.raggamuffin.protorunnerv2.gameobjects.WeaponTestBot;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class VehicleManager
{
	private Vehicle_Runner m_Player;
	private ArrayList<Vehicle> m_Vehicles;
	private ArrayList<Vehicle> m_BlueTeam;
	private ArrayList<Vehicle> m_RedTeam;
	
	private GameLogic m_Game;

    private Vector3 m_PlayerPosition;

	private Publisher m_PlayerSpawnedPublisher;
	
	public VehicleManager(GameLogic Game)
	{
		m_Game = Game;

		m_Vehicles = new ArrayList<>();
		m_BlueTeam = new ArrayList<>();
		m_RedTeam  = new ArrayList<>();

		PubSubHub PubSub = m_Game.GetPubSubHub();
		
		m_PlayerSpawnedPublisher = PubSub.CreatePublisher(PublishedTopics.PlayerSpawned);

        m_PlayerPosition = new Vector3();
	}
	
	public void Update(double deltaTime)
	{
        if(m_Player != null)
        {
            m_PlayerPosition.SetVector(m_Player.GetPosition());
        }

        for(int i = 0; i < m_Vehicles.size(); i++)
        {
            Vehicle object = m_Vehicles.get(i);
			object.Update(deltaTime);

            if(!object.IsValid())
            {
                object.CleanUp();
                RemoveVehicle(object);
                m_Vehicles.remove(object);
                i--;
            }
        }
	}

	// Removes vehicle from their team and the renderer.
	// DOES NOT remove vehicle from m_Vehicles.
	private void RemoveVehicle(Vehicle object)
	{		
		m_Game.RemoveObjectFromRenderer(object);
		GetTeam(object.GetAffiliation()).remove(object);
		
		if(object == m_Player)
		{
			m_Player = null;
		}
	}

	public void SpawnPlayer()
	{
		if(m_Player == null)
		{
			m_Player = new Vehicle_Runner(m_Game);
			m_Vehicles.add(m_Player);
			m_BlueTeam.add(m_Player);
			m_Game.AddObjectToRenderer(m_Player);

			m_PlayerSpawnedPublisher.Publish();
		}
	}

    public Vehicle SpawnVehicle(VehicleType type, double x, double z, double orientation)
    {
        Vehicle spawn;

        switch(type)
        {
            case Wingman:
                spawn = new Vehicle_Wingman(m_Game);
                break;
            case Bit:
                spawn = new Vehicle_Bit(m_Game);
                break;
            case Tank:
                spawn = new Vehicle_Tank(m_Game);
                break;
            case Carrier:
                spawn = new Vehicle_Carrier(m_Game);
                break;
			case LaserStar:
				spawn = new Vehicle_LaserStar(m_Game);
				break;
            case ShieldBearer:
                spawn = new Vehicle_ShieldBearer(m_Game);
                break;
            case Warlord:
                spawn = new Vehicle_Warlord(m_Game);
                break;
            case WeaponTestBot:
                spawn = new WeaponTestBot(m_Game);
                break;
            case TargetBot:
                spawn = new TargetBot(m_Game);
                break;
			case TrainingDummy:
				spawn = new Dummy(m_Game);
				break;
			case BeeperBot:
				spawn = new BeeperBot(m_Game);
				break;
			case SweeperBot:
				spawn = new Vehicle_SweeperBot(m_Game);
				break;
            default:
                Log.e("VehicleManager.java", "Vehicle type: '" + type + "' not found.");
                return null;
        }

        spawn.SetPosition(x, 0, z);
		spawn.RotateY(orientation);
        m_Vehicles.add(spawn);
        GetTeam(spawn.GetAffiliation()).add(spawn);
        m_Game.AddObjectToRenderer(spawn);

		SpawnEffect effect = new SpawnEffect(spawn.GetColour(), spawn.GetPosition());
        m_Game.GetGameObjectManager().AddObject(effect);

        return spawn;
    }

	public Vehicle_Drone SpawnDrone(Vehicle_Carrier anchor)
	{
		Vehicle_Drone spawn = new Vehicle_Drone(m_Game, anchor);

		m_Vehicles.add(spawn);
		GetTeam(spawn.GetAffiliation()).add(spawn);
		m_Game.AddObjectToRenderer(spawn);

		return spawn;
	}

	public Vehicle_WarlordDrone SpawnDrone(Vehicle_Warlord anchor, int droneNumber, int maxDroneCount)
	{
		Vehicle_WarlordDrone spawn = new Vehicle_WarlordDrone(m_Game, anchor, droneNumber, maxDroneCount);

		m_Vehicles.add(spawn);
		GetTeam(spawn.GetAffiliation()).add(spawn);
		m_Game.AddObjectToRenderer(spawn);

		return spawn;
	}

	public Vehicle_TentacleController SpawnTentacleController(Vehicle_ShieldBearer anchor)
    {
        Vehicle_TentacleController spawn = new Vehicle_TentacleController(m_Game, anchor, anchor.GetTentacleRange());

        m_Vehicles.add(spawn);
        GetTeam(spawn.GetAffiliation()).add(spawn);
		m_Game.AddObjectToRenderer(spawn);

        return spawn;
    }

	public ArrayList<Vehicle> GetTeam(AffiliationKey faction)
	{
		switch(faction)
		{
			case RedTeam:
			{
				return m_RedTeam;
			}
			case BlueTeam:
			{
				return m_BlueTeam;
			}
			case Neutral:
			default:
			{
				return null;
			}
		}
	}
	
	public ArrayList<Vehicle> GetOpposingTeam(AffiliationKey faction)
	{
		switch(faction)
		{
			case RedTeam:
			{
				return m_BlueTeam;
			}
			case BlueTeam:
			{
				return m_RedTeam;
			}
			case Neutral:
			default:
			{
				return null;
			}
		}
	}

    public int GetTeamCount(AffiliationKey faction)
    {
        switch(faction)
		{
			case BlueTeam:
			{
				return m_BlueTeam.size();
			}
			case RedTeam:
			{
				return m_RedTeam.size();
			}
			default:
			{
				return 0;
			}
        }
    }
	
	public void Wipe()
	{
		for(int i = 0; i < m_Vehicles.size(); ++i)
		{
			Vehicle object = m_Vehicles.get(i);

			object.CleanUp();
			RemoveVehicle(object);
			m_Vehicles.remove(object);
			--i;
		}
		
        m_PlayerPosition.SetVector(0);

		Update(0);
	}
		
	public Vehicle_Runner GetPlayer()
	{
		return m_Player;
	}
	
	public ArrayList<Vehicle> GetVehicles()
	{
		return m_Vehicles;
	}

    public Vector3 GetPlayerPosition()
    {
        return m_PlayerPosition;
    }
}
