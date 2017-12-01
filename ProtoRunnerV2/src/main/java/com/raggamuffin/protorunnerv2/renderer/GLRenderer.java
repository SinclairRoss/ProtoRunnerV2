package com.raggamuffin.protorunnerv2.renderer;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.RenderObjects.RenderGroup_Generic;
import com.raggamuffin.protorunnerv2.RenderObjects.RenderGroup_Particles;
import com.raggamuffin.protorunnerv2.RenderObjects.RenderGroup_Text;
import com.raggamuffin.protorunnerv2.RenderObjects.RenderGroup_UI;
import com.raggamuffin.protorunnerv2.RenderObjects.RenderGroup_Vehicle;
import com.raggamuffin.protorunnerv2.master.RenderEffectSettings;
import com.raggamuffin.protorunnerv2.RenderObjects.RenderPackage;
import com.raggamuffin.protorunnerv2.master.RenderPackageDistributor;
import com.raggamuffin.protorunnerv2.master.RendererPacket;
import com.raggamuffin.protorunnerv2.ui.UIElementType;
import com.raggamuffin.protorunnerv2.utils.FPSCounter;
import com.raggamuffin.protorunnerv2.utils.FrameRateCounter;

import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer
{
    static { System.loadLibrary("CRenderer-lib"); }

	private static final String TAG = "DEBUG GLRenderer";

    private AssetManager m_AssetManager;

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

    private ModelType[] m_ModelTypes;
    private UIElementType[] m_UIElementTypes;

    private RenderPackageDistributor m_Distributer;

    public GLRenderer(RendererPacket packet, RenderPackageDistributor distributer)
	{
		Log.e(TAG, "GLRenderer");
        m_Packet = packet;
		m_Context = m_Packet.GetContext();
		m_Camera = new GLCamera();
		m_UICamera = new GLOrthoCamera();
		m_RenderEffectSettings = m_Packet.GetRenderEffectSettings();

        m_FrameBufferRenderer = new FrameBufferEffectRenderer();
		m_ModelManager = new ModelManager(m_Context, m_RenderEffectSettings);
		m_UIManager    = new UIRenderManager(m_Context);
        m_TrailRenderer = new TrailRenderer();
        m_RopeRenderer = new RopeRenderer();
        m_ParticleRenderer = new ParticleRenderer();

        m_ModelTypes = ModelType.values();
        m_UIElementTypes = UIElementType.values();

        m_Distributer = distributer;
	}


	public static native void StartRender_Native();
    public static native void FinaliseRender_Native();

    public static native void SetCamera_Native(float[] position, float[] lookAt, float[] up);
    public static native void DrawVehicles_Native(float[] transforms, float[] colours, float[] innerIntensity, int modelIndex, int numInstances);
    public static native void DrawGenericObject_Native(float[] transforms, float[] colours, int modelIndex, int numInstances);
    public static native void DrawParticles_Native(float[] transforms, float[] colours, int numInstances);
    public static native void DrawUIText_Native(float[] positions, float[] scales, float[] colours, char[] text, int instanceCount);
    public static native void DrawUIElements_Native(float[] transforms,float[] scales, float[] colours, int elementType, int instanceCount);
    public static native void OnSurfaceCreated_Native(float[] vertices, int[] verticesPerModel, int[] texturePixels, int[] pixelsPerTexture, int numTextures, int screenWidth, int screenHeight);
    public static native void OnSurfaceChanged_Native(int width, int height);

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config)
	{
        if(true)
        {
            int numModels = 4;

            float[] runnerVertices = ReadFloatArrayFromResource(R.string.runner_vertices);
            float[] bitVertices = ReadFloatArrayFromResource(R.string.byte_vertices);
            float[] plasmaVertices = ReadFloatArrayFromResource(R.string.plasma_vertices);

            float[] uiQuadVertices_Left = ReadFloatArrayFromResource(R.string.ui_quad_right_vertices);

            int[] verticesPerModel = new int[numModels];
            verticesPerModel[0] = runnerVertices.length;
            verticesPerModel[1] = bitVertices.length;
            verticesPerModel[2] = plasmaVertices.length;

            verticesPerModel[3] = uiQuadVertices_Left.length;

            int arrayLength = 0;
            for(int i = 0; i < numModels; ++i)
            {
                arrayLength += verticesPerModel[i];
            }

            float[] coords = new float[arrayLength];

            int offset = 0;
            java.lang.System.arraycopy(runnerVertices, 0, coords, offset, runnerVertices.length);
            offset += runnerVertices.length;

            java.lang.System.arraycopy(bitVertices, 0, coords, offset, bitVertices.length);
            offset += bitVertices.length;

            java.lang.System.arraycopy(plasmaVertices, 0, coords, offset, plasmaVertices.length);
            offset += plasmaVertices.length;

            java.lang.System.arraycopy(uiQuadVertices_Left, 0, coords, offset, uiQuadVertices_Left.length);
            offset += uiQuadVertices_Left.length;

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inScaled = false;

            Vector<Bitmap> bitmaps = new Vector<>(3);
            bitmaps.add(BitmapFactory.decodeResource(m_Packet.GetContext().getResources(), R.drawable.floor_grid, options));
            bitmaps.add(BitmapFactory.decodeResource(m_Packet.GetContext().getResources(), R.drawable.moire, options));
            bitmaps.add(BitmapFactory.decodeResource(m_Packet.GetContext().getResources(), R.drawable.chlo, options));
            bitmaps.add(BitmapFactory.decodeResource(m_Packet.GetContext().getResources(), R.drawable.noise, options));

            int bitmapCount = bitmaps.size();

            int[] pixelsPerTexture = new int[bitmapCount];
            int pixelCount = 0;

            for(int i = 0; i < bitmapCount; ++i)
            {
                Bitmap bitmap = bitmaps.get(i);

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int pixelsInBitmap = width * height;

                pixelCount += pixelsInBitmap;
                pixelsPerTexture[i] = pixelsInBitmap;
            }

            int[] packedPixels = new int[pixelCount];
            offset = 0;

            for(int i = 0; i < bitmapCount; ++i)
            {
                Bitmap bitmap = bitmaps.get(i);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                bitmap.getPixels(packedPixels, offset, width, 0, 0, width, height);

                offset += width * height;
            }

            Point screenSize = m_Packet.GetScreenSize();
            OnSurfaceCreated_Native(coords, verticesPerModel, packedPixels, pixelsPerTexture, bitmapCount, screenSize.x, screenSize.y);

            return;
        }

		Log.e(TAG, "onSurfaceCreated");

        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES31.glEnable(GLES31.GL_CULL_FACE);
        GLES31.glCullFace(GLES31.GL_BACK);

        GLES31.glEnable(GLES31.GL_DEPTH_TEST);
        GLES31.glDepthFunc(GLES31.GL_LEQUAL);
        GLES31.glDepthMask(true);
        GLES31.glDepthRangef(0.0f, 1.0f);
        GLES31.glClearDepthf(1.0f);

        GLES31.glEnable(GLES31.GL_BLEND);
        GLES31.glBlendFunc(GLES31.GL_SRC_ALPHA, GLES31.GL_ONE_MINUS_SRC_ALPHA);

        m_FrameBufferRenderer.LoadAssets(m_Packet);
        m_ModelManager.LoadAssets();
        m_TrailRenderer.LoadAssets();
        m_RopeRenderer.LoadAssets();
        m_UIManager.LoadAssets();
        m_ParticleRenderer.LoadAssets();
    }

    private static FPSCounter m_FPS = new FPSCounter("Renderer");
    private static FrameRateCounter m_Timer = new FrameRateCounter();

	@Override
	public void onDrawFrame(GL10 unused)
    {
        m_FPS.Update();

        RenderPackage rPackage = m_Distributer.Checkout_ForRead();

        if (rPackage != null)
        {
            m_FPS.Bump();
            m_Timer.StartFrame();

            if (true)
            {
                RenderCamera camera = rPackage.GetRenderCamera();
                SetCamera_Native(camera.GetPosition(), camera.GetLookAt(), camera.GetUp());

                StartRender_Native();

                for(int modelIndex = 0; modelIndex < m_ModelTypes.length; ++modelIndex)
                {
                    ModelType type = m_ModelTypes[modelIndex];

                    RenderGroup_Vehicle renderGroup = rPackage.GetVehiclesWithModel(type);
                    int instanceCount = renderGroup.GetInstanceCount();
                    if(instanceCount > 0)
                    {
                        DrawVehicles_Native(renderGroup.GetTransforms(), renderGroup.GetColours(), renderGroup.GetInnerIntensity(), modelIndex, instanceCount);
                    }

                    RenderGroup_Generic renderGroup_Generic = rPackage.GetObjectsWithModel(type);
                    instanceCount = renderGroup_Generic.GetInstanceCount();
                    if(renderGroup_Generic.GetInstanceCount() > 0)
                    {
                        DrawGenericObject_Native(renderGroup_Generic.GetTransforms(), renderGroup_Generic.GetColours(), modelIndex, instanceCount);
                    }
                }

                RenderGroup_Particles particles = rPackage.GetParticles();
                int instanceCount = particles.GetInstanceCount();
                if(instanceCount > 0)
                {
                    DrawParticles_Native(particles.GetTransforms(), particles.GetColours(), particles.GetInstanceCount());
                }

                RenderGroup_Text text = rPackage.GetText();
                instanceCount = text.GetInstanceCount();
                if(instanceCount > 0)
                {
                    DrawUIText_Native(text.GetPositions(), text.GetScales(), text.GetColours(), text.GetText(), text.GetInstanceCount());
                }


                for(int i = 0; i < m_UIElementTypes.length; ++i)
                {
                    UIElementType type = m_UIElementTypes[i];

                    RenderGroup_UI uiElements = rPackage.GetUIElementsWithType(type);
                    DrawUIElements_Native(uiElements.GetPosition(), uiElements.GetScales(), uiElements.GetColours(), i, uiElements.GetInstanceCount());
                }

                m_Distributer.CheckIn_FromRead(rPackage);

                FinaliseRender_Native();
            }
            else
            {
                m_Camera.Update(rPackage.GetRenderCamera());

                m_FrameBufferRenderer.BindFrameBuffer(FrameBufferName.RawRender);

                // Glow horizontal.
                m_FrameBufferRenderer.BindFrameBuffer(FrameBufferName.GlowHorizontal);
                GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
                GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, m_FrameBufferRenderer.GetTextureHandle(FrameBufferName.RawRender));
                m_ModelManager.DrawFBOGlowHoriz();

                // Glow vertical.
                m_FrameBufferRenderer.BindFrameBuffer(FrameBufferName.GlowVertical);
                GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
                GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, m_FrameBufferRenderer.GetTextureHandle(FrameBufferName.GlowHorizontal));
                m_ModelManager.DrawFBOGlowVert();

                // Film grain
                m_FrameBufferRenderer.BindFrameBuffer(FrameBufferName.FilmGrain);
                m_ModelManager.DrawFBOFilmGrain();

                // Render to screen.
                GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, 0);
                GLES31.glViewport(0, 0, m_Width, m_Height);
                GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT | GLES31.GL_DEPTH_BUFFER_BIT);

                GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
                GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, m_FrameBufferRenderer.GetTextureHandle(FrameBufferName.RawRender));

                GLES31.glActiveTexture(GLES31.GL_TEXTURE1);
                GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, m_FrameBufferRenderer.GetTextureHandle(FrameBufferName.GlowVertical));

                GLES31.glActiveTexture(GLES31.GL_TEXTURE2);
                GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, m_FrameBufferRenderer.GetTextureHandle(FrameBufferName.FilmGrain));

                m_ModelManager.DrawFBOFinal();
            }

            m_Timer.EndFrame();
            m_Timer.LogFrameDuration("frame length", 16L);
        }
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

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height)
	{
        if(true)
        {
            OnSurfaceChanged_Native(width, height);
            return;
        }

		Log.e(TAG, "onSurfaceChanged");

		m_Width = width;
		m_Height = height;

		m_Camera.ViewPortChanged(m_Width, m_Height);
		m_UICamera.ViewPortChanged(m_Width, m_Height);
	}
}
