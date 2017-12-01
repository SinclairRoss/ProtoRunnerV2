//
// Created by Mad Beast on 05/10/2017.
//

#include "GLModel_UIBlock.h"

#include "../glm/gtc/type_ptr.hpp"

#include "OpenGL/Shaders/Shader_BasicUI.h"
#include "OpenGL/Utils/Helpers.h"

GLModel_UIBlock::GLModel_UIBlock(std::vector<float> vertices)
{
    m_NumVertices = vertices.size();

    glGenVertexArrays(1, &m_VertexArrayObject);
    glBindVertexArray(m_VertexArrayObject);

    m_ShaderProgram = Helpers::CreateShader(Shader_BasicUI::VertexSource, Shader_BasicUI::FragmentSource);
    glUseProgram(m_ShaderProgram);

    GLfloat* vertexArray = &vertices[0];

    GLuint vertexBuffer;
    glGenBuffers(1, &vertexBuffer);
    glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
    glBufferData(GL_ARRAY_BUFFER, m_NumVertices * 12, vertexArray, GL_STATIC_DRAW);

    GLint vertexHandle = glGetAttribLocation(m_ShaderProgram, "a_Vertex");
    glVertexAttribPointer(vertexHandle, 3, GL_FLOAT, GL_FALSE, 0, 0);
    glEnableVertexAttribArray(vertexHandle);

    m_PositionHandle = glGetUniformLocation(m_ShaderProgram, "u_Position");
    m_ScaleHandle = glGetUniformLocation(m_ShaderProgram, "u_Scale");
    m_VPMatrixHandle = glGetUniformLocation(m_ShaderProgram, "u_VPMatrix");
    m_ColourHandle = glGetUniformLocation(m_ShaderProgram, "u_Colour");
}

GLModel_UIBlock::~GLModel_UIBlock()
{}

void GLModel_UIBlock::Draw(float positions[], float scales[], float colours[], glm::mat4 &vpMatrix, const int numInstances)
{
    glUseProgram(m_ShaderProgram);
    glBindVertexArray(m_VertexArrayObject);

    glUniformMatrix4fv(m_VPMatrixHandle, 1, GL_FALSE, glm::value_ptr(vpMatrix));
    glUniform2fv(m_PositionHandle, numInstances, positions);
    glUniform2fv(m_ScaleHandle, numInstances, scales);
    glUniform4fv(m_ColourHandle, numInstances, colours);

    glDrawArraysInstanced(GL_TRIANGLES, 0, m_NumVertices, numInstances);
}