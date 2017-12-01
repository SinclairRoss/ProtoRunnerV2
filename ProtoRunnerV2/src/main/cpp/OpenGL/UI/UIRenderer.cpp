//
// Created by Mad Beast on 05/10/2017.
//

#include <algorithm>
#include "UIRenderer.h"

UIRenderer::UIRenderer(float vertices[], int vertexCountForModel[]) :
        m_UIBlock_Left(GetCoordinatesForModel(vertices, vertexCountForModel, 3))
{}

UIRenderer::~UIRenderer()
{}

void UIRenderer::DrawObject(float positions[], float scales[], float colours[], glm::mat4& vpMatrix, int elementType, int numInstances)
{
    int positionsSize = numInstances * 2;
    float positions_Trimmed[positionsSize];
    std::copy(positions, positions + positionsSize, positions_Trimmed);

    int scalesSize = numInstances * 2;
    float scales_Trimmed[scalesSize];
    std::copy(scales, scales + scalesSize, scales_Trimmed);

    int colourSize = numInstances * 4;
    float colours_Trimmed[colourSize];
    std::copy(colours, colours + colourSize, colours_Trimmed);


    switch(elementType)
    {
        default:
        {
            m_UIBlock_Left.Draw(positions_Trimmed, scales_Trimmed, colours_Trimmed, vpMatrix, numInstances);
            break;
        }
    }
}

std::vector<float> UIRenderer::GetCoordinatesForModel(float vertices[], int vertexCountForModel[], int index)
{
    int startIndex = 0;
    for(int i = 0; i < index; ++i)
    {
        startIndex += vertexCountForModel[i];
    }

    int finalIndex = startIndex + vertexCountForModel[index];

    std::vector<float> coords(finalIndex - startIndex);

    for(int i = startIndex; i < finalIndex; ++i)
    {
        coords[i - startIndex] = vertices[i];
    }

    return coords;
}
