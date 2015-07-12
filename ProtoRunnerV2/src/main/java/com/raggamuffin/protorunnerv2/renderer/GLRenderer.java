package com.raggamuffin.protorunnerv2.renderer;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.raggamuffin.protorunnerv2.gameobjects.GameObject;
import com.raggamuffin.protorunnerv2.master.RenderEffectSettings;
import com.raggamuffin.protorunnerv2.master.RendererPacket;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.utils.Vector3;

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
	private ArrayList<UIElement> m_UIElements;
	
	private Context m_Context;
	
	private final float[] m_MVPMatrix = new float[16];	// MVP Matrix.
    private final float[] m_MMatrix   = new float[16];	// Model Matrix		// Projection and View Matrices in camera class.
    
    private GLOrthoCamera m_UICamera;
    private GLCamera m_Camera;

    private ModelManager m_ModelManager;
    private UIRenderManager m_UIManager;
    
    private RenderEffectSettings m_RenderEffectSettings;
    
    // FBO
    private final int m_NumFBOs = 3;
    private int m_Textures[];
    private int m_Depth[];
    private int m_Size[];
    private int m_FrameBuffers[];

    private int counter = 0;
    private final int maxCount = 500;
    private Long totalTime = 0L;

	public GLRenderer(RendererPacket packet)
	{
		Log.e(TAG, "GLRenderer");

        m_Packet = packet;
		m_Context 				= m_Packet.GetContext();
		m_UIElements 			= m_Packet.GetUIElements();
		m_Camera 				= new GLCamera(m_Packet.GetCamera());
		m_UICamera 				= new GLOrthoCamera();
		m_RenderEffectSettings 	= m_Packet.GetRenderEffectSettings();

		m_ModelManager = new ModelManager(m_Context, m_RenderEffectSettings);
		m_UIManager    = new UIRenderManager(m_Context, m_UICamera);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) 
	{
		Log.e(TAG, "onSurfaceCreated");
		
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glCullFace(GLES20.GL_BACK);
		
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        GLES20.glDepthMask(true);
        GLES20.glDepthRangef(0.0f, 1.0f);
        GLES20.glClearDepthf(1.0f);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        m_ModelManager.LoadAssets(m_Camera);
        m_UIManager.LoadAssets();

        // FBO
        m_Size = new int [m_NumFBOs];
        m_Size[0] = 1024;
        m_Size[1] = 256;
        m_Size[2] = 256;

        m_Textures 	= new int[m_NumFBOs];
        GLES20.glGenTextures(m_NumFBOs, m_Textures, 0);
        
        for(int i = 0; i < m_NumFBOs; i ++)
        {
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_Textures[i]);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
	        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, m_Size[i], m_Size[i], 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        }
        
        m_Depth = new int[m_NumFBOs];
        GLES20.glGenTextures(m_NumFBOs, m_Depth, 0);
        
        for(int i = 0; i < m_NumFBOs; i ++)
        {
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_Depth[i]);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
	        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_DEPTH_COMPONENT, m_Size[i], m_Size[i], 0, GLES20.GL_DEPTH_COMPONENT, GLES20.GL_UNSIGNED_SHORT, null);
        }

        m_FrameBuffers = new int[m_NumFBOs];
        GLES20.glGenFramebuffers(m_NumFBOs, m_FrameBuffers, 0);
        
        for(int i = 0; i < m_NumFBOs; i ++)
        {
        	GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, m_FrameBuffers[i]);
        	GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, m_Textures[i], 0);     	
        	GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT,  GLES20.GL_TEXTURE_2D, m_Depth[i], 0);
        	
        	int status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
        	if (status != GLES20.GL_FRAMEBUFFER_COMPLETE)
        		Log.e(TAG,"Error on FrameBuffer " + i);
        } 
    }

	@Override
	public void onDrawFrame(GL10 unused) 
	{
	//	Log.e(TAG,"onDrawFrame");

        Long start = System.currentTimeMillis();
		
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, m_FrameBuffers[0]);
		GLES20.glViewport(0, 0, m_Size[0], m_Size[0]);
 
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		m_Camera.Update();
		m_UICamera.Update();

        DrawSkybox();
        DrawObjects();
		DrawUI();
		
		// Glow vertical.
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, m_FrameBuffers[1]);
		GLES20.glViewport(0, 0, m_Size[1], m_Size[1]);
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		    
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_Textures[0]);
			
        m_ModelManager.DrawFBOGlowVert();

        // Glow horizontal.
 		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, m_FrameBuffers[2]);
 		GLES20.glViewport(0, 0, m_Size[2], m_Size[2]);
 		
 		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
     
 		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
 	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_Textures[1]);
 			
         m_ModelManager.DrawFBOGlowHoriz();
        
		// Render to screen.
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
		GLES20.glViewport(0, 0, m_Width, m_Height);
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

    	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_Textures[0]);

    	GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_Textures[2]);
			
        m_ModelManager.DrawFBOFinal();

        Long end = System.currentTimeMillis();

        totalTime += end - start;
        counter++;

        if(counter >= maxCount)
        {
            Log.e("testy test", "Time: " + totalTime / counter);

            totalTime = 0L;
            counter = 0;
        }
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

    private void DrawSkybox()
    {
        m_ModelManager.InitialiseType(ModelType.Skybox);

        Matrix.setIdentityM(m_MMatrix, 0);
        Matrix.translateM(m_MMatrix, 0, (float) m_Camera.GetPosition().I, (float) m_Camera.GetPosition().J, (float) m_Camera.GetPosition().K);

        // Combine the rotation matrix with the projection and camera view
        Matrix.multiplyMM(m_MVPMatrix, 0, m_Camera.m_VMatrix, 0, m_MMatrix, 0);
        Matrix.multiplyMM(m_MVPMatrix, 0, m_Camera.m_ProjMatrix, 0, m_MVPMatrix, 0);

        m_ModelManager.DrawSkyBox(m_MVPMatrix, m_RenderEffectSettings.GetSkyBoxColour());

        m_ModelManager.CleanModel(ModelType.Skybox);
    }

    private void DrawObjects()
    {
        ModelType[] types = ModelType.values();

        for (ModelType type : types)
        {
            m_ModelManager.InitialiseType(type);

            ArrayList<GameObject> list = (ArrayList<GameObject>)m_Packet.GetList(type).clone();

            for(GameObject obj : list)
            {
                if(obj == null)
                    continue;

                Vector3 Position = obj.GetPosition();
                Vector3 Scale 	 = obj.GetScale();
                Vector3 forward  = obj.GetForward();

                Matrix.setIdentityM(m_MMatrix, 0);

                Matrix.translateM(m_MMatrix, 0, (float) Position.I, (float) Position.J, (float) Position.K);

                Matrix.rotateM(m_MMatrix, 0, (float) Math.toDegrees(obj.GetRoll()), (float) forward.I, (float) forward.J, (float) forward.K);
                Matrix.rotateM(m_MMatrix, 0, (float) -Math.toDegrees(obj.GetYaw()), 0.0f, 1.0f, 0.0f);

                Matrix.scaleM(m_MMatrix, 0, (float) Scale.I, (float) Scale.J, (float) Scale.K);

                // Combine the rotation matrix with the projection and camera view
                Matrix.multiplyMM(m_MVPMatrix, 0, m_Camera.m_VMatrix, 0, m_MMatrix, 0);
                Matrix.multiplyMM(m_MVPMatrix, 0, m_Camera.m_ProjMatrix, 0, m_MVPMatrix, 0);

                m_ModelManager.DrawModel(obj, m_MVPMatrix);
            }

            m_ModelManager.CleanModel(type);
        }
    }
	
	private void DrawUI()
	{
		ArrayList<UIElement> Copy = (ArrayList<UIElement>) m_UIElements.clone();

		for(UIElement object : Copy)
	        m_UIManager.DrawElement(object);
	}
	
	public static int loadShader(int type, String shaderCode)
    {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
