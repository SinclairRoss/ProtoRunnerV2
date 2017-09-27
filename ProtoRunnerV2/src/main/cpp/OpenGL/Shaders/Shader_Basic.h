//
// Created by Mad Beast on 27/08/2017.
//

#ifndef PROTORUNNERV2_SHADER_BASIC_H
#define PROTORUNNERV2_SHADER_BASIC_H

namespace Shader_Basic
{
    const char *VertexSource =
    R"glsl(
        #version 300 es

        uniform mat4 u_ModelMatrices[50];
        uniform mat4 u_VPMatrix;

        in vec3 a_Vertex;

        flat out int v_InstanceID;

        void main()
        {
            v_InstanceID = gl_InstanceID;

            mat4 m = u_ModelMatrices[gl_InstanceID];
            mat4 mvp = u_VPMatrix * m;
            gl_Position = mvp * vec4(a_Vertex, 1.0);
        }
    )glsl";

    const char *FragmentSource =
            R"glsl(
        #version 300 es

        uniform vec4 u_Colour[50];

        flat in int v_InstanceID;
        out vec4 FragColour;

        void main()
        {
            FragColour = u_Colour[v_InstanceID];
        }
    )glsl";

}

#endif //PROTORUNNERV2_SHADER_BASIC_H