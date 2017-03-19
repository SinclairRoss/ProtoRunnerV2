package com.raggamuffin.protorunnerv2.master;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;

import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Tentacle;
import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.particles.ParticleType;
import com.raggamuffin.protorunnerv2.particles.Trail;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UIElementType;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class RendererPacket
{
    private final Point m_ScreenSize;
    private final ArrayList<CopyOnWriteArrayList<GameObject>> m_GameObjects;
    private final CopyOnWriteArrayList<Trail> m_Trails;
    private final CopyOnWriteArrayList<Tentacle> m_Tentacles;
    private final CopyOnWriteArrayList<FloorGrid> m_FloorGrids;
    private final CopyOnWriteArrayList<Particle> m_Particles;
    private final CopyOnWriteArrayList<Particle> m_Particles_Multiplier;
    private final ArrayList<CopyOnWriteArrayList<UIElement>> m_UIElements;
    private final ChaseCamera m_Camera;
    private final Context m_Context;
    private final RenderEffectSettings m_RenderEffectSettings;

    public RendererPacket(Context context, ChaseCamera camera, RenderEffectSettings settings, Point screenSize)
    {
        m_ScreenSize = screenSize;

        int numModels = ModelType.values().length;
        m_GameObjects = new ArrayList<>(numModels);

        for (int i = 0; i < numModels; ++i)
        {
            m_GameObjects.add(new CopyOnWriteArrayList<GameObject>());
        }

        int numUIElements = UIElementType.values().length;
        m_UIElements = new ArrayList<>(numUIElements);

        for (int i = 0; i < numUIElements; i++)
        {
            m_UIElements.add(new CopyOnWriteArrayList<UIElement>());
        }

        m_Particles = new CopyOnWriteArrayList<>();
        m_Particles_Multiplier = new CopyOnWriteArrayList<>();
        m_Trails = new CopyOnWriteArrayList<>();
        m_Tentacles = new CopyOnWriteArrayList<>();
        m_FloorGrids = new CopyOnWriteArrayList<>();

        m_Context = context;
        m_Camera = camera;
        m_RenderEffectSettings = settings;
    }

    public void AddObject(Trail trail) { m_Trails.add(trail); }
    public void RemoveObject(Trail trail) { m_Trails.remove(trail); }
    public CopyOnWriteArrayList<Trail> GetTrails() { return m_Trails; }

    public void AddObject(Tentacle node) { m_Tentacles.add(node); }
    public void RemoveObject(Tentacle node) { m_Tentacles.remove(node);}
    public CopyOnWriteArrayList<Tentacle> GetRopes()
    {
        return m_Tentacles;
    }

    public void AddObject(GameObject object) { GetModelList(object.GetModel()).add(object); }
    public void RemoveObject(GameObject object) { GetModelList(object.GetModel()).remove(object); }
    public CopyOnWriteArrayList<GameObject> GetModelList(ModelType type) { return m_GameObjects.get(type.ordinal()); }

    public void AddObject(ArrayList<Particle> particles, ParticleType type) { GetParticleListFromType(type).addAll(particles); }
    public void RemoveObject(ArrayList<Particle> particles, ParticleType type) { GetParticleListFromType(type).removeAll(particles); }
    public CopyOnWriteArrayList<Particle> GetParticles(ParticleType type) { return GetParticleListFromType(type); }

    public void AddObject(FloorGrid floorGrid) { m_FloorGrids.add(floorGrid); }
    public void RemoveObject(FloorGrid floorGrid) { m_FloorGrids.remove(floorGrid); }
    public CopyOnWriteArrayList<FloorGrid> GetFloorGrids()
    {
        return m_FloorGrids;
    }

    public void AddUIElement(UIElement element) { GetUIElementList(element.GetType()).add(element); }
    public void RemoveUIElement(UIElement element) { GetUIElementList(element.GetType()).remove(element); }
    public CopyOnWriteArrayList<UIElement> GetUIElementList(UIElementType type) { return m_UIElements.get(type.ordinal()); }

	public Context GetContext() { return m_Context; }
	public ChaseCamera GetCamera() { return m_Camera; }
	public RenderEffectSettings GetRenderEffectSettings() { return m_RenderEffectSettings; }
    public Point GetScreenSize() { return m_ScreenSize; }

    public void OutputDebugInfo()
    {
        Log.e("Packet", "<-------------------->");
        Log.e("PacketParticles", "Num Particles: " + m_Particles.size());
        Log.e("PacketParticles", "Num Multipliers: " + m_Particles_Multiplier.size());
        Log.e("PacketTrail", "Num Trails: " + m_Trails.size());
        Log.e("PacketFloorGrid", "Num FloorGrids: " + m_FloorGrids.size());
    }

    private CopyOnWriteArrayList<Particle> GetParticleListFromType(ParticleType type)
    {
        CopyOnWriteArrayList<Particle> particleList = null;

        switch (type)
        {
            case Standard:
                particleList = m_Particles;
                break;
            case Multiplier:
                particleList = m_Particles_Multiplier;
                break;
        }

        return particleList;
    }
}
