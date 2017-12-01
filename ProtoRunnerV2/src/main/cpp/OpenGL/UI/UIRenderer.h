//
// Created by Mad Beast on 05/10/2017.
//

#ifndef PROTORUNNERV2_UIRENDERER_H
#define PROTORUNNERV2_UIRENDERER_H

#include "GLModel_UIBlock.h"

class UIRenderer
{
    public:
        UIRenderer(float vertices[], int vertexCountForModel[]);
        ~UIRenderer();

        void DrawObject(float positions[], float scales[], float colours[], glm::mat4& vpMatrix, int modelIndex, int numInstances);

    private:

    std::vector<float> GetCoordinatesForModel(float vertices[], int vertexCountForModel[], int index);

    GLModel_UIBlock m_UIBlock_Left;
};

#endif //PROTORUNNERV2_UIRENDERER_H
