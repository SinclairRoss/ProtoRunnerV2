package com.raggamuffin.protorunnerv2.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.master.RenderEffectSettings;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.MathsHelper;
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
	
	private Vector3 m_EyePos;
    private Vector3 m_ToEye;
	
	private Context m_Context;
	
	private GLCube 	 	 m_Cube;
	private GLRunner     m_Runner;
	private GLPlane 	 m_Plane;
	private GLFloorPanel m_FloorPanel;
	private GLPulseLaser m_PulseLaser;
	private GLPulseLaser m_RailSlug;
	private GLRing		 m_Ring;
	private GLBit		 m_Bit;
	private GLByte		 m_Byte;
    private GLMine       m_Mine;
	private GLStandardPoint m_StandardPoint;
	private GLLine		 m_Pointer;
    private GLLine       m_Trail;
    private GLLine       m_ParticleLaser;
	private GLMissile	 m_Missile;
	private GLExplosion	 m_Explosion;
	private GLScreenQuad m_Screen;
	private GLSkybox	 m_Skybox;
	private GLRadarFragment m_RadarFragment;
    private GLDummy m_Dummy;
	
	public ModelManager(Context context, RenderEffectSettings Settings)
	{		
		m_TextureHandles 	= new int[NUM_TEXTURES];
		m_ResourceIDs  		= new int[NUM_TEXTURES];
		m_ResourceIDs[0] 	= R.drawable.floor_grid;
		m_ResourceIDs[1] 	= R.drawable.horizon;
		m_ResourceIDs[2]	= R.drawable.radar_fragment;
		
		m_RenderEffectSettings = Settings;
		
		m_EyePos 	 = null;
        m_ToEye      = null;
		
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
        m_Mine       = null;
        m_Pointer	 = null;
        m_Trail      = null;
        m_ParticleLaser = null;
		m_Missile	 = null;
		m_Explosion  = null;
		m_Screen	 = null;
		m_Skybox 	 = null;
		m_RadarFragment = null;
        m_Dummy = null;
	}
	
	public void LoadAssets(final GLCamera camera)
	{      
		TextureLoader.LoadTextures(m_Context, m_ResourceIDs, m_TextureHandles);
		LoadModels(camera);  
	}
	
	private void LoadModels(final GLCamera camera)
	{
		m_EyePos   	 = camera.GetPosition();
        m_ToEye      = new Vector3();

		m_Cube 		 = new GLCube();
		m_Runner 	 = new GLRunner();
		m_Plane 	 = new GLPlane();
		m_FloorPanel = new GLFloorPanel();	
		m_PulseLaser = new GLPulseLaser(50.0f);
		m_RailSlug	 = new GLPulseLaser(100.0f);
		m_Ring		 = new GLRing();
		m_Bit 		 = new GLBit();
		m_Byte		 = new GLByte();
        m_Mine       = new GLMine();
		m_StandardPoint = new GLStandardPoint();
        m_Pointer 	 = new GLLine(2.0f);
        m_Trail      = new GLLine(20.0f);
        m_ParticleLaser = new GLLine(3.0f);
		m_Missile	 = new GLMissile();
		m_Explosion	 = new GLExplosion();	
		m_Screen	 = new GLScreenQuad();
		m_Skybox	 = new GLSkybox();
		m_RadarFragment = new GLRadarFragment();
        m_Dummy      = new GLDummy();
	}
	
	public void Draw(GameObject object, float[] projMatrix)
	{
		switch(object.GetModel())
		{
            // unused.
			case Cube:
				m_Cube.SetColour(object.GetColour());
			//	m_Cube.draw(mvpMatrix);
                Log.e("testy test", "<--------- Cube --------->");
				break;

			case Runner:
				m_Runner.SetColour(object.GetColour());
                m_Runner.draw(object.GetPosition(), object.GetScale(), object.GetForward(), (float) object.GetRoll(), (float) object.GetYaw(), projMatrix);

                break;

            // unused.
			case Plane:
			//	GLES20.glDisable(GLES20.GL_DEPTH_TEST);
				
			//	m_Plane.SetColour(object.GetColour());
			//	m_Plane.draw(mvpMatrix);
				
			//	GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                Log.e("testy test", "<--------- Plane --------->");
				break;
				
			case FloorPanel:
				m_FloorPanel.SetColour(object.GetColour());
				m_FloorPanel.SetWorldPos(object.GetPosition());
				m_FloorPanel.draw(object.GetPosition(), projMatrix);

				break;
			
			case PulseLaser:
				m_PulseLaser.SetColour(object.GetColour());
                m_PulseLaser.draw(object.GetPosition(), m_EyePos, projMatrix);

				break;
				
			case RailSlug:
				m_RailSlug.SetColour(object.GetColour());
                m_RailSlug.draw(object.GetPosition(), m_EyePos, projMatrix);
				break;

            // unused.
			case Ring:
			//	m_Ring.SetColour(object.GetColour());
             //   m_Ring.SetEndPointColour(object.GetAltColour());
			//	m_Ring.draw(mvpMatrix);
                Log.e("testy test", "<--------- Ring --------->");
				break;

			case Bit:
				m_Bit.SetColour(object.GetColour());
                m_Bit.draw(object.GetPosition(), object.GetScale(), object.GetForward(), (float) object.GetRoll(), (float) object.GetYaw(), projMatrix);
				break;
				
			case Byte:
				m_Byte.SetColour(object.GetColour());
				m_Byte.draw(object.GetPosition(), object.GetScale(), object.GetForward(), (float) object.GetRoll(), (float) object.GetYaw(), projMatrix);
                break;

            case Mine:
                m_Mine.SetColour(object.GetColour());
                m_Mine.draw(object.GetPosition(), object.GetScale(), object.GetForward(), (float) object.GetRoll(), (float) object.GetYaw(), projMatrix);

                break;

			case StandardPoint:
				m_StandardPoint.SetColour(object.GetColour());
				m_StandardPoint.draw(object.GetPosition(), m_EyePos, projMatrix);

				break;

            case Trail:
            {
                m_ToEye.SetVectorDifference(m_EyePos, object.GetPosition());
                float dist = (float) m_ToEye.GetLength();

                GLES20.glLineWidth((float) (20.0f * MathsHelper.FastInverseSqrt(dist)));

                m_Trail.SetColour(object.GetColour());
                m_Trail.SetEndPointColour(object.GetAltColour());
                m_Trail.draw(object.GetPosition(), object.GetScale(), (float) object.GetYaw(), projMatrix);
                break;
            }
			case LaserPointer:

                m_Pointer.SetColour(object.GetColour());
                m_Pointer.SetEndPointColour(object.GetAltColour());
                m_Pointer.draw(object.GetPosition(), object.GetScale(), (float)object.GetYaw(), projMatrix);
				break;

            case ParticleLaser:
            {
                m_ToEye.SetVectorDifference(m_EyePos, object.GetPosition());
                float dist = (float) m_ToEye.GetLength();

                GLES20.glLineWidth((float) (6.0f * MathsHelper.FastInverseSqrt(dist)));

                m_ParticleLaser.SetColour(object.GetAltColour());
                m_ParticleLaser.SetEndPointColour(object.GetColour());
                m_ParticleLaser.draw(object.GetPosition(), object.GetScale(), (float) object.GetYaw(), projMatrix);
                break;
            }
			case Missile:			
				m_Missile.SetColour(object.GetColour());
				m_Missile.draw(object.GetPosition(), object.GetScale(), object.GetForward(), (float) object.GetRoll(), (float) object.GetYaw(), projMatrix);

                break;
			
			case Explosion:
				m_Explosion.SetColour(object.GetColour());
				m_Explosion.draw(object.GetPosition(), object.GetScale(), object.GetForward(), (float) object.GetRoll(), (float) object.GetYaw(), projMatrix);
				break;

			case RadarFragment:
				m_RadarFragment.SetColour(object.GetColour());
				m_RadarFragment.draw(object.GetPosition(), projMatrix);
				break;

            case Dummy:
                m_Dummy.SetColour(object.GetColour());
                m_Dummy.draw(object.GetPosition(), object.GetScale(), object.GetForward(), (float) object.GetRoll(), (float) object.GetYaw(), projMatrix);
                break;
				
			case Nothing:
				// Do nothing.
				break;

		}
	}

    public void InitialiseType(ModelType type)
    {
        switch(type)
        {
            case FloorPanel:
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_TextureHandles[0]);

                break;

            case RadarFragment:
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_TextureHandles[2]);

                break;
        }

        GLModel model = GetModel(type);

        if(model != null)
            model.InitialiseModel();
    }

    public void CleanModel(ModelType type)
    {
        GLModel model = GetModel(type);

        if(model != null)
            model.CleanModel();
    }

    public GLModel GetModel(ModelType type)
    {
        switch(type)
        {
            case Cube:
                break;
            case Runner:
                return m_Runner;
            case Bit:
                return m_Bit;
            case Byte:
                return m_Byte;
            case Mine:
                return m_Mine;
            case Missile:
                return m_Missile;
            case Dummy:
                return m_Dummy;
            case Plane:
                break;
            case FloorPanel:
                return m_FloorPanel;
            case PulseLaser:
                return m_PulseLaser;
            case RailSlug:
                return m_RailSlug;
            case Ring:
                break;
            case StandardPoint:
                return m_StandardPoint;
            case Trail:
                return m_Trail;
            case LaserPointer:
                return m_Pointer;
            case ParticleLaser:
                return m_ParticleLaser;
            case Explosion:
                return m_Explosion;
            case Skybox:
                return m_Skybox;
            case RadarFragment:
                return m_RadarFragment;
            case Nothing:
                break;
        }

        return null;
    }

    public void DrawSkyBox(Vector3 pos, Colour colour, final float[] projMatrix)
    {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_TextureHandles[1]);

        m_Skybox.InitialiseModel();

        m_Skybox.SetColour(colour);
        m_Skybox.draw(pos, projMatrix);

        m_Skybox.CleanModel();
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
}