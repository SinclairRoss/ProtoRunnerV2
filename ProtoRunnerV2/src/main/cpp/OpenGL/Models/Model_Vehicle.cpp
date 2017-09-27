//
// Created by Mad Beast on 26/08/2017.
//

#include "OpenGL/Models/Model_Vehicle.h"

#include "../Shaders/Shader_Wireframe.h"
#include "../glm/gtc/type_ptr.hpp"
#include "../Utils/Helpers.h"

Model_Vehicle::Model_Vehicle(std::vector<float> vertices)
{
    m_NumVertices = vertices.size() / 3;
    int numTriangles = m_NumVertices / 3;

    GLfloat barycentricCoords[vertices.size()];
    for(int i = 0; i < numTriangles; ++i)
    {
        barycentricCoords[i * 9 + 0] = 1.0f;
        barycentricCoords[i * 9 + 1] = 0.0f;
        barycentricCoords[i * 9 + 2] = 0.0f;

        barycentricCoords[i * 9 + 3] = 0.0f;
        barycentricCoords[i * 9 + 4] = 1.0f;
        barycentricCoords[i * 9 + 5] = 0.0f;

        barycentricCoords[i * 9 + 6] = 0.0f;
        barycentricCoords[i * 9 + 7] = 0.0f;
        barycentricCoords[i * 9 + 8] = 1.0f;
    }

    GLfloat* vertexArray = &vertices[0];

    glGenVertexArrays(1, &m_VertexArrayObject);
    glBindVertexArray(m_VertexArrayObject);

    m_ShaderProgram = Helpers::CreateShader(Shader_Wireframe::VertexSource, Shader_Wireframe::FragmentSource);
    glUseProgram(m_ShaderProgram);

    GLuint vertexBuffer;
    glGenBuffers(1, &vertexBuffer);
    glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
    glBufferData(GL_ARRAY_BUFFER, m_NumVertices * 12, vertexArray, GL_STATIC_DRAW);

    GLint vertexHandle = glGetAttribLocation(m_ShaderProgram, "a_Vertex");
    glVertexAttribPointer(vertexHandle, 3, GL_FLOAT, GL_FALSE, 0, 0);
    glEnableVertexAttribArray(vertexHandle);

    GLuint barycentricBuffer;
    glGenBuffers(1, &barycentricBuffer);
    glBindBuffer(GL_ARRAY_BUFFER, barycentricBuffer);
    glBufferData(GL_ARRAY_BUFFER, sizeof(barycentricCoords), barycentricCoords, GL_STATIC_DRAW);

    GLint barycentricHandle = glGetAttribLocation(m_ShaderProgram, "a_Barycentric");
    glVertexAttribPointer(barycentricHandle, 3, GL_FLOAT, GL_FALSE, 0, 0);
    glEnableVertexAttribArray(barycentricHandle);

    m_ModelMatricesHandle = glGetUniformLocation(m_ShaderProgram, "u_ModelMatrices");
    m_ColourHandle = glGetUniformLocation(m_ShaderProgram, "u_Colour");
    m_InnerIntensityHandle = glGetUniformLocation(m_ShaderProgram, "u_InnerIntensity");
    m_VPMatrixHandle = glGetUniformLocation(m_ShaderProgram, "u_VPMatrix");
}

Model_Vehicle::~Model_Vehicle()
{}

void Model_Vehicle::Draw(float transforms[], float colours[], float innerIntensity[], glm::mat4 &vpMatrix, const int numInstances)
{
    glUseProgram(m_ShaderProgram);
    glBindVertexArray(m_VertexArrayObject);

    glUniformMatrix4fv(m_VPMatrixHandle, 1, GL_FALSE, glm::value_ptr(vpMatrix));
    glUniformMatrix4fv(m_ModelMatricesHandle, numInstances, GL_FALSE, transforms);
    glUniform4fv(m_ColourHandle, numInstances, colours);
    glUniform1fv(m_InnerIntensityHandle, numInstances, innerIntensity);

    glDrawArraysInstanced(GL_TRIANGLES, 0, m_NumVertices, numInstances);
}