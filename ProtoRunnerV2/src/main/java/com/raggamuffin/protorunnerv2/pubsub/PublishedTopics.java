package com.raggamuffin.protorunnerv2.pubsub;

public enum PublishedTopics 
{
	SwitchScreen,
	StartGame,
    GameReady,
    StartTest,
    StartTutorial,
    NextTutorialButtonPressed,
    TutorialComplete,
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
	PlayerSwitchedWeapon,
    SignInPressed,
    SignOutPressed,
    GooglePlayConnected,
    GooglePlayDisconnected,

    // Game events
    MultiplierCollected,
    MultiplierIncreased,
    MultiplierDecreased,

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
}