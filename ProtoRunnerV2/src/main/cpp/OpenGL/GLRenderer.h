//
// Created by Mad Beast on 27/08/2017.
//

#ifndef PROTORUNNERV2_GLRENDERER_H
#define PROTORUNNERV2_GLRENDERER_H

#include <memory>
#include "glm/glm.hpp"

#include "OpenGL/FrameBufferEffects/FrameBufferContainer.h"
#include "OpenGL/Models/ModelContainer.h"
#include "OpenGL/Textures/TextureContainer.h"
#include "OpenGL/Text/TextRenderer.h"
#include "OpenGL/UI/UIRenderer.h"

#include "Models/ModelContainer.h"

class GLRenderer
{
    public:
        GLRenderer(float vertices[], int verticesPerModel[], int textures[], int pixelsPerTexture[], int numTextures, int screenWidth, int screenHeight);
        ~GLRenderer();

        void OnSurfaceCreated();
        void SetCameraTranforms(float *position, float *lookAt, float *up);

        void StartRender();
        void FinaliseRender();

        void DrawVehicles(float* transforms, float* colours, float* innerIntensity, int modelIndex, int numObjects);
        void DrawObjects(float* transforms, float* colours, int modelIndex, int numObjects);
        void DrawUI_Text(float positions[], float scales[], float colours[], char text[], int numInstances);
        void DrawUI(float positions[], float scales[], float colours[], int elementType, int numInstances);
        void DrawParticles(float* transforms, float* colours, int numInstances);
        void OnSurfaceChanged(int width, int height);



    private:
        const float FOV;

        TextureContainer m_TextureContainer;
        FrameBufferContainer m_FrameBufferContainer;
        ModelContainer m_ModelContainer;
        TextRenderer m_TextRenderer;
        UIRenderer m_UIRenderer;
        ParticleGroup_Standard m_ParticleGroup;

        glm::vec3 m_CameraPos;
        glm::mat4 m_VPMatrix;
        glm::mat4 m_ProjectionMatrix;
        glm::mat4 m_ProjectionMatrix_UI;
};

#endif //PROTORUNNERV2_GLRENDERER_H
