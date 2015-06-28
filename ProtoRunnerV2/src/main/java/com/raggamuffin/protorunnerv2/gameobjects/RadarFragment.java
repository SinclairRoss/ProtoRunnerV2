package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.audio.GameAudioManager;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour.ActivationMode;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_LerpTo;
import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.pubsub.PubSubHub;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
import com.raggamuffin.protorunnerv2.utils.Spring3;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class RadarFragment extends GameObject
{
	private Vector3 m_Offset;
	private double m_Heat;
	
	private final double MIN_DEPTH;
	private final double MAX_DEPTH;	
	private final double MIN_ALPHA = 0.0;
	private final double MAX_ALPHA = 0.2;
	private double m_Depth;
	
	private ColourBehaviour_LerpTo m_ColourBehaviour;
	private Vector3 m_NormalisedRadarPos;
	
	private Spring3 m_Spring;
	
	private RadarSignatureType m_RadarSignatureType;

	private Colour m_FriendlyColour;
	private Colour m_EnemyColour;
	private Colour m_NeutralColour;
	
	public RadarFragment(GameLogic game, double min, double max, double x, double y, double radarRadius)
	{
		super(game.GetPubSubHub(), game.GetGameAudioManager());

		MIN_DEPTH = min;
		MAX_DEPTH = max;	
		m_Depth = 0.0;
		
		m_Model = ModelType.RadarFragment;
		SetBaseColour(Colours.PastelBlue);
		
		m_BaseColour = GetBaseColour();
		m_BaseColour.Alpha = MIN_ALPHA;
		
		m_ColourBehaviour = new ColourBehaviour_LerpTo(this, ActivationMode.Continuous);
		m_ColourBehaviour.SetAltColour(Colours.PastelBlue);
		AddColourBehaviour(m_ColourBehaviour);
		
		m_Heat = 0.0;
		
		m_Offset = new Vector3(x, 0 ,y);
		
		m_NormalisedRadarPos = new Vector3(m_Offset);
		m_NormalisedRadarPos.Scale(1 / radarRadius);
	
		m_Spring = new Spring3(m_Offset);
		
		m_RadarSignatureType = RadarSignatureType.None;
		
		m_FriendlyColour 	= game.GetColourManager().GetPrimaryColour();
		m_EnemyColour 		= game.GetColourManager().GetAccentingColour();
		m_NeutralColour 	= new Colour(Colours.PastelGrey);
	}
	
	@Override
	public void Update(double deltaTime)
	{
		super.Update(deltaTime);

		m_Heat += MathsHelper.RandomDouble(0, 0.4);
		m_Depth = MathsHelper.Lerp(m_Heat, MIN_DEPTH, MAX_DEPTH);
		
		m_Spring.SetRelaxedPosition(m_Offset.I, m_Depth, m_Offset.K);
		m_Spring.Update(deltaTime);
		
		m_Position.Add(m_Offset);

		m_ColourBehaviour.SetIntensity(m_Heat);
		double normalisedDepth = MathsHelper.Normalise(m_Position.J, MIN_DEPTH, MAX_DEPTH);
		m_BaseColour.Alpha = MathsHelper.Lerp(normalisedDepth, MIN_ALPHA, MAX_ALPHA);
	}
	
	public void SetSignitureType(RadarSignatureType type)
	{	
		switch(type)
		{
			case Player:
				m_ColourBehaviour.SetAltColour(m_FriendlyColour);	
				m_RadarSignatureType = type;
				break;
				
			case Enemy:
				if(m_RadarSignatureType == RadarSignatureType.Player)
					break;
				
				m_ColourBehaviour.SetAltColour(m_EnemyColour);
				m_RadarSignatureType = type;
				break;
			
			case Wingman:
				if(m_RadarSignatureType == RadarSignatureType.Player ||
				   m_RadarSignatureType == RadarSignatureType.Enemy	)
					break;
				
				m_ColourBehaviour.SetAltColour(m_FriendlyColour);
				m_RadarSignatureType = type;
				break;	
		}
	}

	public void HeatUp()
	{
		m_Heat = 1.0;
	}
	
	public void CoolDown()
	{
		m_Heat = 0.0;
		m_RadarSignatureType = RadarSignatureType.None;
		m_ColourBehaviour.SetAltColour(m_NeutralColour);
	}
	
	@Override
	public boolean IsValid() 
	{
		return true;
	}
	
	public Vector3 GetNormalisedRadarPosition()
	{
		return m_NormalisedRadarPos;
	}
}