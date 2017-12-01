//
// Created by Mad Beast on 06/10/2017.
//

#ifndef PROTORUNNERV2_MODEL_SCREENQUAD_H
#define PROTORUNNERV2_MODEL_SCREENQUAD_H

#include "GLES3/gl31.h"

class Model_ScreenQuad
{
public:
    Model_ScreenQuad();
    ~Model_ScreenQuad();

    void Draw();

private:

    GLuint m_ShaderProgram;
    GLuint m_VertexArrayObject;
    GLint m_NumVertices;

    GLint m_TextureHandle_Noise;
    GLint m_TextureHandle;
    GLint m_OffsetHandle;
};


#endif //PROTORUNNERV2_MODEL_SCREENQUAD_H
