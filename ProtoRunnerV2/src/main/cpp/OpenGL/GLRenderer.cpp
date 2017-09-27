//
// Created by Mad Beast on 27/08/2017.
//

#include "GLRenderer.h"

#include "glm/gtc/matrix_transform.hpp"
#include "glm/gtc/type_ptr.hpp"

GLRenderer::GLRenderer(float vertices[], int verticesPerModel[], int textures[], int pixelsPerTexture[], int numTextures) :
        FOV(1.57079632679489661923132169163975144f),
        m_TextureContainer(textures, pixelsPerTexture, numTextures),
        m_ModelContainer(vertices, verticesPerModel),
        m_TextRenderer(),
        m_ParticleGroup(),
        m_CameraPos(),
        m_VPMatrix(),
        m_ProjectionMatrix(),
        m_ProjectionMatrix_UI()
{}

GLRenderer::~GLRenderer()
{}

void GLRenderer::OnSurfaceCreated()
{
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    glEnable(GL_CULL_FACE);
    glCullFace(GL_BACK);

    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LEQUAL);
    glDepthMask(GL_TRUE);
    glDepthRangef(0.0f, 1.0f);
    glClearDepthf(1.0f);

    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  //  glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);
}

void GLRenderer::PrepareSurface(float *position, float *lookAt, float *up)
{
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    m_CameraPos = glm::make_vec3(position);
    glm::vec3 lookAtVec = glm::make_vec3(lookAt);
    glm::vec3 upVec = glm::make_vec3(up);

    glm::mat4 viewMatrix = glm::lookAt(m_CameraPos, lookAtVec, upVec);

    m_VPMatrix = m_ProjectionMatrix * viewMatrix;

    DrawUI();
}

void GLRenderer::DrawVehicles(float* transforms, float* colours, float* innerIntensity, int modelIndex, int numObjects)
{
    m_ModelContainer.DrawVehicles(transforms, colours, innerIntensity, modelIndex, m_VPMatrix, numObjects);
}

void GLRenderer::DrawObjects(float* transforms, float* colours, int modelIndex, int numObjects)
{
    m_ModelContainer.Draw_Objects(transforms, colours, modelIndex, m_VPMatrix, numObjects);
}

void GLRenderer::DrawParticles(float* transforms, float* colours, int numInstances)
{
    m_ParticleGroup.Draw(transforms, colours, m_VPMatrix, m_CameraPos, numInstances);
}

void GLRenderer::DrawUI_Text(float transforms[], float colours[], char text[], int numInstances)
{
    m_TextRenderer.Draw(transforms, colours, text, numInstances, m_ProjectionMatrix_UI);
}

void GLRenderer::DrawUI()
{

}

void GLRenderer::OnSurfaceChanged(int width, int height)
{
    glViewport(0,0, width, height);
    float ratio = (float)width / (float)height;

    m_ProjectionMatrix_UI = glm::ortho(-ratio, ratio,   -1.0f, 1.0f,   0.0f, 1.0f);
    m_ProjectionMatrix = glm::perspective(FOV, ratio, 0.1f, 1000.0f);
}