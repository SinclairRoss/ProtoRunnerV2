package com.raggamuffin.protorunnerv2.RenderObjects;

// Author: Sinclair Ross
// Date:   29/07/2017

import com.raggamuffin.protorunnerv2.gameobjects.ChaseCamera;
import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Vehicle;
import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.renderer.RenderCamera;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UIElementType;
import com.raggamuffin.protorunnerv2.ui.UIElement_Label;
import com.raggamuffin.protorunnerv2.utils.Vector3;

import java.util.ArrayList;

public class RenderPackage
{
    private RenderCamera m_Camera;

    private final ArrayList<RenderGroup_Vehicle> m_Vehicles;
    private final ArrayList<RenderGroup_Generic> m_GenericObjects;
    private final ArrayList<RenderGroup_UI> m_UIElements;
    private final RenderGroup_Particles m_Particles;
    private final RenderGroup_Text m_Text;
    private final RenderGroup_Radial m_Radials;

    public RenderPackage()
    {
        m_Camera = new RenderCamera();

        int numModels = ModelType.values().length;

        m_Vehicles = new ArrayList<>(numModels);
        for(int i = 0; i < numModels; ++i) { m_Vehicles.add(new RenderGroup_Vehicle()); }

        m_GenericObjects = new ArrayList<>(numModels);
        for(int i = 0; i < numModels; ++i) { m_GenericObjects.add(new RenderGroup_Generic()); }

        int numUIElements = UIElementType.values().length;
        m_UIElements = new ArrayList<>(numUIElements);
        for(int i = 0; i < numUIElements; ++i) { m_UIElements.add(new RenderGroup_UI()); }

        m_Particles = new RenderGroup_Particles();
        m_Text = new RenderGroup_Text();
        m_Radials = new RenderGroup_Radial();
    }

    public void Clear()
    {
        for(int i = 0; i < m_Vehicles.size(); ++i) { m_Vehicles.get(i).Clean(); }
        for(int i = 0; i < m_GenericObjects.size(); ++i) { m_GenericObjects.get(i).Clean(); }
        for(int i = 0; i < m_UIElements.size(); ++i) { m_UIElements.get(i).Clean(); }

        m_Particles.Clean();
        m_Text.Clean();
        m_Radials.Clean();
    }

    public void SetCamera(ChaseCamera camera) { m_Camera.SetupForCamera(camera); }
    public RenderCamera GetRenderCamera() { return m_Camera; }

    // Vehicles
    public void AddVehicle(Vehicle vehicle)
    {
        RenderGroup_Vehicle renderGroup = m_Vehicles.get(vehicle.GetModel().ordinal());
        renderGroup.AddVehicle(vehicle);
    }

    public RenderGroup_Vehicle GetVehiclesWithModel(ModelType model) { return m_Vehicles.get(model.ordinal()); }

    // Generic Objects
    public void AddObject(GameObject object)
    {
        RenderGroup_Generic renderGroup = GetObjectsWithModel(object.GetModel());
        renderGroup.AddObject(object);
    }

    public RenderGroup_Generic GetObjectsWithModel(ModelType model) { return m_GenericObjects.get(model.ordinal()); }

    // Particles
    public void PrepareForParticles(int instanceCount) { m_Particles.PrepareRenderGroup(instanceCount);}
    public void AddParticle(Particle particle) { m_Particles.AddParticle(particle); }
    public RenderGroup_Particles GetParticles() { return m_Particles; }

    // Floor grids
    public void AddFloorGrid(FloorGrid floorGrid)
    {
        RenderGroup_Generic renderGroup = GetObjectsWithModel(ModelType.FloorGrid);
        renderGroup.AddObject(floorGrid.GetPosition(), Vector3.ONE, Vector3.FORWARD, Vector3.UP, Vector3.RIGHT, floorGrid.GetColour());
    }

    // Text
    public void AddText(UIElement_Label label) { m_Text.AddText(label); }
    public RenderGroup_Text GetText() { return m_Text; }

    // UI
    public void AddUIElement(UIElement element)
    {
        RenderGroup_UI renderGroup = m_UIElements.get(element.GetType().ordinal());
        renderGroup.AddElement(element);
    }

    public RenderGroup_UI GetUIElementsWithType(UIElementType model) { return m_UIElements.get(model.ordinal()); }
}
