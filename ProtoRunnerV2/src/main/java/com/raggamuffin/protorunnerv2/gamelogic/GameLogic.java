package com.raggamuffin.protorunnerv2.gamelogic;

import java.util.ArrayList;

import android.app.Activity;

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.gameobjects.ExhibitionCameraAnchor;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Tentacle;
import com.raggamuffin.protorunnerv2.managers.GameManager_Test;
import com.raggamuffin.protorunnerv2.managers.GameObjectManager;
import com.raggamuffin.protorunnerv2.managers.RopeManager;
import com.raggamuffin.protorunnerv2.managers.InGameSoundEffectsManager;
import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.particles.TrailNode;
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
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UIScreens;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
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
    private GameObjectManager m_GameObjectManager;
    private RopeManager m_RopeManager;
	private UIManager m_UIManager;
	private RenderEffectManager m_RenderEffectManager;
	private GameStats m_GameStats;
	private SecondWindHandler m_SecondWindHandler;
    private InGameSoundEffectsManager m_SFXManager;

    private GameManager m_GameManager;
    private final GameManager_Play m_PlayManager;
    private final GameManager_Tutorial m_TutorialManager;
    private final GameManager_Exhibition m_ExhibitionManager;
    private final GameManager_Test m_TestManager;
    private final GooglePlayService m_GooglePlayService;

    private Publisher m_GameReadyPublisher;

    private GameMode m_GameMode;

    public static boolean TEST_MODE = false;

	public GameLogic(Activity activity, PubSubHub pubSub, ControlScheme scheme, RendererPacket packet)
	{
		super(activity, packet);

		m_Camera = packet.GetCamera();
		m_Control = scheme;

        m_PubSubHub = pubSub;
        m_ColourManager = new ColourManager(this);
		m_DatabaseManager= new DatabaseManager(this);
		m_GameAudioManager = new GameAudioManager(m_Context, m_Camera);
        m_SFXManager = new InGameSoundEffectsManager(this);
		m_ParticleManager = new ParticleManager(this);
		m_BulletManager = new BulletManager(this);
		m_VehicleManager = new VehicleManager(this);
        m_GameObjectManager = new GameObjectManager(this);
        m_RopeManager = new RopeManager(this);
		m_UIManager = new UIManager(this);
		m_GameStats = new GameStats(this);
		m_SecondWindHandler	= new SecondWindHandler(this);
        m_RenderEffectManager = new RenderEffectManager(this, m_Packet.GetRenderEffectSettings());
        m_GooglePlayService = new GooglePlayService(this);

        m_CameraAnchor = new ExhibitionCameraAnchor(this);
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
        m_PubSubHub.SubscribeToTopic(PublishedTopics.StartTest, new StartTestSubscriber());

        m_GameReadyPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.GameReady);

        m_PlayManager = new GameManager_Play(this);
        m_TutorialManager = new GameManager_Tutorial(this);
        m_ExhibitionManager = new GameManager_Exhibition(this);
        m_TestManager = new GameManager_Test(this);
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
        m_GameObjectManager.Update(deltaTime);
        m_RopeManager.Update(deltaTime);
		m_RenderEffectManager.Update(deltaTime);
		m_SecondWindHandler.Update(deltaTime);
        m_GameStats.Update(deltaTime);

		CheckCollisions();

		m_Camera.Update(deltaTime);
	}
	
	private void CheckCollisions()
	{
        ArrayList<Projectile> projectiles = m_BulletManager.GetActiveBullets();

        for(int i = 0; i < projectiles.size(); ++i)
        {
            Projectile projectile = projectiles.get(i);
		    ArrayList<Vehicle> vehicles = m_VehicleManager.GetVehicles();

		    for(Vehicle vehicle : vehicles)
		    {
                // Prevent friendly fire.
                if (vehicle.GetAffiliation() != projectile.GetAffiliation())
                {
                    CollisionReport report = projectile.CheckForCollision(vehicle);
                    if (report != null)
                    {
                        vehicle.CollisionResponse(projectile.GetDamageOutput());
                        projectile.CollisionResponse(report);
                    }
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
            case Test:
                m_GameManager = m_TestManager;
                break;
        }

        m_GameMode = mode;
        m_GameManager.Initialise();
    }

    public void AddTrailToRenderer(TrailNode point)
    {
        m_Packet.AddObject(point);
    }

    public void AddRopeToRenderer(Tentacle tentacle)
    {
        m_Packet.AddObject(tentacle);
    }

    public void RemoveTrailFromRenderer(TrailNode point)
    {
        m_Packet.RemoveObject(point);
    }

    public void RemoveRopeFromRenderer(Tentacle tentacle)
    {
        m_Packet.RemoveObject(tentacle);
    }

    public void AddParticleToRenderer(Particle particle)
    {
        m_Packet.AddObject(particle);
    }

    public void RemoveParticleFromRenderer(Particle particle)
    {
        m_Packet.RemoveObject(particle);
    }

    // Adds a game object and all children of the game object to the renderer.
	public void AddObjectToRenderer(GameObject obj)
	{
        m_Packet.AddObject(obj);
	}

	// Removes a game object and all of its children from the renderer.
	public void RemoveGameObjectFromRenderer(GameObject obj)
	{
        m_Packet.RemoveObject(obj);
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

    public GameObjectManager GetGameObjectManager()
    {
        return m_GameObjectManager;
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

    public RopeManager GetRopeManager()
    {
        return m_RopeManager;
    }
	
	public GameAudioManager GetGameAudioManager()
	{
		return m_GameAudioManager;
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
            m_GameObjectManager.Wipe();
            m_GameStats.ResetStats();

            if(!m_DatabaseManager.HasTheTutorialBeenOffered())
            {
                m_UIManager.ShowScreen(UIScreens.NewToGame);
            }
            else
            {
                SetGameMode(GameMode.Play);
                m_UIManager.ShowScreen(UIScreens.Play);

                m_SecondWindHandler.AutoSpawnOff();
                m_GameReadyPublisher.Publish();
            }
        }
    }

    private class StartTestSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_BulletManager.Wipe();
            m_VehicleManager.Wipe();
            m_GameStats.ResetStats();

            SetGameMode(GameMode.Test);
            m_SecondWindHandler.AutoSpawnOff();
            m_UIManager.ShowScreen(UIScreens.TestMode);
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
        }
    }

    private class PlayerDestroyedSubscriber extends Subscriber
    {
        @Override
        public void Update(int args)
        {
            m_Camera.NormalCam();

            //m_UIManager.ShowScreen(UIScreens.Reboot);

            ArrayList<Vehicle> wingmen = m_VehicleManager.GetTeam(AffiliationKey.BlueTeam);

            if(wingmen.size() > 0)
            {
                m_Camera.SetLookAt(wingmen.get(0));
            }
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
		m_GameAudioManager.Pause();
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
        m_GameAudioManager.Stop();
    }

    public GameMode GetGameMode()
    {
        return m_GameMode;
    }
}
