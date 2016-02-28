package com.raggamuffin.protorunnerv2.gamelogic;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.gameobjects.ExhibitionCameraAnchor;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.particles.TrailPoint;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.ColourManager;
import com.raggamuffin.protorunnerv2.managers.DatabaseManager;
import com.raggamuffin.protorunnerv2.managers.GameManager;
import com.raggamuffin.protorunnerv2.managers.GameManager_Exhibition;
import com.raggamuffin.protorunnerv2.managers.GameManager_Play;
import com.raggamuffin.protorunnerv2.managers.GameMode;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.managers.RenderEffectManager;
import com.raggamuffin.protorunnerv2.managers.GameManager_Tutorial;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.managers.GooglePlayService;
import com.raggamuffin.protorunnerv2.master.RendererPacket;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UIScreens;
import com.raggamuffin.protorunnerv2.weapons.Projectile;

public class GameLogic extends ApplicationLogic
{
	private ChaseCamera m_Camera;
	private ControlScheme m_Control;
	
	private ExhibitionCameraAnchor m_CameraAnchor;

    private ColourManager m_ColourManager;
	private DatabaseManager m_DatabaseManager;
	private GameAudioManager m_GameAudioManager;
	private PubSubHub m_PubSubHub;
	private ParticleManager m_ParticleManager;
	private BulletManager m_BulletManager;
	private VehicleManager m_VehicleManager;
	private UIManager m_UIManager;
	private RenderEffectManager m_RenderEffectManager;
	private GameStats m_GameStats;
	private SecondWindHandler m_SecondWindHandler;

    private GameManager m_GameManager;
    private GameManager_Play m_PlayManager;
    private GameManager_Tutorial m_TutorialManager;
    private GameManager_Exhibition m_ExhibitionManager;
    private GooglePlayService m_GooglePlayService;

	public GameLogic(Activity activity, PubSubHub pubSub, ControlScheme scheme, RendererPacket packet)
	{
		super(activity, packet);

		m_Camera = packet.GetCamera();
		m_Control = scheme;

        m_PubSubHub 			= pubSub;
        m_ColourManager         = new ColourManager(this);
		m_DatabaseManager 	    = new DatabaseManager(this);
		m_GameAudioManager		= new GameAudioManager(m_Context, m_Camera);
		m_ParticleManager 		= new ParticleManager(this);
		m_BulletManager 		= new BulletManager(this);
		m_VehicleManager 		= new VehicleManager(this);
		m_UIManager 			= new UIManager(this);
		m_GameStats 			= new GameStats(this);
		m_SecondWindHandler		= new SecondWindHandler(this);
        m_RenderEffectManager 	= new RenderEffectManager(this, m_Packet.GetRenderEffectSettings());
        m_GooglePlayService     = new GooglePlayService(this);

        m_CameraAnchor = new ExhibitionCameraAnchor();
        AttachCameraToAnchor();
        m_Camera.SetInPlace();

        m_PubSubHub.SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.PlayerDestroyed, new PlayerDestroyedSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.StartGame, new StartGameSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.StartTutorial, new StartTutorialSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.EndGame, new EndGameSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.TutorialComplete, new TutorialCompleteSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.HighScorePressed, new LeaderBoardPressedSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.AchievementsPressed, new AchievementsPressedSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.HighTimePressed, new HighTimeLeaderBoardPressedSubscriber());

		m_GameAudioManager.StartMusic();

