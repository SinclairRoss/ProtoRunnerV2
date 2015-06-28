package com.raggamuffin.protorunnerv2.gameobjects;

import com.raggamuffin.protorunnerv2.colours.ColourBehaviour;
import com.raggamuffin.protorunnerv2.colours.ColourBehaviour_Pulse;
import com.raggamuffin.protorunnerv2.renderer.ModelType;
import com.raggamuffin.protorunnerv2.utils.Colours;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;

public class Block extends GameObject
{
	protected ColourBehaviour m_AmbientBehaviour;
	
	public Block()
	{
		super(null, null);
	
		m_Model = ModelType.Cube;
		int BlockColour = MathsHelper.RandomInt(0, 3);
		
		switch(BlockColour)
		{
			case 0:
				m_BaseColour.SetColour(Colours.ChaserOrange);
				break;
			
			case 1:
				m_BaseColour.SetColour(Colours.Crimson);
				break;
			
			case 2:
				m_BaseColour.SetColour(Colours.EmeraldGreen);
				break;
				
			case 3:
				m_BaseColour.SetColour(Colours.BlockPurple);
				break;

			default:
				throw(new RuntimeException("Block.as: case taken is out of range. Possible fault in MathsHelper.RandomInt(Min,Max)"));
		}
		
		m_AmbientBehaviour = new ColourBehaviour_Pulse(this, ColourBehaviour.ActivationMode.Continuous);	
		AddColourBehaviour(m_AmbientBehaviour);

		AddChild(new FloorGrid(m_Colour));
	}
	
	@Override
	public boolean IsValid()
	{
		return true;
	}


}
