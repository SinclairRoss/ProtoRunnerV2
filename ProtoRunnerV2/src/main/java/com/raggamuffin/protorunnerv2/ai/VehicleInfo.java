package com.raggamuffin.protorunnerv2.ai;

import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;

public class VehicleInfo 
{
	public enum MovementStates 
	{
		Normal,
		Reverse,
		StrafeLeft,
		StrafeRight
	}
	
	public enum AfterBurnerStates
	{
		Engaged,
		Disengaged
	}
	
	public enum WeaponStates
	{
		Firing,
		Holding
	}
	
	MovementStates m_MovementState;
	AfterBurnerStates m_AfterBurnerState;
	WeaponStates m_WeaponState;
	Vehicle m_Target;
	
	public VehicleInfo()
	{
		m_MovementState 	= MovementStates.Normal;
		m_AfterBurnerState	= AfterBurnerStates.Disengaged;
		m_WeaponState 		= WeaponStates.Holding;
	}
	
	///// Movement state
	public void SetMovementState(MovementStates state)
	{
		m_MovementState = state;
	}
	
	public MovementStates GetMovmentState()
	{
		return m_MovementState;
	}
	
	///// Afterburner state
	public void SetAfterBurnerState(AfterBurnerStates state)
	{
		m_AfterBurnerState = state;
	}
	
	public AfterBurnerStates GetAfterBurnerState()
	{
		return m_AfterBurnerState;
	}
	
	///// Weapon state
	public void SetWeaponState(WeaponStates state)
	{
		m_WeaponState = state;
	}
	
	public WeaponStates GetWeaponState()
	{
		return m_WeaponState;
	}
	
	///// Target
	public void SetTarget(Vehicle target)
	{
		m_Target = target;
	}
	
	public Vehicle GetTarget()
	{
		return m_Target;
	}
}
