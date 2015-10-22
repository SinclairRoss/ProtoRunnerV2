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

	private Context m_Context;

	private GLFloorPanel m_FloorPanel;
    private GLModel      m_PlasmaPulse;
	private GLRing		 m_Ring;
	private GLLine		 m_Pointer;
	private GLScreenQuad m_Screen;
	private GLSkybox	 m_Skybox;
	private GLRadarFragment m_RadarFragment;

    private GLModel_StandardObject m_Runner;
    private GLModel_StandardObject m_Bit;
    private GLModel_StandardObject m_Byte;
    private GLModel_StandardObject m_Mine;
    private GLModel_StandardObject m_EngineDrone;
    private GLModel_StandardObject m_Missile;
    private GLModel_StandardObject m_Carrier;
    private GLModel_StandardObject m_Dummy;
    private GLModel_StandardObject m_WeaponDrone;
    private GLModel_HollowObject   m_Explosion;
    private GLModel_LaserBeam      m_ParticleLaser;

    public ModelManager(Context context, RenderEffectSettings Settings)
	{		
		m_TextureHandles 	= new int[NUM_TEXTURES];
		m_ResourceIDs  		= new int[NUM_TEXTURES];
		m_ResourceIDs[0] 	= R.drawable.floor_grid;
		m_ResourceIDs[1] 	= R.drawable.horizon;
		m_ResourceIDs[2]	= R.drawable.radar_fragment;
		
		m_RenderEffectSettings = Settings;

		m_Context 	 = context;
	}
	
	public void LoadAssets()
	{      
		TextureLoader.LoadTextures(m_Context, m_ResourceIDs, m_TextureHandles);
		LoadModels();
	}
	
	private void LoadModels()
    {
        m_FloorPanel = new GLFloorPanel();
        m_Ring = new GLRing();
        m_Pointer = new GLLine(2.0f);
        m_ParticleLaser = new GLModel_LaserBeam();
        m_PlasmaPulse = new GLModel_PlasmaPulse();

        m_Screen = new GLScreenQuad();
        m_Skybox = new GLSkybox();
        m_RadarFragment = new GLRadarFragment();

        m_Runner        = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.runner_vertices));
        m_Bit           = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.bit_vertices));
        m_Byte          = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.byte_vertices));
        m_Mine          = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.mine_vertices));
        m_EngineDrone   = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.enginedrone_vertices));
        m_Missile       = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.missile_vertices));
        m_Carrier       = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.carrier_vertices));
        m_Dummy         = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.dummy_vertices));
        m_Explosion     = new GLModel_HollowObject(ReadFloatArrayFromResource(R.string.explosion_vertices));
        m_WeaponDrone   = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.weapondrone_vertices));
    }

    private float[] ReadFloatArrayFromResource(int resource)
    {
        String raw = m_Context.getString(resource);
        raw = raw.replaceAll("\\s","");
        String[] rawArray = raw.split(",");

        int numValues = rawArray.length;
        float[] array = new float[numValues];

        for(int i = 0; i < numValues; i ++)
            array[i] = Float.parseFloat(rawArray[i]);

        return array;
    }
	
	public void Draw(GameObject object)
	{
        GLModel model = GetModel(object.GetModel());

        if(model == null)
            return;

        model.Draw(object);
	}

    public void InitialiseModel(ModelType type, float[] projMatrix, Vector3 eye)
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
            model.InitialiseModel(projMatrix, eye);
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
            case Runner:
                return m_Runner;
            case Bit:
                return m_Bit;
            case Byte:
                return m_Byte;
            case Mine:
                return m_Mine;
            case EngineDrone:
                return m_EngineDrone;
            case Missile:
                return m_Missile;
            case Carrier:
                return m_Carrier;
            case Dummy:
                return m_Dummy;
            case FloorPanel:
                return m_FloorPanel;
            case Ring:
                break;
            case LaserPointer:
                return m_Pointer;
            case ParticleLaser:
                return m_ParticleLaser;
            case PlasmaPulse:
                return m_PlasmaPulse;
            case Explosion:
                return m_Explosion;
            case Skybox:
                return m_Skybox;
            case RadarFragment:
                return m_RadarFragment;
            case WeaponDrone:
                return m_WeaponDrone;
            case Nothing:
                break;
        }

        return null;
    }

    public void DrawSkyBox(Vector3 pos, Colour colour, final float[] projMatrix)
    {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_TextureHandles[1]);

        m_Skybox.InitialiseModel(projMatrix, pos);

        m_Skybox.SetColour(colour);
        m_Skybox.draw(pos);

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