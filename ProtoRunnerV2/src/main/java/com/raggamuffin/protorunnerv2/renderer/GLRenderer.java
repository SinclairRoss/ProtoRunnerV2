package com.raggamuffin.protorunnerv2.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.raggamuffin.protorunnerv2.gameobjects.FloorGrid;
import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.gameobjects.Tentacle;
import com.raggamuffin.protorunnerv2.master.RenderEffectSettings;
import com.raggamuffin.protorunnerv2.master.RendererPacket;
import com.raggamuffin.protorunnerv2.particles.Particle;
import com.raggamuffin.protorunnerv2.particles.ParticleType;
import com.raggamuffin.protorunnerv2.particles.Trail;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UIElementType;
import com.raggamuffin.protorunnerv2.utils.FrameRateCounter;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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

    private float[] m_ViewMatrix;
    private float[] m_ViewMatrix_UI;
    private ModelType[] m_ModelTypes;

	public GLRenderer(RendererPacket packet)
	{
		Log.e(TAG, "GLRenderer");

        m_Packet = packet;
		m_Context = m_Packet.GetContext();
		m_Camera = new GLCamera(m_Packet.GetCamera());
		m_UICamera = new GLOrthoCamera();
		m_RenderEffectSettings = m_Packet.GetRenderEffectSettings();

        m_FrameBufferRenderer = new FrameBufferEffectRenderer();
		m_ModelManager = new ModelManager(m_Context, m_RenderEffectSettings);
		m_UIManager    = new UIRenderManager(m_Context);
        m_TrailRenderer = new TrailRenderer();
        m_RopeRenderer = new RopeRenderer();
        m_ParticleRenderer = new ParticleRenderer();

        m_RenderDurationCounter = new FrameRateCounter();
        m_DeltaRender = new FrameRateCounter();

        m_ViewMatrix = new float[16];
        m_ViewMatrix_UI = new float[16];
        m_ModelTypes = ModelType.values();
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
        //m_Packet.OutputDebugInfo();

        m_RenderDurationCounter.StartFrame();

        m_FrameBufferRenderer.BindFrameBuffer(FrameBufferName.RawRender);

		m_Camera.Update();
		m_UICamera.Update();

        Matrix.setIdentityM(m_ViewMatrix, 0);
        Matrix.multiplyMM(m_ViewMatrix, 0, m_Camera.m_ProjMatrix, 0, m_Camera.m_VMatrix, 0);

        DrawSkybox(m_ViewMatrix);
        DrawFloorPanels(m_ViewMatrix);
        DrawParticles(m_ViewMatrix);
        DrawParticles_Multiplier(m_ViewMatrix);
        DrawObjects(m_ViewMatrix);
        DrawTrails(m_ViewMatrix);
        DrawRopes(m_ViewMatrix);

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

       //  m_RenderDurationCounter.EndFrame();
       //  m_RenderDurationCounter.LogFrameDuration("Renderer", 0L);

        //m_DeltaRender.EndFrame();
        //m_DeltaRender.LogFrameDuration("Delta", 30L);
        //m_DeltaRender.StartFrame();
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
        for (ModelType type : m_ModelTypes)
        {
            CopyOnWriteArrayList<GameObject> list = m_Packet.GetModelList_InGame(type);

            if(list.size() > 0)
            {
                m_ModelManager.InitialiseModel(type, view, m_Camera.GetPosition());

                for (GameObject obj : list)
                {
                    m_ModelManager.Draw(obj);
                }

                m_ModelManager.CleanModel(type);
            }
        }
    }

    private void DrawParticles(float[] view)
    {
        CopyOnWriteArrayList<Particle> list = m_Packet.GetParticles(ParticleType.Standard);

        if(!list.isEmpty())
        {
            m_ParticleRenderer.Initialise(ParticleType.Standard, view, m_Camera.GetPosition(), list);
            m_ParticleRenderer.Draw();
            m_ParticleRenderer.Clean();
        }
    }

    private void DrawParticles_Multiplier(float[] view)
    {
        CopyOnWriteArrayList<Particle> list = m_Packet.GetParticles(ParticleType.Multiplier);

        if(!list.isEmpty())
        {
            m_ParticleRenderer.Initialise(ParticleType.Multiplier, view, m_Camera.GetPosition(), list);
            m_ParticleRenderer.Draw();
            m_ParticleRenderer.Clean();
        }
    }

    private void DrawFloorPanels(float[] view)
    {
        CopyOnWriteArrayList<FloorGrid> list = m_Packet.GetFloorGrids();

        if(!list.isEmpty())
        {
            m_ModelManager.InitialiseFloorPanel(view);

            for(FloorGrid obj : list)
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
        CopyOnWriteArrayList<Trail> list = m_Packet.GetTrails();

        if(!list.isEmpty())
        {
            for (Trail trail : list)
            {
                if (trail != null)
                {
                    m_TrailRenderer.Initialise(view, m_Camera.GetPosition(), trail);
                    m_TrailRenderer.Draw();
                }
            }

            m_TrailRenderer.Clean();
        }
    }

    private void DrawRopes(float[] view)
    {
        CopyOnWriteArrayList<Tentacle> list = m_Packet.GetRopes();

        if(!list.isEmpty())
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
        Matrix.setIdentityM(m_ViewMatrix_UI, 0);
        Matrix.multiplyMM(m_ViewMatrix_UI, 0, m_UICamera.m_ProjMatrix, 0, m_UICamera.m_VMatrix, 0);

        UIElementType[] types = UIElementType.values();

        for (UIElementType type : types)
        {
            CopyOnWriteArrayList<UIElement> copy = m_Packet.GetUIElementList(type);

            if(!copy.isEmpty())
            {
                m_UIManager.InitialiseModel(type, m_ViewMatrix_UI);
                for (UIElement object : copy)
                {
                    m_UIManager.DrawElement(object);
                }
                m_UIManager.CleanModel(type);
            }
        }
    }
}
