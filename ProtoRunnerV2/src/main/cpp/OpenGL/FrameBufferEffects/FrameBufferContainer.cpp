//
// Created by Mad Beast on 06/10/2017.
//

#include "FrameBufferContainer.h"

FrameBufferContainer::FrameBufferContainer(int textureCount) :
        TEXTURE_COUNT(textureCount)
{}

FrameBufferContainer::~FrameBufferContainer()
{}

void FrameBufferContainer::OnSurfaceChanged(int width, int height)
{
    m_ScreenWidth = width;
    m_ScreenHeight = height;

    GLuint renderTexture;
    glGenTextures(1, &renderTexture);
    glActiveTexture(GL_TEXTURE0 + TEXTURE_COUNT);
    glBindTexture(GL_TEXTURE_2D, renderTexture);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, m_ScreenWidth, m_ScreenHeight, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);

    glActiveTexture(GL_TEXTURE0 + TEXTURE_COUNT + 1);
    GLuint depthTexture;
    glGenTextures(1, &depthTexture);
    glBindTexture(GL_TEXTURE_2D, depthTexture);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, m_ScreenWidth, m_ScreenHeight, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_SHORT, 0);

    glGenFramebuffers(1, &m_MainRenderOutput);
    glBindFramebuffer(GL_FRAMEBUFFER, m_MainRenderOutput);
    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, renderTexture, 0);
    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexture, 0);

    GLenum status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
    if(status != GL_FRAMEBUFFER_COMPLETE)
        return;
}

void FrameBufferContainer::RenderToScreen()
{
    glBindFramebuffer(GL_FRAMEBUFFER, 0);
    glViewport(0,0, m_ScreenWidth, m_ScreenHeight);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    m_ScreenQuad.Draw();
}

void FrameBufferContainer::BindFrameBuffer()
{
    glBindFramebuffer(GL_FRAMEBUFFER, m_MainRenderOutput);
    glViewport(0,0, m_ScreenWidth, m_ScreenHeight);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
}