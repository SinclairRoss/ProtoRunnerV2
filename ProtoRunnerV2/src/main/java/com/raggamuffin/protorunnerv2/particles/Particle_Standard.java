package com.raggamuffin.protorunnerv2.particles;

import com.raggamuffin.protorunnerv2.gamelogic.GameLogic;
import com.raggamuffin.protorunnerv2.gameobjects.RenderObjectType;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

import java.util.ArrayList;

public class Particle_Standard extends Particle
{
	private ArrayList<Graviton> m_Gravitons;
	
	public Particle_Standard(GameLogic game, ArrayList<Graviton> gravitons)
	{
		super(game, ModelType.StandardPoint);

        m_Gravitons = gravitons;

        m_DragCoefficient = 0.9;
        m_FadeIn = 0.2;
        m_FadeOut = 0.7;
	}
	
	@Override
	public void Update(double deltaTime)
	{
		super.Update(deltaTime);
		
		for(Graviton grav : m_Gravitons)
		{
			grav.GetGravitonBehaviour().ApplyGravitonForce(this);
		}
	}

    @Override
    public void CleanUp()
    {

    }
}
