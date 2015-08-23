package com.raggamuffin.protorunnerv2.master;

import java.util.ArrayList;

import android.content.Context;

import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UIElementType;

public class RendererPacket 
{
	private ArrayList<ArrayList<GameObject>> m_GameObjects;
	private ArrayList<ArrayList<UIElement>> m_UIElements;
	private ChaseCamera m_Camera;
	private Context m_Context;
	private RenderEffectSettings m_RenderEffectSettings;
	
	public RendererPacket()
    {
        int numModels = ModelType.values().length;
		m_GameObjects = new ArrayList<>(numModels);

        for(int i = 0; i < numModels; i++)
            m_GameObjects.add(new ArrayList<GameObject>());

        int numUIElements = UIElementType.values().length;
        m_UIElements = new ArrayList<>(numUIElements);

        for(int i = 0; i < numUIElements; i++)
            m_UIElements.add(new ArrayList<UIElement>());

		m_Context = null;
		m_Camera = null;
		m_RenderEffectSettings = null;
	}

    public ArrayList<GameObject> GetModelList(ModelType type)
    {
        return m_GameObjects.get(type.ordinal());
    }

    public void AddObject(GameObject object)
    {
        GetModelList(object.GetModel()).add(object);
    }

    public void RemoveObject(GameObject object)
    {
        GetModelList(object.GetModel()).remove(object);
    }

	///// UI Elements.

    public ArrayList<UIElement> GetUIElementList(UIElementType type)
    {
        return m_UIElements.get(type.ordinal());
    }

    public void AddUIElement(UIElement element)
    {
        GetUIElementList(element.GetType()).add(element);
    }

    public void RemoveUIElement(UIElement element)
    {
        GetUIElementList(element.GetType()).remove(element);
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
