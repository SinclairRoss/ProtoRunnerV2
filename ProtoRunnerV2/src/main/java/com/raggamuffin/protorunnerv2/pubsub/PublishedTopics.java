package com.raggamuffin.protorunnerv2.pubsub;

public enum PublishedTopics 
{
	SwitchScreen,
	StartGame,
    StartTutorial,
    NextTutorialButtonPressed,
    TutorialComplete,
	EndGame,
	PlayerSpawned,
	PlayerHit,
	PlayerShotFired,
	EnemyHit,
	EnemyDestroyed,
	WingmanDestroyed,
	PlayerDestroyed,
	PlayerSwitchedWeapon,
    PanicSwitchFired,
    PanicSwitchDepleted,

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