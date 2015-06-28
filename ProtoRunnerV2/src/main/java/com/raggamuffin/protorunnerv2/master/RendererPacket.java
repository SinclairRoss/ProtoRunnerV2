package com.raggamuffin.protorunnerv2.master;

import java.util.Vector;

import android.content.Context;

import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.particles.TrailParticle;
import com.raggamuffin.protorunnerv2.ui.UIElement;

public class RendererPacket 
{
	private Vector<GameObject> m_GameObjects;
	private Vector<GameObject> m_TransparentObjects;
	private Vector<UIElement>  m_UIElements;
	private Vector<TrailParticle> m_TrailParticles;
	private ChaseCamera m_Camera;
	private Context m_Context;
	private RenderEffectSettings m_RenderEffectSettings;
	
	public RendererPacket()
	{
		m_GameObjects = null;
		m_TransparentObjects = null;
		m_UIElements = null;
		m_TrailParticles = null;
		m_Context = null;
		m_Camera = null;
		m_RenderEffectSettings = null;
	}
	
	///// Game Objects.
	public Vector<GameObject> GetGameObjects()
	{
		return m_GameObjects;
	}
	
	public void SetGameObjects(Vector<GameObject> objects)
	{
		m_GameObjects = objects;
	}
	
	///// Transparent Objects.
	public Vector<GameObject> GetTransparentObjects()
	{
		return m_TransparentObjects;
	}
	
	public void SetTransparentObjects(Vector<GameObject> objects)
	{
		m_TransparentObjects = objects;
	}
	
	///// UI Elements.
	public Vector<UIElement> GetUIElements()
	{
		return m_UIElements;
	}
	
	public void SetUIElements(Vector<UIElement> elements)
	{
		m_UIElements = elements;
	}
	
	///// Trail Particles.
	public Vector<TrailParticle> GetTrails()
	{
		return m_TrailParticles;
	}
	
	public void SetTrails(Vector<TrailParticle> trails)
	{
		m_TrailParticles = trails;
	}
	
	///// Context.
	public Context GetContext()
	{
		return m_Context;
	}
	
	public void SetContext(Context context)
	{
		m_Context = context;
	}
	
	///// Camera.
	public ChaseCamera GetCamera()
	{
		return m_Camera;
	}
	
	public void SetCamera(ChaseCamera cam)
	{
		m_Camera = cam;
	}
	
	///// Render Effect Settings.
	public RenderEffectSettings GetRenderEffectSettings()
	{
		return m_RenderEffectSettings;
	}
	
	public void SetRenderEffectSettings(RenderEffectSettings Settings)
	{
		m_RenderEffectSettings = Settings;
	}
}
