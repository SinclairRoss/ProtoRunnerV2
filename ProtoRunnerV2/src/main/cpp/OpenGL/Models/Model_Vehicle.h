//
// Created by S.Ross on 26/08/2017.
//

#ifndef PROTORUNNERV2_MODEL_VEHICLE_H
#define PROTORUNNERV2_MODEL_VEHICLE_H

#include <GLES3/gl31.h>
#include <vector>

#include "../glm/glm.hpp"

class Model_Vehicle
{
    public:
        Model_Vehicle(std::vector<float> vertices);
        ~Model_Vehicle();

        void Draw(float transforms[], float colours[], float innerIntensity[], glm::mat4 &vpMatrix, const int numInstances);

    private:
        GLuint m_ShaderProgram;
        GLuint m_VertexArrayObject;
        GLint m_NumVertices;

        GLint m_ModelMatricesHandle;
        GLint m_ColourHandle;
        GLint m_InnerIntensityHandle;
        GLint m_VPMatrixHandle;
};

#endif //PROTORUNNERV2_MODEL_VEHICLE_H
