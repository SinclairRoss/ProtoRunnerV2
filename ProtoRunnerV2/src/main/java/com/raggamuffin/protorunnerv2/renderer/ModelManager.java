package com.raggamuffin.protorunnerv2.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.master.RenderEffectSettings;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector3;

public class ModelManager 
{
	public static final int HORIZ = 0;
	public static final int VERT  = 1;
    public static final int GRAIN = 2;
	public static final int NORM  = 3;
	
	public final int NUM_TEXTURES = 3;
	
	private int m_TextureHandles[];
	private int m_ResourceIDs[];
	
	private RenderEffectSettings m_RenderEffectSettings;

	private Context m_Context;

	private GLModel_FloorPanel m_FloorPanel;
	private GLModel_Ring m_Ring;
	private GLScreenQuad m_Screen;
	private GLModel_HorizonRing m_HorizonRing;
	private GLModel_RadarFragment m_RadarFragment;

    private GLModel_SolidObject m_ParticleLaser;
    private GLModel_SolidObject m_PlasmaPulse;
    private GLModel_SolidObject m_SpawnPillar;
    private GLModel_SolidObjectPriority m_LaserPointer;

    private GLModel_StandardObject m_Runner;
    private GLModel_StandardObject m_Bit;
    private GLModel_StandardObject m_Byte;
    private GLModel_StandardObject m_ShieldBearer;
    private GLModel_StandardObject m_Mine;
    private GLModel_StandardObject m_EngineDrone;
    private GLModel_StandardObject m_Missile;
    private GLModel_StandardObject m_Carrier;
    private GLModel_StandardObject m_Dummy;
    private GLModel_StandardObject m_WeaponDrone;
    private GLModel_StandardObject m_ThreePointStar;
    private GLModel_PhasedObject m_Shield;

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
        m_FloorPanel = new GLModel_FloorPanel();
        m_Ring = new GLModel_Ring();
        m_LaserPointer = new GLModel_SolidObjectPriority(ReadFloatArrayFromResource(R.string.laser_vertices));
        m_ParticleLaser = new GLModel_SolidObject(ReadFloatArrayFromResource(R.string.laser_vertices));
        m_PlasmaPulse = new GLModel_SolidObject(ReadFloatArrayFromResource(R.string.plasma_vertices));
        m_SpawnPillar = new GLModel_SolidObject(ReadFloatArrayFromResource(R.string.spawn_pillar));

        m_Screen = new GLScreenQuad();
        m_HorizonRing = new GLModel_HorizonRing();
        m_RadarFragment = new GLModel_RadarFragment();

        m_Runner = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.runner_vertices));
        m_Bit = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.bit_vertices));
        m_Byte = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.byte_vertices));
        m_Mine = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.mine_vertices));
        m_ShieldBearer = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.shieldbearer_vertices));
        m_EngineDrone = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.enginedrone_vertices));
        m_Missile = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.missile_vertices));
        m_Carrier = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.carrier_vertices));
        m_Dummy = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.dummy_vertices));
        m_WeaponDrone = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.weapondrone_vertices));
        m_ThreePointStar = new GLModel_StandardObject(ReadFloatArrayFromResource(R.string.three_point_star));
        m_Shield = new GLModel_PhasedObject(ReadFloatArrayFromResource(R.string.shield_vertices));
    }

    private float[] ReadFloatArrayFromResource(int resource)
    {
        String raw = m_Context.getString(resource);
        raw = raw.replaceAll("\\s","");
        String[] rawArray = raw.split(",");

        int numValues = rawArray.length;
        float[] array = new float[numValues];

        for(int i = 0; i < numValues; i ++)
        {
            array[i] = Float.parseFloat(rawArray[i]);
        }

        return array;
    }
	
	public void Draw(GameObject object)
    {
        GLModel model = GetModel(object.GetModel());

        if(model != null)
        {
            model.Draw(object);
        }
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
        {
            model.InitialiseModel(projMatrix, eye);
        }
    }

    public void CleanModel(ModelType type)
    {
        GLModel model = GetModel(type);

        if(model != null)
        {
            model.CleanModel();
        }
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
                return m_Ring;
            case Trail:
                return null;
            case LaserPointer:
                return m_LaserPointer;
            case ParticleLaser:
                return m_ParticleLaser;
            case PlasmaPulse:
                return m_PlasmaPulse;
            case SpawnPillar:
                return m_SpawnPillar;
            case RadarFragment:
                return m_RadarFragment;
            case WeaponDrone:
                return m_WeaponDrone;
            case ThreePointStar:
                return m_ThreePointStar;
            case ShieldBearer:
                return m_ShieldBearer;
            case Shield:
                return m_Shield;
            case Nothing:
                return null;
            case PlasmaShot:
                return null;
            case RailSlug:
                return null;
            case StandardPoint:
                return null;
            default:
                Log.e("ModelManager.java", "ModelType: '" + type + "' not found.");
                return null;
        }
    }

    public void DrawSkyBox(Vector3 pos, Colour colour, final float[] projMatrix)
    {
        m_HorizonRing.InitialiseModel(projMatrix);
        m_HorizonRing.Draw(pos, colour);
        m_HorizonRing.CleanModel();
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

    public void DrawFBOFilmGrain()
    {
        m_Screen.draw(GRAIN);
    }

	public void DrawFBOFinal()
	{
        m_Screen.SetFilmGrainIntensity((float) m_RenderEffectSettings.GetFilmGrainIntensity());
		m_Screen.draw(NORM);
	}
}