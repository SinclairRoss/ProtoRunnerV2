//
// Created by Mad Beast on 06/10/2017.
//

#ifndef PROTORUNNERV2_SHADER_SCREENQUAD_H
#define PROTORUNNERV2_SHADER_SCREENQUAD_H

namespace Shader_ScreenQuad
{
    const char *VertexSource =
            R"glsl(
        #version 300 es

        in vec3 a_Vertices;
        in vec2 a_TexCoords;

        out vec2 v_TexCoords;

        void main()
        {
            v_TexCoords = a_TexCoords;
            gl_Position = vec4(a_Vertices, 1.0);
        }
    )glsl";

    const char *FragmentSource =
            R"glsl(
        #version 300 es

        uniform sampler2D u_NoiseTexture;
        uniform sampler2D u_Texture;
        uniform vec2 u_RandomOffset;

        in vec2 v_TexCoords;
        out vec4 FragColour;

        void main()
        {
            vec4 noise = texture(u_NoiseTexture, v_TexCoords + u_RandomOffset) * 0.1;
            vec4 img = texture(u_Texture, v_TexCoords);

            FragColour = img + noise;
        }
    )glsl";
}

#endif //PROTORUNNERV2_SHADER_SCREENQUAD_H
