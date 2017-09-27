package com.raggamuffin.protorunnerv2.pubsub;

public enum PublishedTopics 
{
	SwitchScreen,
	StartGame,
    GameReady,
    StartTest,
    NextTutorialButtonPressed,
    HighScorePressed,
    HighTimePressed,
    AchievementsPressed,
	EndGame,
	PlayerSpawned,
	PlayerHit,
	PlayerShotFired,
	EnemyHit,
	EnemyDestroyed,
	WingmanDestroyed,
	PlayerDestroyed,
    SignInPressed,
    SignOutPressed,
    GooglePlayConnected,
    GooglePlayDisconnected,

    // Controls
    EvadeLeft,
    EvadeRight,
    StrafeLeft,
    StrafeRight,
    AfterBurnersEngage,
    AfterBurnersDisengage,
    Reverse,
    Forward,
    Fire,
    CeaseFire,
    WeaponLeft,
    WeaponRight,
    WeaponUp,
    WeaponDown,

    OnLearnToTouchComplete,

    OnPointerDown,
    OnPointerUp,

    LogicThreadComplete,

    RenderThreadStarted,
    RenderThreadComplete
}