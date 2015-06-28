package com.raggamuffin.protorunnerv2.ui;

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;

public abstract class TextAnimation 
{
	public enum ActivationMode {Continuous, Triggered, Random}
	
	public enum AnimationState {Initialising, Running, Closing, Idle }
	private AnimationState m_AnimationState;
	
	protected UILabel m_Anchor;
	protected String m_OutputText;
	
	private ActivationBehaviour m_ActivationBehaviour;
	
	protected GameAudioManager m_GameAudioManager;
	
	private double m_Delay;
	
	public TextAnimation(UILabel Element, GameAudioManager audio, ActivationMode Mode)
	{
		m_Anchor = Element;
		m_GameAudioManager = audio;
		
		m_AnimationState = AnimationState.Idle;
		m_OutputText = "";

		SetActivationMode(Mode);
		
		m_Delay = 0.0;
	}
	
	public void Update(double deltaTime)
	{
		switch(m_AnimationState)
		{
			case Initialising:
				m_Delay -= deltaTime;
				
				if(m_Delay <= 0.0)
					m_AnimationState = AnimationState.Running;
				
				break;

			case Running:
				UpdateBehaviour(deltaTime);
				break;
				
			case Closing:
				m_AnimationState = AnimationState.Idle;

			case Idle:
				m_ActivationBehaviour.UpdateTrigger(deltaTime);
				break;
				
			default:
				break;
			
		}
	}
	
	protected abstract void InitialiseBehaviour();
	protected abstract void UpdateBehaviour(double DeltaTime);
	
	private void SetActivationMode(ActivationMode Mode)
	{
		switch(Mode)
		{
			case Continuous:
				m_ActivationBehaviour = new ContinuousActivation(this);
				break;
				
			case Random:
				m_ActivationBehaviour = new RandomActivation(this);
				break;
				
			case Triggered:
				m_ActivationBehaviour = new TriggeredActivation(this);
				break;
				
			default:
				new RuntimeException("TextAnimation.java - SetActivationMode(ActivationMode Mode) : Unhandled switch case.");
				break;
		}
	}
	
	protected void AnimationComplete()
	{
		m_AnimationState = AnimationState.Closing;
	}
	
	public void TriggerBehaviour()
	{
		TriggerBehaviour(0.0);
	}
	
	public void TriggerBehaviour(double delay)
	{
		InitialiseBehaviour();
		m_AnimationState = AnimationState.Initialising;
		m_Delay = delay;
	}
	
	public AnimationState GetState()
	{
		return m_AnimationState;
	}
}
