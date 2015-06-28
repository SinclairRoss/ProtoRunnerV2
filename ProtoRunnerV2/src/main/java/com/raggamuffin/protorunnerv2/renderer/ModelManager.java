package com.raggamuffin.protorunnerv2.renderer;

import android.content.Context;
import android.opengl.GLES20;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.master.RenderEffectSettings;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ModelManager 
{
	public static final int HORIZ = 0;
	public static final int VERT  = 1;
	public static final int NORM  = 2;
	
	public final int NUM_TEXTURES = 3;
	
	private int m_TextureHandles[];
	private int m_ResourceIDs[];
	
	private RenderEffectSettings m_RenderEffectSettings;
	
	private Vector3  m_EyePos;
	private Vector3	 m_WorldPos;
	
	private Context m_Context;
	
	private GLCube 	 	 m_Cube;
	private GLRunner 	 m_Runner;
	private GLPlane 	 m_Plane;
	private GLFloorPanel m_FloorPanel;
	private GLPulseLaser m_PulseLaser;
	private GLPulseLaser m_RailSlug;
	private GLRing		 m_Ring;
	private GLBit		 m_Bit;
	private GLByte		 m_Byte;
	private GLStandardPoint m_StandardPoint;
	private GLLine		 m_Line;
	private GLMissile	 m_Missile;
	private GLExplosion	 m_Explosion;
	private GLScreenQuad m_Screen;
	private GLSkybox	 m_Skybox;
	private GLRadarFragment m_RadarFragment;
	
	public ModelManager(Context context, RenderEffectSettings Settings)
	{		
		m_TextureHandles 	= new int[NUM_TEXTURES];
		m_ResourceIDs  		= new int[NUM_TEXTURES];
		m_ResourceIDs[0] 	= R.drawable.floor_grid;
		m_ResourceIDs[1] 	= R.drawable.horizon;
		m_ResourceIDs[2]	= R.drawable.radar_fragment;
		
		m_RenderEffectSettings = Settings;
		
		m_EyePos 	 = null;
		m_WorldPos 	 = null;
		
		m_Context 	 = context;
		
		m_Cube 		 = null;
		m_Runner 	 = null;
		m_Plane 	 = null;
		m_FloorPanel = null;
		m_PulseLaser = null;
		m_RailSlug   = null;
		m_Ring		 = null;
		m_Bit 		 = null;
		m_Byte		 = null;
		m_StandardPoint = null;
		m_Line		 = null;
		m_Missile	 = null;
		m_Explosion  = null;
		m_Screen	 = null;
		m_Skybox 	 = null;
		m_RadarFragment = null;
	}
	
	public void LoadAssets(final GLCamera camera)
	{      
		TextureLoader.LoadTextures(m_Context, m_ResourceIDs, m_TextureHandles);
		LoadModels(camera);  
	}
	
	private void LoadModels(final GLCamera camera)
	{
		m_EyePos   	 = camera.GetPosition();
		m_WorldPos 	 = new Vector3();

		m_Cube 		 = new GLCube();
		m_Runner 	 = new GLRunner();
		m_Plane 	 = new GLPlane();
		m_FloorPanel = new GLFloorPanel();	
		m_PulseLaser = new GLPulseLaser(50.0f);
		m_RailSlug	 = new GLPulseLaser(100.0f);
		m_Ring		 = new GLRing();
		m_Bit 		 = new GLBit();
		m_Byte		 = new GLByte();
		m_StandardPoint = new GLStandardPoint();
		m_Line 	 	 = new GLLine();
		m_Missile	 = new GLMissile();
		m_Explosion	 = new GLExplosion();	
		m_Screen	 = new GLScreenQuad();
		m_Skybox	 = new GLSkybox();
		m_RadarFragment = new GLRadarFragment();
	}
	
	public void DrawModel(GameObject object, final float[] mvpMatrix)
	{
		switch(object.GetModel())
		{
			case Cube:
				m_Cube.SetColour(object.GetColour());
				m_Cube.draw(mvpMatrix);
				break;
			
			case Runner:
				m_Runner.SetColour(object.GetColour());
				m_Runner.draw(mvpMatrix);
				break;

			case Plane:
				GLES20.glDisable(GLES20.GL_DEPTH_TEST);
				
				m_Plane.SetColour(object.GetColour());
				m_Plane.draw(mvpMatrix);
				
				GLES20.glEnable(GLES20.GL_DEPTH_TEST);
				break;
				
			case FloorPanel:
				
				GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_TextureHandles[0]);
				
				GLES20.glDisable(GLES20.GL_DEPTH_TEST);
				
				if(m_EyePos.J < 0.0);
					GLES20.glDisable(GLES20.GL_CULL_FACE);
				
				m_FloorPanel.SetColour(object.GetColour());
				m_FloorPanel.SetWorldPos(m_WorldPos);
				m_FloorPanel.draw(mvpMatrix);
				
				GLES20.glEnable(GLES20.GL_DEPTH_TEST);
				GLES20.glEnable(GLES20.GL_CULL_FACE);
				break;
			
			case PulseLaser:
				m_PulseLaser.SetEyePos(m_EyePos);
				m_PulseLaser.SetWorldPos(m_WorldPos);
				m_PulseLaser.SetColour(object.GetColour());
				m_PulseLaser.draw(mvpMatrix);

				break;
				
			case RailSlug:
				m_RailSlug.SetEyePos(m_EyePos);
				m_RailSlug.SetWorldPos(m_WorldPos);
				m_RailSlug.SetColour(object.GetColour());
				m_RailSlug.draw(mvpMatrix);
				break;

			case Ring:
				m_Ring.SetColour(object.GetColour());
                m_Ring.SetEndPointColour(object.GetAltColour());
				m_Ring.draw(mvpMatrix);
				break;

			case Bit:
				m_Bit.SetColour(object.GetColour());
				m_Bit.draw(mvpMatrix);
				break;
				
			case Byte:
				m_Byte.SetColour(object.GetColour());
				m_Byte.draw(mvpMatrix);			
				break;
				
			case StandardPoint:
				m_StandardPoint.SetEyePos(m_EyePos);
				m_StandardPoint.SetWorldPos(m_WorldPos);
				m_StandardPoint.SetColour(object.GetColour());
				m_StandardPoint.draw(mvpMatrix);

				break;

			case LaserPointer:
				GLES20.glLineWidth(2.0f);
				m_Line.SetColour(object.GetColour());
                m_Line.SetEndPointColour(object.GetAltColour());
				m_Line.draw(mvpMatrix);
				break;

			case Missile:			
				m_Missile.SetColour(object.GetColour());
				m_Missile.draw(mvpMatrix);
				break;
			
			case Explosion:
				
				GLES20.glDisable(GLES20.GL_CULL_FACE);
				GLES20.glDisable(GLES20.GL_DEPTH_TEST);
				
				m_Explosion.SetColour(object.GetColour());
				m_Explosion.draw(mvpMatrix);
				
				GLES20.glEnable(GLES20.GL_CULL_FACE);
				GLES20.glEnable(GLES20.GL_DEPTH_TEST);
				
				break;

			case RadarFragment:
				GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_TextureHandles[2]);
				
				m_RadarFragment.SetColour(object.GetColour());
				m_RadarFragment.draw(mvpMatrix);
				break;
				
			case Nothing:
				// Do nothing.
				break;
		}
	}

    public void DrawSkyBox(final float[] mvpMatrix, Colour colour)
    {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_TextureHandles[1]);
        m_Skybox.SetColour(colour);
        m_Skybox.draw(mvpMatrix);
    }

	public void DrawFBOGlowHoriz()
	{
		m_Screen.SetHorizontalGlowIntensity((float) m_RenderEffectSettings.GetGlowIntensityHoriz());
		m_Screen.draw(HORIZ);
	}
	
	public void DrawFBOGlowVert()
	{
		m_Screen.SetVerticalGlowIntensity((float) m_RenderEffectSettings.GetGlowIntensityVert());
		m_Screen.draw(VERT);
	}
	
	public void DrawFBOFinal()
	{
		m_Screen.draw(NORM);
	}
	
	public void SetWorldPosition(final Vector3 worldPos)
	{
		m_WorldPos.I = worldPos.I;
		m_WorldPos.J = worldPos.J;
		m_WorldPos.K = worldPos.K;
	}
}