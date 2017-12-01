//
// Created by Mad Beast on 06/10/2017.
//

#ifndef PROTORUNNERV2_FRAMEBUFFERCONTAINER_H
#define PROTORUNNERV2_FRAMEBUFFERCONTAINER_H

#include "GLES3/gl31.h"
#include "Model_ScreenQuad.h"

class FrameBufferContainer
{
    public:
        FrameBufferContainer(int textureCount);
        ~FrameBufferContainer();

        void OnSurfaceChanged(int width, int height);
        void BindFrameBuffer();
        void RenderToScreen();

    private:
        const int TEXTURE_COUNT;

        int m_ScreenWidth;
        int m_ScreenHeight;

        GLuint m_MainRenderOutput;
        Model_ScreenQuad m_ScreenQuad;

};

#endif //PROTORUNNERV2_FRAMEBUFFERCONTAINER_H
