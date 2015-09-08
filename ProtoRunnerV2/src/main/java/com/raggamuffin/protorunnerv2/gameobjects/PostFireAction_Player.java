package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.pubsub.PublishedTopics;
import com.raggamuffin.protorunnerv2.pubsub.Publisher;

public class PostFireAction_Player extends PostFireAction
{
	private Publisher m_PlayerShotFiredPublisher;
	
	public PostFireAction_Player(Vehicle anchor, PubSubHub pubSub)
	{
		super(anchor);
		m_PlayerShotFiredPublisher = pubSub.CreatePublisher(PublishedTopics.PlayerShotFired);
	}

	@Override
	public void Update() 
	{
		double drainAmount = m_Anchor.GetPrimaryWeapon().GetDrain();
		m_Anchor.DrainEnergy(drainAmount);
		m_PlayerShotFiredPublisher.Publish();
	}
}
