package com.raggamuffin.protorunnerv2.gameobjects;

import java.util.ArrayList;
import java.util.Vector;


import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_FadeTo;
import com.raggamuffin.protorunnerv2.gamelogic.AffiliationKey;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.Vector3;
import com.raggamuffin.protorunnerv2.utils.Vector4;
import com.raggamuffin.protorunnerv2.gameobjects.Wingman;

public abstract class GameObject 
{
	///// Position and orientation vectors
	protected Vector3 m_Position;		// Position of the GameObject. 
	private Vector3 m_PreviousPosition;	// Used in collision detection.
	protected Vector3 m_Forward;		// Forward vector of the GameObject
    protected Vector3 m_Backward;
	protected Vector3 m_Up;				// Up vector of the GameObject.
	protected Vector3 m_Left;			// Left vector of the GameObject.
    protected Vector3 m_Right;
	protected Vector3 m_Scale;			// The scale of the GameObject.
	protected Vector3 m_Velocity;		// Velocity of the GameObject.
	protected Vector3 m_Force;			// The force being applied to the GameObject.
	protected Vector3 m_Acceleration;	// Acceleration of GameObject
	protected double m_Yaw;	            // The orientation of the GameObject in the x-z plane
    protected double  m_Roll;           // The orientation of the GameObject in the x-y plane
	
	///// Physics Attributes
	protected double m_Mass;			// The mass of the vehicle.
	protected double m_DragCoefficient;	// How much air resistance is generated by the vehicle.
	
	///// Colour Attributes
	protected Colour m_Colour;			// Colour of the GameObject.
	protected Colour m_BaseColour;		// Original colour of the GameObject.
    protected Colour m_AltColour;       // The Alternate colour of the GameObject.
	private Vector4 m_DeltaColour; 		// The colour to be added to the base colour resulting in the actual colour of the game object. Uses a vector3 because the colour class doesn't support numbers outside of the range 0 - 1.
	private ArrayList<ColourBehaviour> m_ColourBehaviours;	// Contains all colour behaviours active on this game object.
    protected ColourBehaviour_FadeTo m_ChangeColourBehaviour;

	///// Misc Attributes.
	protected double m_BoundingRadius;	// The bounding radius of the game object. Used in collision detection.	
	protected ModelType m_Model;				// What model the gameobject is using to render.
	private ArrayList<GameObject> m_Children;	// Other game object that depend on this object. for example reticules and Floor effects.
	private AffiliationKey m_Faction;
	protected PubSubHub m_PubSubHub;
	protected GameAudioManager m_GameAudioManager;
	
	protected boolean m_ForciblyInvalidated;
	
	public GameObject(PubSubHub PubSub, GameAudioManager audio)
	{
		///// Position and orientation vectors.
		m_Position 			= new Vector3(0,0,0);		
		m_PreviousPosition 	= new Vector3(0,0,0);
		m_Forward 			= new Vector3(0,0,1);
        m_Backward          = new Vector3(0,0,-1);
		m_Up 				= new Vector3(0,1,0);
		m_Left 				= new Vector3(-1,0,0);
        m_Right             = new Vector3(1,0,0);
		m_Scale 			= new Vector3(1.0);
		m_Velocity 			= new Vector3();
		m_Force 			= new Vector3();
		m_Acceleration  	= new Vector3();
		m_Yaw = 0.0;
        m_Roll = 0.0;
		
		///// Physics Attributes
		m_Mass				 = 1000.0f;
		m_DragCoefficient	 = 0.007f;
		
		///// Colour Attributes.
		m_BaseColour  = new Colour(Colours.Black);
        m_AltColour   = new Colour(Colours.Black);
		m_Colour  	  = new Colour(m_BaseColour);
		m_DeltaColour = new Vector4();
		m_ColourBehaviours = new ArrayList<>();

        m_ChangeColourBehaviour = new ColourBehaviour_FadeTo(this, ColourBehaviour.ActivationMode.Triggered, 1.0);
        AddColourBehaviour(m_ChangeColourBehaviour);

        ///// Misc Attributes.
		m_BoundingRadius = 1.0;
		m_Children = new ArrayList<>();

		SetAffiliation(AffiliationKey.Neutral);

		m_Model = ModelType.Nothing;
		m_PubSubHub = PubSub;
		m_GameAudioManager = audio;
		
		m_ForciblyInvalidated = false;
	}
	
	public void Update(double deltaTime)
	{	
		CalculateAcceleration();
		CalculateVelocity();
        ApplyDrag();
		UpdatePosition(deltaTime);
        UpdateColours(deltaTime);

		for (GameObject Child : m_Children)
        {
            Child.GetPosition().SetVector(m_Position);
            Child.Update(deltaTime);
		}
		
		CleanUpForces();
	}

    protected void CalculateAcceleration()
    {
        m_Acceleration.I = m_Force.I / m_Mass;
        m_Acceleration.J = m_Force.J / m_Mass;
        m_Acceleration.K = m_Force.K / m_Mass;
    }

    protected void CalculateVelocity()
    {
        m_Velocity.I += m_Acceleration.I;
        m_Velocity.J += m_Acceleration.J;
        m_Velocity.K += m_Acceleration.K;
    }

