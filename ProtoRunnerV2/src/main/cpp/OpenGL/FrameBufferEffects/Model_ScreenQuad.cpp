//
// Created by Mad Beast on 06/10/2017.
//

#include "Model_ScreenQuad.h"

#include "OpenGL/Utils/Helpers.h"
#include "OpenGL/Shaders/Shader_ScreenQuad.h"

Model_ScreenQuad::Model_ScreenQuad()
{
    const float size = 1.0f;
    GLfloat vertices[] =
    {
             size,  size,  0.0f,
            -size,  size,  0.0f,
             size, -size,  0.0f,

            -size,  size,  0.0f,
            -size, -size,  0.0f,
             size, -size,  0.0f
    };

    GLfloat texCoordsArray[] =
    {
            1.0f,   1.0f,
            0.0f,   1.0f,
            1.0f,   0.0f,

            0.0f,   1.0f,
            0.0f,   0.0f,
            1.0f,   0.0f
    };

    m_NumVertices = 6;

    glGenVertexArrays(1, &m_VertexArrayObject);
    glBindVertexArray(m_VertexArrayObject);

    m_ShaderProgram = Helpers::CreateShader(Shader_ScreenQuad::VertexSource, Shader_ScreenQuad::FragmentSource);
    glUseProgram(m_ShaderProgram);

    GLuint vertexBuffer;
    glGenBuffers(1, &vertexBuffer);
    glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
    glBufferData(GL_ARRAY_BUFFER, m_NumVertices * 12, vertices, GL_STATIC_DRAW);

    GLint vertexHandle = glGetAttribLocation(m_ShaderProgram, "a_Vertices");
    glVertexAttribPointer(vertexHandle, 3, GL_FLOAT, GL_FALSE, 0, 0);
    glEnableVertexAttribArray(vertexHandle);

    GLuint texCoords;
    glGenBuffers(1, &texCoords);
    glBindBuffer(GL_ARRAY_BUFFER, texCoords);
    glBufferData(GL_ARRAY_BUFFER, m_NumVertices * 8, texCoordsArray, GL_STATIC_DRAW);

    GLint texCoordHandle = glGetAttribLocation(m_ShaderProgram, "a_TexCoords");
    glVertexAttribPointer(texCoordHandle, 2, GL_FLOAT, GL_FALSE, 0, 0);
    glEnableVertexAttribArray(texCoordHandle);

    m_TextureHandle_Noise = glGetUniformLocation(m_ShaderProgram, "u_NoiseTexture");
    m_TextureHandle = glGetUniformLocation(m_ShaderProgram, "u_Texture");
    m_OffsetHandle = glGetUniformLocation(m_ShaderProgram, "u_RandomOffset");
}

Model_ScreenQuad::~Model_ScreenQuad()
{}

void Model_ScreenQuad::Draw()
{
    glUseProgram(m_ShaderProgram);
    glBindVertexArray(m_VertexArrayObject);

    glUniform1i(m_TextureHandle_Noise, 3);
    glUniform1i(m_TextureHandle, 4);

    glUniform2f(m_OffsetHandle, Helpers::RandomFloat(), Helpers::RandomFloat());

    glDrawArrays(GL_TRIANGLES, 0, m_NumVertices);
}