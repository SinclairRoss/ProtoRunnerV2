//
// Created by Mad Beast on 05/10/2017.
//

#ifndef PROTORUNNERV2_SHADER_BASICUI_H
#define PROTORUNNERV2_SHADER_BASICUI_H

namespace Shader_BasicUI
{
    const char *VertexSource =
            R"glsl(
        #version 300 es

        uniform vec2 u_Position[15];
        uniform vec2 u_Scale[15];
        uniform mat4 u_VPMatrix;

        in vec3 a_Vertex;
        flat out int v_InstanceID;

        void main()
        {
            v_InstanceID = gl_InstanceID;

            vec2 pos = u_Position[gl_InstanceID];
            vec2 scale = u_Scale[gl_InstanceID];

            mat4 model;
            model[0] = vec4(scale.x,    0.0,        0.0,        0.0);
            model[1] = vec4(0.0,        scale.y,    0.0,        0.0);
            model[2] = vec4(0.0,        0.0,        1.0,        0.0);
            model[3] = vec4(pos,                    0.0,        1.0);

            mat4 mvp = u_VPMatrix * model;
            gl_Position = mvp * vec4(a_Vertex, 1.0);
        }
    )glsl";

    const char *FragmentSource =
            R"glsl(
        #version 300 es

        uniform vec4 u_Colour[15];
        flat in int v_InstanceID;

        out vec4 FragColour;

        void main()
        {
            vec4 colour = u_Colour[v_InstanceID];
            FragColour = colour;
        }
    )glsl";
};

#endif //PROTORUNNERV2_SHADER_BASICUI_H
