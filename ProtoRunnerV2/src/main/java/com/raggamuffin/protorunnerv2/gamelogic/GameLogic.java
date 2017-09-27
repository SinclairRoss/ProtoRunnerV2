package com.raggamuffin.protorunnerv2.gamelogic;

import android.app.Activity;

import com.raggamuffin.protorunnerv2.ObjectEffect.ObjectEffect;
import com.raggamuffin.protorunnerv2.ObjectEffect.ObjectEffectController;
import com.raggamuffin.protorunnerv2.ObjectEffect.ObjectEffectType;
import com.raggamuffin.protorunnerv2.ObjectEffect.ObjectEffect_HealthBar;
import com.raggamuffin.protorunnerv2.RenderObjects.RenderObject_HealthBar;
import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.gameobjects.ExhibitionCameraAnchor;
import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.managers.BulletManager;
import com.raggamuffin.protorunnerv2.managers.DatabaseManager;
import com.raggamuffin.protorunnerv2.managers.GameManager;
import com.raggamuffin.protorunnerv2.managers.GameManager_Exhibition;
import com.raggamuffin.protorunnerv2.managers.GameManager_Play;
import com.raggamuffin.protorunnerv2.managers.GameManager_Quiet;
import com.raggamuffin.protorunnerv2.managers.GameManager_Test;
import com.raggamuffin.protorunnerv2.managers.GameMode;
import com.raggamuffin.protorunnerv2.managers.GameObjectManager;
import com.raggamuffin.protorunnerv2.managers.GooglePlayService;
import com.raggamuffin.protorunnerv2.managers.InGameSoundEffectsManager;
import com.raggamuffin.protorunnerv2.managers.ParticleManager;
import com.raggamuffin.protorunnerv2.managers.RenderEffectManager;
import com.raggamuffin.protorunnerv2.managers.RopeManager;
import com.raggamuffin.protorunnerv2.managers.TrailManager;
import com.raggamuffin.protorunnerv2.managers.UIManager;
import com.raggamuffin.protorunnerv2.managers.VehicleManager;
import com.raggamuffin.protorunnerv2.master.ControlScheme;
import com.raggamuffin.protorunnerv2.RenderObjects.RenderPackage;
import com.raggamuffin.protorunnerv2.master.RenderPackageDistributor;
import com.raggamuffin.protorunnerv2.master.RendererPacket;
import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.particles.Trail;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;
import com.raggamuffin.protorunnerv2.pubsub.Subscriber;
import com.raggamuffin.protorunnerv2.ui.UIElement_Label;
import com.raggamuffin.protorunnerv2.ui.UIScreens;
import com.raggamuffin.protorunnerv2.utils.CollisionReport;
import com.raggamuffin.protorunnerv2.utils.StopWatch;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.weapons.Projectile;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameLogic extends ApplicationLogic
{
    public static GameLogic DEBUG_INSTANCE = null;

	private ChaseCamera m_Camera;
	private ControlScheme m_Control;
	
	private ExhibitionCameraAnchor m_CameraAnchor;

	private DatabaseManager m_DatabaseManager;
	private GameAudioManager m_GameAudioManager;
	private PubSubHub m_PubSubHub;
	private ParticleManager m_ParticleManager;
	private BulletManager m_BulletManager;
	private VehicleManager m_VehicleManager;
    private GameObjectManager m_GameObjectManager;
    private RopeManager m_RopeManager;
    private TrailManager m_TrailManager;
	private UIManager m_UIManager;
	private RenderEffectManager m_RenderEffectManager;
	private GameStats m_GameStats;
    private InGameSoundEffectsManager m_SFXManager;
    private ObjectEffectController m_ObjectEffectController;

    private GameManager m_GameManager;
    private final GameManager_Play m_PlayManager;
    private final GameManager_Exhibition m_ExhibitionManager;
    private final GameManager_Quiet m_QuietManager;
    private final GameManager_Test m_TestManager;
    private final GooglePlayService m_GooglePlayService;

    private Publisher m_GameReadyPublisher;
    private Publisher m_GameOverPublisher;

    private GameMode m_GameMode;

    public static boolean TEST_MODE = false;

    private RenderPackageDistributor m_Distributor;

    private ArrayList<GameObject> m_MiscObjects;

	public GameLogic(Activity activity, PubSubHub pubSub, ControlScheme scheme, RendererPacket packet, RenderPackageDistributor distributer)
	{
		super(activity, packet);

        DEBUG_INSTANCE = this;

		m_Camera = packet.GetCamera();
		m_Control = scheme;

        m_Distributor = distributer;

        m_PubSubHub = pubSub;
		m_DatabaseManager= new DatabaseManager(this);
		m_GameAudioManager = new GameAudioManager(m_Context, m_Camera);
        m_SFXManager = new InGameSoundEffectsManager(this);
		m_ParticleManager = new ParticleManager(this);
		m_BulletManager = new BulletManager(this);
		m_VehicleManager = new VehicleManager(this);
        m_GameObjectManager = new GameObjectManager();
        m_RopeManager = new RopeManager(this);
        m_TrailManager = new TrailManager();
		m_UIManager = new UIManager(this);
		m_GameStats = new GameStats(this);
        m_RenderEffectManager = new RenderEffectManager(this, m_Packet.GetRenderEffectSettings());
        m_GooglePlayService = new GooglePlayService(this);

        m_ObjectEffectController = new ObjectEffectController(this);

        m_CameraAnchor = new ExhibitionCameraAnchor();
        AttachCameraToAnchor();
        m_Camera.SetInPlace();

        m_PubSubHub.SubscribeToTopic(PublishedTopics.PlayerSpawned, new PlayerSpawnedSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.PlayerDestroyed, new PlayerDestroyedSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.StartGame, new StartGameSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.EndGame, new EndGameSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.HighScorePressed, new LeaderBoardPressedSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.AchievementsPressed, new AchievementsPressedSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.HighTimePressed, new HighTimeLeaderBoardPressedSubscriber());
        m_PubSubHub.SubscribeToTopic(PublishedTopics.OnLearnToTouchComplete, new OnLearnToTouchCompleteSubscriber());

        m_GameReadyPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.GameReady);
        m_GameOverPublisher = m_PubSubHub.CreatePublisher(PublishedTopics.EndGame);

        m_PlayManager = new GameManager_Play(this);
        m_ExhibitionManager = new GameManager_Exhibition(this);
        m_QuietManager = new GameManager_Quiet(this);
        m_TestManager = new GameManager_Test(this);
        m_GameManager = m_QuietManager;

        m_MiscObjects = new ArrayList<>();

        SetGameMode(GameMode.Exhibition);
    }

    private static StopWatch asdf = new StopWatch();

	@Override
	public void Update(double deltaTime)
	{
        m_GameManager.Update(deltaTime);
		m_UIManager.Update(deltaTime);
		m_ParticleManager.Update(deltaTime);
		m_VehicleManager.Update(deltaTime);
        m_BulletManager.Update(deltaTime);
        m_GameObjectManager.Update(deltaTime);
        m_RopeManager.Update(deltaTime);
        m_TrailManager.Update();
		m_RenderEffectManager.Update(deltaTime);
        m_GameStats.Update(deltaTime);
        m_ObjectEffectController.Update(deltaTime);

		CheckCollisions();

		m_Camera.Update(deltaTime);

        RenderPackage rPackage = WaitForRenderPackage();
        FillRenderPackage(rPackage);
        m_Distributor.CheckIn_FromWrite(rPackage);
	}

	private RenderPackage WaitForRenderPackage()
    {
        RenderPackage rPackage = null;
        while(rPackage == null)
        {
            rPackage = m_Distributor.Checkout_ForWrite();
        }

        return rPackage;
    }

	private void FillRenderPackage(RenderPackage rPackage)
    {
        rPackage.SetCamera(m_Camera);

        ArrayList<Vehicle> vehicles = m_VehicleManager.GetVehicles();
        for(int i = 0; i < vehicles.size(); ++i)
        {
            Vehicle vehicle = vehicles.get(i);
            rPackage.AddVehicle(vehicle);
        }

        ArrayList<GameObject> objects = m_GameObjectManager.GetGameObjects();
        for(int i = 0; i < objects.size(); ++i)
        {
            GameObject object = objects.get(i);
            rPackage.AddObject(object);
        }

        ArrayList<FloorGrid> grids = m_GameObjectManager.GetFloorGrids();
        for(int i = 0; i < grids.size(); ++i)
        {
            FloorGrid grid = grids.get(i);
            rPackage.AddFloorGrid(grid);
        }

        ArrayList<Projectile> m_Projectiles = m_BulletManager.GetActiveBullets();
        for(int i = 0; i < m_Projectiles.size(); ++i)
        {
            GameObject object = m_Projectiles.get(i);
            rPackage.AddObject(object);
        }

        ArrayList<Particle> particles = m_ParticleManager.GetActiveParticles();
        int particleCount = particles.size();
        rPackage.PrepareForParticles(particleCount);
        for(int i = 0; i < particleCount; ++i)
        {
            Particle particle = particles.get(i);
            rPackage.AddParticle(particle);
        }

        for(int i = 0; i < m_MiscObjects.size(); ++i)
        {
            GameObject obj = m_MiscObjects.get(i);
            rPackage.AddObject(obj);
        }

        ObjectEffectType[] effectTypes = ObjectEffectType.values();
        for(int i = 0; i < effectTypes.length; ++i)
        {
            ObjectEffectType type = effectTypes[i];
            ArrayList<ObjectEffect> effectsOfType = m_ObjectEffectController.GetEffectsForType(type);

            for (int j = 0; j < effectsOfType.size(); ++j)
            {
                GameObject obj = effectsOfType.get(j);
                rPackage.AddObject(obj);
            }
        }

        ArrayList<UIElement_Label> labels = m_UIManager.GetLabels();
        for(int i = 0; i < labels.size(); ++i)
        {
            UIElement_Label label = labels.get(i);
            rPackage.AddText(label);
        }

       //ArrayList<ObjectEffect_HealthBar> healthBars = m_ObjectEffectController.GetHealthBars();
       //for(int i = 0; i < healthBars.size(); ++i)
       //{
       //    ObjectEffect_HealthBar healthBar = healthBars.get(i);
       //    rPackage.AddHealthBar(healthBar);
       //}

       //ArrayList<Trail> trails = m_TrailManager.GetTrails();
       //for(int i = 0; i < trails.size(); ++i)
       //{
       //    Trail trail = trails.get(i);
       //    rPackage.AddTrail(trail);
       //}
    }
	
	private void CheckCollisions()
	{
        ArrayList<Projectile> projectiles = m_BulletManager.GetActiveBullets();

        for(int i = 0; i < projectiles.size(); ++i)
        {
            Projectile projectile = projectiles.get(i);
		    ArrayList<Vehicle> vehicles = m_VehicleManager.GetVehicles();

            int numVehicles = vehicles.size();
            for(int j = 0; j < numVehicles; ++j)
		    {
                Vehicle vehicle = vehicles.get(j);

                // Prevent friendly fire.
                if (vehicle.GetAffiliation() != projectile.GetAffiliation())
                {
                    CollisionReport report = projectile.CheckForCollision(vehicle);
                    if (report != null)
                    {
                        vehicle.CollisionResponse(projectile.GetDamageOutput());
                        projectile.CollisionResponse(report, vehicle);
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
                m_GameManager = m_PlayManager;
                break;
            case Exhibition:
                m_GameManager = m_ExhibitionManager;
                break;
            case Quiet:
                m_GameManager = m_QuietManager;
                break;
            case Test:
                m_GameManager = m_TestManager;
                break;
        }

        m_GameMode = mode;
        m_GameManager.Initialise();
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
		m_Camera.Attach(m_CameraAnchor, Vector3.FORWARD);
	}

    public RopeManager GetRopeManager() { return m_RopeManager; }
    public TrailManager GetTrailManager() { return m_TrailManager; }

    public void AddMiscObject(GameObject obj) { m_MiscObjects.add(obj); }
    public void RemoveMiscObject(GameObject obj) { m_MiscObjects.remove(obj); }

	public GameAudioManager GetGameAudioManager() { return m_GameAudioManager; }
	public UIManager GetUIManager() { return m_UIManager; }
	public GameStats GetGameStats() { return m_GameStats; }
	public DatabaseManager GetDatabaseManager() { return m_DatabaseManager; }
    public GooglePlayService GetGooglePlayService() { return m_GooglePlayService; }
    public ObjectEffectController GetObjectEffectController() { return m_ObjectEffectController; }

    public void SaveScores()
    {
        m_GooglePlayService.SubmitScore(m_GameStats.GetScore());
        m_GooglePlayService.SubmitTime(m_GameStats.GetPlayTimeMillis());
        m_DatabaseManager.SaveHighScoreLocally(m_GameStats.GetScore(), m_GameStats.GetPlayTimeMillis());
    }

    private class StartGameSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            m_BulletManager.Wipe();
            m_VehicleManager.Wipe();
            m_GameObjectManager.Wipe();
            m_GameStats.Start();

            SetGameMode(GameMode.Play);
            m_UIManager.ShowScreen(UIScreens.Play);

            m_GameReadyPublisher.Publish();
        }
    }

    private class EndGameSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            SetGameMode(GameMode.Exhibition);
            m_GameStats.Stop();
            m_UIManager.ShowScreen(UIScreens.GameOverScreen);
            AttachCameraToAnchor();
        }
    }

    private class PlayerSpawnedSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            m_Camera.Attach(m_VehicleManager.GetPlayer(), Vector3.UP);
            m_Camera.SetUp(0, 1, 0);
        }
    }

    private class PlayerDestroyedSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            m_Camera.NormalCam();

            ArrayList<Vehicle> wingmen = m_VehicleManager.GetTeam(AffiliationKey.BlueTeam);
            if(!wingmen.isEmpty())
            {
                m_Camera.SetLookAt(wingmen.get(0));
            }

            m_GameOverPublisher.Publish();
        }
    }

    private class LeaderBoardPressedSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            m_GooglePlayService.DisplayLeaderBoard();
        }
    }

    private class HighTimeLeaderBoardPressedSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            m_GooglePlayService.DisplayHighTimeLeaderBoard();
        }
    }

    private class OnLearnToTouchCompleteSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
        {
            SetGameMode(GameMode.Exhibition);
            m_UIManager.ShowScreen(UIScreens.Splash );
            m_GameAudioManager.StartMusic();
        }
    }

    private class AchievementsPressedSubscriber extends Subscriber
    {
        @Override
        public void Update(Object args)
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

    public RendererPacket GetPacket() { return m_Packet; }
}
