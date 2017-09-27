package com.raggamuffin.protorunnerv2.renderer;

// Author: Sinclair Ross
// Date:   17/09/2016

import android.graphics.Point;
import android.opengl.GLES31;
import android.opengl.GLES31;
import android.util.Log;

import com.raggamuffin.protorunnerv2.master.RendererPacket;

public class FrameBufferEffectRenderer
{
    private int m_TextureHandles[];
    private int m_DepthBufferHandles[];
    private Point m_TextureSize[];
    private int m_FrameBufferHandles[];

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
        m_TextureSize[0] = new Point(packet.GetScreenSize());
        m_TextureSize[1] = new Point(256, 256);
        m_TextureSize[2] = new Point(256, 256);
        m_TextureSize[3] = new Point(512, 512);

        int numFBOs = FrameBufferName.values().length;

        GLES31.glGenTextures(numFBOs, m_TextureHandles, 0);
        GLES31.glGenTextures(numFBOs, m_DepthBufferHandles, 0);
        GLES31.glGenFramebuffers(numFBOs, m_FrameBufferHandles, 0);

        for(FrameBufferName name : FrameBufferName.values())
        {
            int i = name.ordinal();

            CreateDepthTexture(m_DepthBufferHandles[i], m_TextureSize[i]);
            CreateTexture(m_TextureHandles[i], m_TextureSize[i]);
            CreateFrameBufferObject(m_FrameBufferHandles[i], m_TextureHandles[i], m_DepthBufferHandles[i]);
        }
    }

    private void CreateTexture(int textureHandle, Point size)
    {
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureHandle);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_LINEAR);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR);
        GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE);
        GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE);
        GLES31.glTexImage2D(GLES31.GL_TEXTURE_2D, 0, GLES31.GL_RGBA, size.x, size.y, 0, GLES31.GL_RGBA, GLES31.GL_UNSIGNED_BYTE, null);
    }

    private void CreateDepthTexture(int textureHandle, Point size)
    {
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureHandle);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_LINEAR);
        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_LINEAR);
        GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_CLAMP_TO_EDGE);
        GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_CLAMP_TO_EDGE);
        GLES31.glTexImage2D(GLES31.GL_TEXTURE_2D, 0, GLES31.GL_DEPTH_COMPONENT, size.x, size.y, 0, GLES31.GL_DEPTH_COMPONENT, GLES31.GL_UNSIGNED_SHORT, null);
    }

    private void CreateFrameBufferObject(int frameBufferHandle, int textureHandle, int depthHandle)
    {
        GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, frameBufferHandle);
        GLES31.glFramebufferTexture2D(GLES31.GL_FRAMEBUFFER, GLES31.GL_COLOR_ATTACHMENT0, GLES31.GL_TEXTURE_2D, textureHandle, 0);
        GLES31.glFramebufferTexture2D(GLES31.GL_FRAMEBUFFER, GLES31.GL_DEPTH_ATTACHMENT, GLES31.GL_TEXTURE_2D, depthHandle, 0);

        if (GLES31.glCheckFramebufferStatus(GLES31.GL_FRAMEBUFFER) != GLES31.GL_FRAMEBUFFER_COMPLETE)
        {
            Log.e("FrameBuffer", "Error on FrameBuffer " + frameBufferHandle);
        }
    }

    public void BindFrameBuffer(FrameBufferName name)
    {
        int i = name.ordinal();
        GLES31.glBindFramebuffer(GLES31.GL_FRAMEBUFFER, m_FrameBufferHandles[i]);
        GLES31.glViewport(0, 0, m_TextureSize[i].x, m_TextureSize[i].y);

        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT | GLES31.GL_DEPTH_BUFFER_BIT);
    }

    public int GetTextureHandle(FrameBufferName name)
    {
        return m_TextureHandles[name.ordinal()];
    }
}
