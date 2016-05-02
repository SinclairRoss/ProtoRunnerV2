package com.raggamuffin.protorunnerv2.master;

import java.util.ArrayList;

import android.content.Context;

import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.particles.TrailPoint;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UIElementType;
import com.raggamuffin.protorunnerv2.weapons.Projectile;

public class RendererPacket 
{
	private final ArrayList<ArrayList<GameObject>> m_GameObjects;
    private final ArrayList<TrailPoint> m_TrailPoints;
    private final ArrayList<Particle> m_Particles;
	private final ArrayList<ArrayList<UIElement>> m_UIElements;
	private final ChaseCamera m_Camera;
	private final Context m_Context;
	private final RenderEffectSettings m_RenderEffectSettings;
	
	public RendererPacket(Context context, ChaseCamera camera, RenderEffectSettings settings)
    {
        int numModels = ModelType.values().length;
		m_GameObjects = new ArrayList<>(numModels);

        for(int i = 0; i < numModels; i++)
        {
            m_GameObjects.add(new ArrayList<GameObject>());
        }

        int numUIElements = UIElementType.values().length;
        m_UIElements = new ArrayList<>(numUIElements);

        for(int i = 0; i < numUIElements; i++)
        {
            m_UIElements.add(new ArrayList<UIElement>());
        }

        m_Particles = new ArrayList<>();
        m_TrailPoints = new ArrayList<>();

		m_Context = context;
		m_Camera = camera;
		m_RenderEffectSettings = settings;
	}

    ///// Game Objects.
    public ArrayList<TrailPoint> GetTrailPoints()
    {
        return m_TrailPoints;
    }

    public ArrayList<GameObject> GetModelList(ModelType type)
    {
        return m_GameObjects.get(type.ordinal());
    }

    public ArrayList<Particle> GetParticles()
    {
        return m_Particles;
    }

    public void AddObject(TrailPoint point)
    {
        m_TrailPoints.add(point);
    }

    public void AddObject(GameObject object)
    {
        GetModelList(object.GetModel()).add(object);
    }

    public void AddObject(Particle particle)
    {
        m_Particles.add(particle);
    }

    public void RemoveObject(TrailPoint point)
    {
        m_TrailPoints.remove(point);
    }

    public void RemoveObject(GameObject object)
    {
        GetModelList(object.GetModel()).remove(object);
    }

    public void RemoveObject(Particle particle)
    {
        m_Particles.remove(particle);
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

	///// Camera.
	public ChaseCamera GetCamera()
    {
        return m_Camera;
    }

	///// Render Effect Settings.
	public RenderEffectSettings GetRenderEffectSettings()
	{
		return m_RenderEffectSettings;
	}
}