        m_PlayManager = new GameManager_Play(this);
        m_TutorialManager = new GameManager_Tutorial(this);
        m_ExhibitionManager = new GameManager_Exhibition(this);
        m_GameManager = m_ExhibitionManager;
        SetGameMode(GameMode.Exhibition);
	}

	@Override
	public void Update(double deltaTime) 
	{
        m_ColourManager.Update(deltaTime);
        m_GameManager.Update(deltaTime);
		m_UIManager.Update(deltaTime);
		m_ParticleManager.Update(deltaTime);
		m_BulletManager.Update(deltaTime);
		m_VehicleManager.Update(deltaTime);
		m_RenderEffectManager.Update(deltaTime);
		m_SecondWindHandler.Update(deltaTime);
        m_GameStats.Update(deltaTime);

		CheckCollisions(deltaTime);

		m_Camera.Update(deltaTime);
	}
	
	private void CheckCollisions(double deltaTime)
	{
        ArrayList<Projectile> projectiles = m_BulletManager.GetActiveBullets();

        for(Projectile projectile : projectiles)
        {
		    ArrayList<Vehicle> vehicles = m_VehicleManager.GetVehicles();

		    for(Vehicle vehicle : vehicles)
		    {
				// Prevent friendly fire.
				if(vehicle.GetAffiliation() == projectile.GetAffiliation())
					continue;

                if(projectile.CollidesWith(vehicle))
                {
                    vehicle.CollisionResponse(projectile.GetDamageOutput());
                    projectile.CollisionResponse(vehicle);
                }
			}
		}
	}

    private void SetGameMode(GameMode mode)
    {
        m_GameManager.CleanUp();

        switch(mode)
        {
            case Play:
                m_GameManager = m_PlayManager;
                break;

            case Tutorial:
                m_GameManager = m_TutorialManager;
                break;

            case Exhibition:
                m_GameManager = m_ExhibitionManager;
                break;
        }

        m_GameManager.Initialise();
    }

    public void AddTrailToRemderer(TrailPoint point)
    {
        m_Packet.AddObject(point);
    }

    public void RemoveTrailFromRenderer(TrailPoint point)
    {
        m_Packet.RemoveObject(point);
    }

    public void AddParticleToRenderer(Particle particle)
    {
        m_Packet.AddObject(particle);
    }

    public void RemoveParticleFromRenderer(Particle particle)
    {
        m_Packet.RemoveObject(particle);
    }

    public void AddBulletToRenderer(Projectile particle)
    {
        m_Packet.AddObject(particle);
    }

    public void RemoveBulletFromRenderer(Projectile particle)
    {
        m_Packet.RemoveObject(particle);
    }

    // Adds a game object and all children of the game object to the renderer.
	public void AddObjectToRenderer(GameObject obj)
	{
        ArrayList<GameObject> Children = new ArrayList<>(); 	// A vector containing Game Objects not yet added to the Children vector.
		Children.add(obj);

		for(int c = 0; c < Children.size(); c ++)
		{
			GameObject child = Children.get(c);

            ArrayList<GameObject> Investigated = child.GetChildren();

			for(GameObject Temp : Investigated)
			{
				Children.add(Temp);
			}

            m_Packet.AddObject(child);

			Children.remove(c);
			c--;
		}
	}

	// Removes a game object and all of its children from the renderer.
	public void RemoveGameObjectFromRenderer(GameObject obj)
	{
		ArrayList<GameObject> children = new ArrayList<>(); 	// A vector containing Game Objects not yet added to the Children vector.
        children.add(obj);

		for(int c = 0; c < children.size(); c ++)
		{
			GameObject child = children.get(c);

            ArrayList<GameObject> Investigated = child.GetChildren();

			for(GameObject Temp : Investigated)
			{
                children.add(Temp);
			}

            m_Packet.RemoveObject(child);

            children.remove(c);
			c--;
		}
	}

	public void AddObjectToRenderer(UIElement element)
	{
		 m_Packet.AddUIElement(element);
	}
	
	public void RemoveObjectFromRenderer(UIElement element)
	{
        m_Packet.RemoveUIElement(element);
	}
	
	public PubSubHub GetPubSubHub()
	{
		return m_PubSubHub;
	}

	public VehicleManager GetVehicleManager()
	{
		return m_VehicleManager;
	}
	
	public BulletManager GetBulletManager()
	{
		return m_BulletManager;
	}
	
	public ParticleManager GetParticleManager()
	{
		return m_ParticleManager;
	}

	public ControlScheme GetControlScheme()
	{
		return m_Control;
	}

	public ChaseCamera GetCamera()
	{
		return m_Camera;
	}

	public void AttachCameraToAnchor()
	{
		m_Camera.Attach(m_CameraAnchor);
        m_Camera.SetUp(0,0,1);
	}
	
	public GameAudioManager GetGameAudioManager()
	{
		return m_GameAudioManager;
	}
	
	public RenderEffectManager GetRenderEffectManager()
	{
		return m_RenderEffectManager;
	}

	public UIManager GetUIManager()
	{
		return m_UIManager;
	}
	
	public GameStats GetGameStats()
	{
		return m_GameStats;
	}
	
	public SecondWindHandler GetSecondWindHandler()
	{
		return m_SecondWindHandler;
	}
	
	public DatabaseManager GetDatabaseManager()
	{
		return m_DatabaseManager;
	}

    public GameManager_Tutorial GetTutorial()
    {
        return m_TutorialManager;
    }

    public ColourManager GetColourManager()
    {
        return m_ColourManager;
    }

    public GooglePlayService GetGooglePlayService()
    {
        return m_GooglePlayService;
    }

    public void SaveScores()
    {
        m_GooglePlayService.SubmitScore(m_GameStats.GetScore());
        m_GooglePlayService.SubmitTime(m_GameStats.GetPlayTimeMillis());
        m_DatabaseManager.SaveHighScoreLocally(m_GameStats.GetScore(), m_GameStats.GetPlayTimeMillis());
    }

    private class StartGameSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_BulletManager.Wipe();
            m_VehicleManager.Wipe();
            m_GameStats.ResetStats();

            if(!m_DatabaseManager.HasTheTutorialBeenOffered())
            {
                m_UIManager.ShowScreen(UIScreens.NewToGame);
            }
            else
            {
                SetGameMode(GameMode.Play);
                m_SecondWindHandler.AutoSpawnOff();
            }
        }
    }

    private class StartTutorialSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_BulletManager.Wipe();
            m_VehicleManager.Wipe();
            m_GameStats.ResetStats();

            SetGameMode(GameMode.Tutorial);
            m_UIManager.ShowScreen(UIScreens.Tutorial);
            m_SecondWindHandler.AutoSpawnOn();
        }
    }

    private class EndGameSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            SetGameMode(GameMode.Exhibition);
            m_SecondWindHandler.Reset();
            m_GameStats.Lock();
            m_UIManager.ShowScreen(UIScreens.GameOverScreen);
            AttachCameraToAnchor();
        }
    }

    private class PlayerSpawnedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_Camera.Attach(m_VehicleManager.GetPlayer());
            m_Camera.SetUp(0, 1, 0);
            m_UIManager.ShowScreen(UIScreens.Play);
        }
    }

    private class PlayerDestroyedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_Camera.NormalCam();

            m_UIManager.ShowScreen(UIScreens.Reboot);

            ArrayList<Vehicle> wingmen = m_VehicleManager.GetTeam(AffiliationKey.BlueTeam);

            if(wingmen.size() == 0)
                return;

            m_Camera.SetLookAt(wingmen.get(0));
        }
    }

    private class TutorialCompleteSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_DatabaseManager.TutorialOffered();

            m_VehicleManager.Wipe();

            SetGameMode(GameMode.Exhibition);
            m_SecondWindHandler.Reset();
            m_GameStats.Lock();
            m_UIManager.ShowScreen(UIScreens.MainMenu);
            AttachCameraToAnchor();
        }
    }

    private class LeaderBoardPressedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_GooglePlayService.DisplayLeaderBoard();
        }
    }

    private class HighTimeLeaderBoardPressedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_GooglePlayService.DisplayHighTimeLeaderBoard();
        }
    }

    private class AchievementsPressedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_GooglePlayService.DisplayAchievements();
        }
    }

	@Override
	public void Pause()
	{
		m_GameAudioManager.Stop();
	}

	@Override
	public void Resume()
	{
		m_GameAudioManager.Resume();
	}

    @Override
    public void Destroy()
    {
        m_GooglePlayService.Disconnect();
    }
}
