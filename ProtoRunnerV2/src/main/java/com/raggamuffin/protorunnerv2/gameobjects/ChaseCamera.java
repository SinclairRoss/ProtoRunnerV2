package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.utils.Spring3;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ChaseCamera
{
	///// Camera Attributes \\\\\
	private Vector3 m_Position;			// Position of the camera.
	private Vector3 m_Up;				// The up vector of the camera;
	private Vector3 m_LookAt;			// Where the camera is looking.
	
	private Vector3 m_PositionOffset;	// Position of the springs relaxed state relative to the Chase Object.
	private Vector3 m_RelaxedPosition;	// The Position of the Relaxed Position.

    private Vector3 m_Force;
    private Vector3 m_Acceleration;
    private Vector3 m_Velocity;
    private double m_Mass;
    private double m_DragCoefficient;

	///// Chase Attributes \\\\\
	private GameObject m_ChaseObject;	// The object the camera is following.
    private GameObject m_LookObject;       // The object the camera is looking at.

	private Spring3 m_Spring;

	public ChaseCamera()
	{
		///// Camera Attributes \\\\\
		m_Position 			= new Vector3(0.0,  3.0, 5.0);
		m_Up 				= new Vector3(0.0,  1.0,  0.0);
		m_LookAt 			= new Vector3();
		m_RelaxedPosition 	= new Vector3();
		m_PositionOffset 	= new Vector3();

        m_Force = new Vector3();
        m_Acceleration = new Vector3();
        m_Velocity = new Vector3();
        m_Mass = 0.5;
        m_DragCoefficient = 0.85;

		///// Spring Attributes \\\\\
		m_Spring = new Spring3(0.08, 0);

        NormalCam();
    }
	
	public void Attach(GameObject chaseObject)
	{
		m_ChaseObject = chaseObject;
        m_LookObject  = chaseObject;
	}

    public void SetLookAt(GameObject lookObject)
    {
        m_LookObject = lookObject;
    }
	
	public void Update(double deltaTime)
	{
        CalculateRelaxedPosition();
        m_Force.SetVector(m_Spring.CalculateSpringForce(m_Position, m_RelaxedPosition));

        CalculateAcceleration();
        CalculateVelocity();
        UpdatePosition(deltaTime);

        CalculateLookAt();
	}
	
	private void CalculateLookAt()
	{
        Vector3 lookPosition = m_LookObject.GetPosition();
        Vector3 lookForward  = m_LookObject.GetForward();

		m_LookAt.I = lookPosition.I + lookForward.I * 10.0;
		m_LookAt.J = lookPosition.J + lookForward.J * 10.0;
		m_LookAt.K = lookPosition.K + lookForward.K * 10.0;
	}
	
	private void CalculateRelaxedPosition()
	{
        Vector3 chasePosition = m_ChaseObject.GetPosition();
        Vector3 chaseForward  = m_ChaseObject.GetForward();

		m_RelaxedPosition.I = chasePosition.I + (chaseForward.I * m_PositionOffset.I);
		m_RelaxedPosition.J = chasePosition.J + (m_PositionOffset.J);
		m_RelaxedPosition.K = chasePosition.K + (chaseForward.K * m_PositionOffset.K);
	}

    private void CalculateAcceleration()
    {
        m_Acceleration.I = (m_Force.I / m_Mass);
        m_Acceleration.J = (m_Force.J / m_Mass);
        m_Acceleration.K = (m_Force.K / m_Mass);
    }

    private void CalculateVelocity()
    {
        m_Velocity.Add(m_Acceleration);
        m_Velocity.Scale(m_DragCoefficient);
    }

    private void UpdatePosition(double deltaTime)
    {
        m_Position.I += m_Velocity.I * deltaTime;
        m_Position.J += m_Velocity.J * deltaTime;
        m_Position.K += m_Velocity.K * deltaTime;
    }

	public void SetInPlace()
	{
		m_Position.SetVector(m_RelaxedPosition);
	}
	
	public Vector3 GetPosition()
	{
		return m_Position;
	}

	public Vector3 GetLookAt()
	{
		return m_LookAt;
	}
	
	public Vector3 GetUp()
	{
		return m_Up;
	}

    public void SetUp(double i, double j, double k)
    {
        m_Up.SetVector(i,j,k);
    }

    public void SprintCam()
    {
        m_Spring.SetStiffness(1.0);
        m_PositionOffset.SetVector(0, 1.5, 0);
    }

    public void NormalCam()
    {
        m_Spring.SetStiffness(0.1);
        m_PositionOffset.SetVector(-5, 10, -5); //-5 10 -2
    }
}
