    private void ApplyDrag()
    {
        double Speed = m_Velocity.GetLength();

        double i = m_DragCoefficient * -m_Velocity.I * Speed;
        double j = m_DragCoefficient * -m_Velocity.J * Speed;
        double k = m_DragCoefficient * -m_Velocity.K * Speed;

        m_Velocity.Add(i, j, k);
    }
	
	private void UpdatePosition(double deltaTime)
    {
        m_PreviousPosition.SetVector(m_Position);

        m_Position.I += m_Velocity.I * deltaTime;
        m_Position.J += m_Velocity.J * deltaTime;
        m_Position.K += m_Velocity.K * deltaTime;
	}

	protected void UpdateVectors()
	{
        m_Forward.I = -Math.sin(m_Yaw);
        m_Forward.K =  Math.cos(m_Yaw);
        m_Backward.SetVectorAsInverse(m_Forward);

        m_Left.I = Math.cos(m_Yaw);
        m_Left.K = Math.sin(m_Yaw);
        m_Right.SetVectorAsInverse(m_Left);
	}
	
	public void ApplyForce(Vector3 Dir, double Force)
	{
		m_Force.I += Dir.I * Force;
		m_Force.J += Dir.J * Force;
		m_Force.K += Dir.K * Force;
	}

    private void UpdateColours(double deltaTime)
    {
        m_Colour.SetColour(m_BaseColour);
        m_DeltaColour.SetVector(0.0);

        for (ColourBehaviour Behaviour : m_ColourBehaviours)
        {
            Behaviour.Update(deltaTime);
            m_DeltaColour.Add(Behaviour.GetDeltaColour());
        }

        m_Colour.Add(m_DeltaColour);
    }

    public void AddColourBehaviour(ColourBehaviour behaviour)
	{
		m_ColourBehaviours.add(behaviour);
	}
	
	public void RemoveColourBehaviour(ColourBehaviour behaviour)
	{
		m_ColourBehaviours.remove(behaviour);
    }

	protected void CleanUpForces()
	{
		m_Force.SetVector(0.0f);
	}
	
	public void CollisionResponse(GameObject Collider, double deltaTime)
	{
		// Should be overriden for collision response.
	}
	
	// Each subclass must define when it becomes invalid.
	public abstract boolean IsValid();
	
	public void ForceInvalidation()
	{
		m_ForciblyInvalidated = true;
	}
	
	protected boolean IsForciblyInvalidated()
	{
		return m_ForciblyInvalidated;
	}
	
	protected void ResetForcedInvalidation()
	{
		m_ForciblyInvalidated = false;
	}

	///// Getters / Setters.
	public double GetYaw()
	{
		return m_Yaw;
	}
	
	public void SetYaw(double yaw)
	{
		m_Yaw = yaw;
	}

    public double GetRoll()
    {
        return m_Roll;
    }

    public void SetRoll(double roll)
    {
        m_Roll = roll;
    }

	public ModelType GetModel()
	{
		return m_Model;
	}

	public Vector3 GetPosition()
	{
		return m_Position;
	}
	
	public void SetPosition(Vector3 position)
	{
		m_Position.SetVector(position);
	}
	
	public void SetPosition(double x, double y, double z)
	{
		m_Position.SetVector(x, y, z);
	}
	
	public void SetVelocity(Vector3 velocity)
	{
		m_Velocity.SetVector(velocity);
	}
	
	public Vector3 GetPreviousPosition()
	{
		return m_PreviousPosition;
	}
	
	public Vector3 GetForward()
	{
		return m_Forward;
	}

	public Vector3 GetUp()
	{
		return m_Up;
	}

	public Vector3 GetVelocity()
	{
		return m_Velocity;
	}
	
	public Vector3 GetScale()
	{
		return m_Scale;
	}
	
	public Colour GetBaseColour()
	{
		return m_BaseColour;
	}
	
	public void SetBaseColour(double[] colour)
	{
        m_ChangeColourBehaviour.SetNextColour(colour);
        m_ChangeColourBehaviour.TriggerBehaviour();

        m_AltColour.SetAsInverse(colour);
	}
	
	public void SetBaseColour(Colour colour)
	{
        m_ChangeColourBehaviour.SetNextColour(colour);
        m_ChangeColourBehaviour.TriggerBehaviour();

        m_AltColour.SetAsInverse(colour);
	}

	public Colour GetColour()
	{
		return m_Colour;
	}

    public Colour GetAltColour() { return m_AltColour; }

	public double GetBoundingRadius()
	{
		return m_BoundingRadius;
	}
	
	public ArrayList<GameObject> GetChildren()
	{
		return m_Children;
	}
	
	public void AddChild(GameObject obj)
	{
		m_Children.add(obj);
	}

	public void RemoveAllChildren()
	{
		m_Children.clear();
	}
	
	public void SetAffiliation(AffiliationKey faction)
	{
		m_Faction = faction;
	}
	
	public AffiliationKey GetAffiliation()
	{
		return m_Faction;
	}
	
	public double GetMass()
	{
		return m_Mass;
	}
	
	public void SetScale(double scale)
	{
		m_Scale.SetVector(scale);
	}
	
	public void SetForward(Vector3 forward)
	{
		m_Forward.SetVector(forward);
	}
	
	public void SetDragCoefficient(double drag)
	{
		m_DragCoefficient = drag;
	}
	
	public void SetMass(double mass)
	{
		m_Mass = mass;
	}
}
