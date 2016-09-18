package com.raggamuffin.protorunnerv2.renderer;

// Author: Sinclair Ross
// Date:   17/09/2016

import android.graphics.Point;
import android.opengl.GLES20;
import android.util.Log;

import com.raggamuffin.protorunnerv2.master.RendererPacket;

public class FrameBufferEffectRenderer
{
    GLScreenQuad m_ScreenQuad;

    int m_TextureHandles[];
    int m_DepthBufferHandles[];
    Point m_TextureSize[];
    int m_FrameBufferHandles[];

    public FrameBufferEffectRenderer()
    {
        int numFBOs = FrameBufferName.values().length;

        m_TextureHandles = new int[numFBOs];
        m_DepthBufferHandles = new int[numFBOs];
        m_TextureSize = new Point[numFBOs];
        m_FrameBufferHandles = new int[numFBOs];
    }

    public void LoadAssets(RendererPacket packet)
    {
        m_ScreenQuad = new GLScreenQuad();

        m_TextureSize[0] = new Point(packet.GetScreenSize());
        m_TextureSize[1] = new Point(256, 256);
        m_TextureSize[2] = new Point(256, 256);
        m_TextureSize[3] = new Point(256, 256);

        int numFBOs = FrameBufferName.values().length;

        GLES20.glGenTextures(numFBOs, m_TextureHandles, 0);
        GLES20.glGenTextures(numFBOs, m_DepthBufferHandles, 0);
        GLES20.glGenFramebuffers(numFBOs, m_FrameBufferHandles, 0);

        for(FrameBufferName name : FrameBufferName.values())
        {
            int i = name.ordinal();

            CreateTexture(m_TextureHandles[i], m_TextureSize[i]);
            CreateDepthTexture(m_DepthBufferHandles[i], m_TextureSize[i]);
            CreateFrameBufferObject(m_FrameBufferHandles[i], m_TextureHandles[i], m_DepthBufferHandles[i]);
        }
    }

    private void CreateTexture(int textureHandle, Point size)
    {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, size.x, size.y, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
    }

    private void CreateDepthTexture(int textureHandle, Point size)
    {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_DEPTH_COMPONENT, size.x, size.y, 0, GLES20.GL_DEPTH_COMPONENT, GLES20.GL_UNSIGNED_SHORT, null);
    }

    private void CreateFrameBufferObject(int frameBufferHandle, int textureHandle, int depthHandle)
    {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBufferHandle);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, textureHandle, 0);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_TEXTURE_2D, depthHandle, 0);

        if (GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) != GLES20.GL_FRAMEBUFFER_COMPLETE)
        {
            Log.e("FrameBuffer", "Error on FrameBuffer " + frameBufferHandle);
        }
    }

    public void BindFrameBuffer(FrameBufferName name)
    {
        int i = name.ordinal();
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, m_FrameBufferHandles[i]);
        GLES20.glViewport(0, 0, m_TextureSize[i].x, m_TextureSize[i].y);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    public int GetTextureHandle(FrameBufferName name)
    {
        return m_TextureHandles[name.ordinal()];
    }
}
