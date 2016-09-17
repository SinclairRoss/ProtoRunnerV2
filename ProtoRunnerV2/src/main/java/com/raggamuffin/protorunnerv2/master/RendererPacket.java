package com.raggamuffin.protorunnerv2.master;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;

import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Tentacle;
import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.particles.TrailNode;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UIElementType;

public class RendererPacket 
{
    private final Point m_ScreenSize;
	private final ArrayList<ArrayList<GameObject>> m_RenderObjects;
    private final ArrayList<TrailNode> m_TrailNodes;
    private final ArrayList<Tentacle> m_Tentacles;
    private final ArrayList<Particle> m_Particles;
	private final ArrayList<ArrayList<UIElement>> m_UIElements;
	private final ChaseCamera m_Camera;
	private final Context m_Context;
	private final RenderEffectSettings m_RenderEffectSettings;
	
	public RendererPacket(Context context, ChaseCamera camera, RenderEffectSettings settings, Point screenSize)
    {
        m_ScreenSize = screenSize;

        int numModels = ModelType.values().length;
		m_RenderObjects = new ArrayList<>(numModels);

        for(int i = 0; i < numModels; ++i)
        {
            m_RenderObjects.add(new ArrayList<GameObject>());
        }

        int numUIElements = UIElementType.values().length;
        m_UIElements = new ArrayList<>(numUIElements);

        for(int i = 0; i < numUIElements; i++)
        {
            m_UIElements.add(new ArrayList<UIElement>());
        }

        m_Particles = new ArrayList<>();
        m_TrailNodes = new ArrayList<>();
        m_Tentacles = new ArrayList<>();

		m_Context = context;
		m_Camera = camera;
		m_RenderEffectSettings = settings;
	}

    public ArrayList<TrailNode> GetTrailPoints()
    {
        return m_TrailNodes;
    }

    public ArrayList<Tentacle> GetRopes()
    {
        return m_Tentacles;
    }

    public ArrayList<GameObject> GetModelList(ModelType type)
    {
        return m_RenderObjects.get(type.ordinal());
    }

    public ArrayList<Particle> GetParticles()
    {
        return m_Particles;
    }

    public void AddObject(TrailNode point)
    {
        m_TrailNodes.add(point);
    }

    public void AddObject(Tentacle node)
    {
        m_Tentacles.add(node);
    }

    public void AddObject(GameObject object)
    {
        GetModelList(object.GetModel()).add(object);
    }

    public void AddObject(Particle particle)
    {
        m_Particles.add(particle);
    }

    public void RemoveObject(TrailNode point)
    {
        m_TrailNodes.remove(point);
    }

    public void RemoveObject(Tentacle node) { m_Tentacles.remove(node);}

    public void RemoveObject(GameObject object)
    {
        GetModelList(object.GetModel()).remove(object);
    }

    public void RemoveObject(Particle particle)
    {
        m_Particles.remove(particle);
    }

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

	public Context GetContext()
	{
		return m_Context;
	}

	public ChaseCamera GetCamera()
    {
        return m_Camera;
    }

	public RenderEffectSettings GetRenderEffectSettings()
	{
		return m_RenderEffectSettings;
	}

    public Point GetScreenSize()
    {
        return m_ScreenSize;
    }
}
