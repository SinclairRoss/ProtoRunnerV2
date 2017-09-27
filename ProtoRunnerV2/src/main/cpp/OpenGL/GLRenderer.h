//
// Created by Mad Beast on 27/08/2017.
//

#ifndef PROTORUNNERV2_GLRENDERER_H
#define PROTORUNNERV2_GLRENDERER_H

#include <memory>
#include <OpenGL/Particles/ParticleGroup_Standard.h>
#include <OpenGL/Text/TextRenderer.h>

#include "OpenGL/Models/ModelContainer.h"
#include "OpenGL/Textures/TextureContainer.h"
#include "glm/glm.hpp"

#include "Models/ModelContainer.h"

class GLRenderer
{
    public:
        GLRenderer(float vertices[], int verticesPerModel[], int textures[], int pixelsPerTexture[], int numTextures);
        ~GLRenderer();

        void OnSurfaceCreated();
        void PrepareSurface(float *position, float *lookAt, float *up);
        void DrawVehicles(float* transforms, float* colours, float* innerIntensity, int modelIndex, int numObjects);
        void DrawObjects(float* transforms, float* colours, int modelIndex, int numObjects);
        void DrawUI_Text(float transforms[], float colours[], char text[], int numInstances);
        void DrawUI();
        void DrawParticles(float* transforms, float* colours, int numInstances);
        void OnSurfaceChanged(int width, int height);

    private:
        const float FOV;

        TextureContainer m_TextureContainer;
        ModelContainer m_ModelContainer;
        TextRenderer m_TextRenderer;
        ParticleGroup_Standard m_ParticleGroup;

        glm::vec3 m_CameraPos;
        glm::mat4 m_VPMatrix;
        glm::mat4 m_ProjectionMatrix;
        glm::mat4 m_ProjectionMatrix_UI;
};

#endif //PROTORUNNERV2_GLRENDERER_H
