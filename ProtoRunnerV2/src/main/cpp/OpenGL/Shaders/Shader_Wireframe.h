//
// Created by Mad Beast on 28/08/2017.
//

#ifndef PROTORUNNERV2_SHADER_WIREFRAME_H
#define PROTORUNNERV2_SHADER_WIREFRAME_H

namespace Shader_Wireframe
{
    const char *VertexSource =
            R"glsl(
        #version 300 es

        uniform mat4 u_ModelMatrices[50];
        uniform mat4 u_VPMatrix;

        in vec3 a_Vertex;
        in vec3 a_Barycentric;

        out vec3 v_Barycentric;
        flat out int v_InstanceID;

        void main()
        {
            v_Barycentric = a_Barycentric;
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
        uniform float u_InnerIntensity[50];

        in vec3 v_Barycentric;
        flat in int v_InstanceID;

        out vec4 FragColour;

        void main()
        {
            float innerIntensity = u_InnerIntensity[v_InstanceID];
            vec4 colour = u_Colour[v_InstanceID];

            float outerMultiplier = float(any(lessThan(v_Barycentric, vec3(0.06))));
            float innerMultiplier = innerIntensity * (1.0 - outerMultiplier);

            vec4 outerColour = colour * outerMultiplier;
            vec4 innerColour = colour * innerMultiplier;

            FragColour = outerColour + innerColour;
            FragColour.w = colour.w;
        }
    )glsl";
}


#endif //PROTORUNNERV2_SHADER_WIREFRAME_H
