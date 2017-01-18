package com.raggamuffin.protorunnerv2.renderer;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Tentacle;
import com.raggamuffin.protorunnerv2.master.RenderEffectSettings;
import com.raggamuffin.protorunnerv2.master.RendererPacket;
import com.raggamuffin.protorunnerv2.particles.ParticleType;
import com.raggamuffin.protorunnerv2.particles.Particle_Multiplier;
import com.raggamuffin.protorunnerv2.particles.Particle_Standard;
import com.raggamuffin.protorunnerv2.particles.TrailNode;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UIElementType;
import com.raggamuffin.protorunnerv2.utils.FrameRateCounter;

import android.content.Context;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import android.opengl.Matrix;
import android.util.Log;

public class GLRenderer implements GLSurfaceView.Renderer
{
	public static final String TAG = "DEBUG GLRenderer";

	private int m_Width;
	private int m_Height;

    private RendererPacket m_Packet;

	private Context m_Context;

    private GLOrthoCamera m_UICamera;
    private GLCamera m_Camera;

    private FrameBufferEffectRenderer m_FrameBufferRenderer;
    private ModelManager m_ModelManager;
    private UIRenderManager m_UIManager;
    private TrailRenderer m_TrailRenderer;
    private RopeRenderer m_RopeRenderer;
    private ParticleRenderer m_ParticleRenderer;

    private RenderEffectSettings m_RenderEffectSettings;

    private FrameRateCounter m_RenderDurationCounter;
    private FrameRateCounter m_DeltaRender;

	public GLRenderer(RendererPacket packet)
	{
		Log.e(TAG, "GLRenderer");

        m_Packet = packet;
		m_Context 				= m_Packet.GetContext();
		m_Camera 				= new GLCamera(m_Packet.GetCamera());
		m_UICamera 				= new GLOrthoCamera();
		m_RenderEffectSettings 	= m_Packet.GetRenderEffectSettings();

        m_FrameBufferRenderer = new FrameBufferEffectRenderer();
		m_ModelManager = new ModelManager(m_Context, m_RenderEffectSettings);
		m_UIManager    = new UIRenderManager(m_Context);
        m_TrailRenderer = new TrailRenderer();
        m_RopeRenderer = new RopeRenderer();
        m_ParticleRenderer = new ParticleRenderer();

        m_RenderDurationCounter = new FrameRateCounter();
        m_DeltaRender = new FrameRateCounter();
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config)
	{
		Log.e(TAG, "onSurfaceCreated");

		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glCullFace(GLES20.GL_BACK);

		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthMask(true);
        GLES20.glDepthRangef(0.0f, 1.0f);
        GLES20.glClearDepthf(1.0f);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        m_FrameBufferRenderer.LoadAssets(m_Packet);
        m_ModelManager.LoadAssets();
        m_TrailRenderer.LoadAssets();
        m_RopeRenderer.LoadAssets();
        m_UIManager.LoadAssets();
        m_ParticleRenderer.LoadAssets();
    }

	@Override
	public void onDrawFrame(GL10 unused)
	{
        m_RenderDurationCounter.StartFrame();

        m_FrameBufferRenderer.BindFrameBuffer(FrameBufferName.RawRender);

		m_Camera.Update();
		m_UICamera.Update();

        float[] view = new float[16];
        Matrix.setIdentityM(view, 0);
        Matrix.multiplyMM(view, 0, m_Camera.m_ProjMatrix, 0, m_Camera.m_VMatrix, 0);

        DrawSkybox(view);
        DrawFloorPanels(view);
        DrawParticles(view);
        DrawParticles_Multiplier(view);
        DrawObjects(view);
        DrawTrails(view);
        DrawRopes(view);

		DrawUI();

        // Glow horizontal.
        m_FrameBufferRenderer.BindFrameBuffer(FrameBufferName.GlowHorizontal);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_FrameBufferRenderer.GetTextureHandle(FrameBufferName.RawRender));
        m_ModelManager.DrawFBOGlowHoriz();

