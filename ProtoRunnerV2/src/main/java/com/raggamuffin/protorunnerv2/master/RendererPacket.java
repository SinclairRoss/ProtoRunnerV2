package com.raggamuffin.protorunnerv2.master;

import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

import android.content.Context;

import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.ui.UIElement;

public class RendererPacket 
{
	private ArrayList<ArrayList<GameObject>> m_GameObjects;

	private ArrayList<UIElement> m_UIElements;
	private ChaseCamera m_Camera;
	private Context m_Context;
	private RenderEffectSettings m_RenderEffectSettings;
	
	public RendererPacket()
    {
        int numModels = ModelType.values().length;

		m_GameObjects = new ArrayList<ArrayList<GameObject>>(numModels);

        for(int i = 0; i < numModels; i++)
        {
            m_GameObjects.add(new ArrayList<GameObject>());
        }

		m_UIElements = null;
		m_Context = null;
		m_Camera = null;
		m_RenderEffectSettings = null;
	}

    public ArrayList<GameObject> GetList(ModelType type)
    {
        return m_GameObjects.get(type.ordinal());
    }

    public void AddObject(GameObject object)
    {
        GetList(object.GetModel()).add(object);
    }

    public void RemoveObject(GameObject object)
    {
        GetList(object.GetModel()).remove(object);
    }

	///// UI Elements.
	public ArrayList<UIElement> GetUIElements()
	{
		return m_UIElements;
	}
	
	public void SetUIElements(ArrayList<UIElement> elements)
	{
		m_UIElements = elements;
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
