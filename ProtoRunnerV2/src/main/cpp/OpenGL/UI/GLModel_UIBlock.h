//
// Created by Mad Beast on 05/10/2017.
//

#ifndef PROTORUNNERV2_GLMODEL_UIBLOCK_H
#define PROTORUNNERV2_GLMODEL_UIBLOCK_H

#include <vector>

#include "GLES3/gl31.h"
#include "OpenGL/glm/detail/type_mat.hpp"

class GLModel_UIBlock
{
    public:
        GLModel_UIBlock(std::vector<float> vertices);
        ~GLModel_UIBlock();

        void Draw(float positions[], float scales[], float colours[], glm::mat4 &vpMatrix, const int numInstances);

    private:

        GLuint m_ShaderProgram;
        GLuint m_VertexArrayObject;
        GLint m_NumVertices;

        GLint m_PositionHandle;
        GLint m_ScaleHandle;
        GLint m_VPMatrixHandle;

        GLint m_ColourHandle;
};

#endif //PROTORUNNERV2_GLMODEL_UIBLOCK_H