        // Glow vertical.
        m_FrameBufferRenderer.BindFrameBuffer(FrameBufferName.GlowVertical);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_FrameBufferRenderer.GetTextureHandle(FrameBufferName.GlowHorizontal));
        m_ModelManager.DrawFBOGlowVert();

        // Film grain
        m_FrameBufferRenderer.BindFrameBuffer(FrameBufferName.FilmGrain);
        m_ModelManager.DrawFBOFilmGrain();


		// Render to screen.
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		GLES20.glViewport(0, 0, m_Width, m_Height);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_FrameBufferRenderer.GetTextureHandle(FrameBufferName.RawRender));

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_FrameBufferRenderer.GetTextureHandle(FrameBufferName.GlowVertical));

        GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_FrameBufferRenderer.GetTextureHandle(FrameBufferName.FilmGrain));

        m_ModelManager.DrawFBOFinal();

        m_RenderDurationCounter.EndFrame();
        m_RenderDurationCounter.LogFrameDuration("Renderer", 16L);

        m_DeltaRender.EndFrame();
        m_DeltaRender.LogFrameDuration("Delta", 30L);
        m_DeltaRender.StartFrame();
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height)
	{
		Log.e(TAG, "onSurfaceChanged");

		m_Width = width;
		m_Height = height;

		m_Camera.ViewPortChanged(m_Width, m_Height);
		m_UICamera.ViewPortChanged(m_Width, m_Height);
	}

    private void DrawSkybox(float[] view)
    {
        m_ModelManager.DrawSkyBox(m_Camera.GetPosition(), m_RenderEffectSettings.GetSkyBoxColour(), view);
    }

    private void DrawObjects(float[] view)
    {
        ModelType[] types = ModelType.values();

        for (ModelType type : types)
        {
            ArrayList<GameObject> list = (ArrayList<GameObject>)m_Packet.GetModelList(type).clone();

            if(list.size() > 0)
            {
                m_ModelManager.InitialiseModel(type, view, m_Camera.GetPosition());

                for (GameObject obj : list)
                {
                    if (obj != null)
                    {
                        m_ModelManager.Draw(obj);
                    }
                }

                m_ModelManager.CleanModel(type);
            }
        }
    }

    private void DrawParticles(float[] view)
    {
        ArrayList<Particle_Standard> list = (ArrayList<Particle_Standard>)m_Packet.GetParticles().clone();

        if(list.size() > 0)
        {
            m_ParticleRenderer.Initialise(ParticleType.Standard, view, m_Camera.GetPosition());

            for (Particle_Standard obj : list)
            {
                if (obj != null)
                {
                    m_ParticleRenderer.Draw(obj.GetPosition(), obj.GetColour());
                }
            }

            m_ParticleRenderer.Clean();
        }
    }

    private void DrawParticles_Multiplier(float[] view)
    {
        ArrayList<Particle_Multiplier> list = (ArrayList<Particle_Multiplier>)m_Packet.GetParticles_Multiplier().clone();

        if(list.size() > 0)
        {
            m_ParticleRenderer.Initialise(ParticleType.Multiplier, view, m_Camera.GetPosition());

            for (Particle_Multiplier obj : list)
            {
                if (obj != null)
                {
                    m_ParticleRenderer.Draw(obj.GetPosition(), obj.GetColour());
                }
            }

            m_ParticleRenderer.Clean();
        }
    }

    private void DrawFloorPanels(float[] view)
    {
        ArrayList<FloorGrid> list = (ArrayList<FloorGrid>)m_Packet.GetFloorGrids().clone();

        if(!list.isEmpty())
        {
            m_ModelManager.InitialiseFloorPanel(view);

            for (FloorGrid obj : list)
            {
                if (obj != null)
                {
                    m_ModelManager.DrawFloorPanel(obj.GetPosition(), obj.GetColour(), obj.GetAttenuation());
                }
            }

            m_ModelManager.CleanFloorPanel();
        }
    }

    private void DrawTrails(float[] view)
    {
        ArrayList<TrailNode> list = (ArrayList<TrailNode>)m_Packet.GetTrailPoints().clone();

        if(!list.isEmpty())
        {
            m_TrailRenderer.Initialise(view, m_Camera.GetPosition());

            for (TrailNode obj : list)
            {
                if (obj != null)
                {
                    m_TrailRenderer.Draw(obj);
                }
            }

            m_TrailRenderer.Clean();
        }
    }

    private void DrawRopes(float[] view)
    {
        ArrayList<Tentacle> list = (ArrayList<Tentacle>)m_Packet.GetRopes().clone();

        if(list.size() > 0)
        {
            m_RopeRenderer.Initialise(view, m_Camera.GetPosition());

            for (Tentacle obj : list)
            {
                if (obj != null)
                {
                    m_RopeRenderer.Draw(obj);
                }
            }

            m_RopeRenderer.Clean();
        }
    }

    private void DrawUI()
    {
        float[] view = new float[16];
        Matrix.setIdentityM(view, 0);
        Matrix.multiplyMM(view, 0, m_UICamera.m_ProjMatrix, 0, m_UICamera.m_VMatrix, 0);

        UIElementType[] types = UIElementType.values();

        for (UIElementType type : types)
        {
            ArrayList<UIElement> Copy = (ArrayList<UIElement>) m_Packet.GetUIElementList(type).clone();

            for(UIElement object : Copy)
                m_UIManager.DrawElement(object, view);
        }
    }
}
