package com.raggamuffin.protorunnerv2.renderer;

public class Shaders
{
    public static final String vertexShader_BASIC =
            "uniform mat4 u_ProjMatrix;"
        +   "uniform vec3 u_Position;"

        +   "attribute vec4 a_Vertices;"

        +   "void main()"
        +   "{"
        +   "   mat4 model;"
        +   "   model[0] = vec4(1,0,0,0);"
        +   "   model[1] = vec4(0,1,0,0);"
        +   "   model[2] = vec4(0,0,1,0);"
        +   "   model[3] = vec4(u_Position.x, u_Position.y, u_Position.z, 1);"

        +	"	gl_Position = (u_ProjMatrix * model) * a_Vertices;"
        +   "}";

    public static final String fragmentShader_BLOCKCOLOUR =
            "precision lowp float;"
        +   "uniform vec4 u_Color;"

        +   "void main()"
        +   "{"
        +   "	gl_FragColor = u_Color;"
        +   "}";

    public static final String vertexShader_LOOKAT_BASIC =
            "uniform mat4 u_ProjMatrix;"
        +   "uniform vec3 u_Position;"
        +   "uniform vec3 u_Forward;"
        +   "uniform vec3 u_Scale;"

        +	"attribute vec4 a_Vertices;"

        + 	"void main()"
        +	"{"
        +   "   vec3 z = normalize(u_Forward);"
        +   "   vec3 x = normalize(cross(vec3(0,1,0),z));"
        +   "   vec3 y = cross(z,x);"

        +   "   mat4 model;"
        +   "   model[0] = vec4(x.x, x.y, x.z, 0);"
        +   "   model[1] = vec4(y.x, y.y, y.z, 0);"
        +   "   model[2] = vec4(z.x, z.y, z.z, 0);"
        +   "   model[3] = vec4(u_Position.x, u_Position.y, u_Position.z, 1);"

        +   "   mat4 scale;"
        +   "   scale[0] = vec4(u_Scale.x, 0, 0, 0);"
        +   "   scale[1] = vec4(0, u_Scale.y, 0, 0);"
        +   "   scale[2] = vec4(0, 0, u_Scale.z, 0);"
        +   "   scale[3] = vec4(0, 0, 0, 1);"

        +	"	gl_Position = (u_ProjMatrix * (model * scale)) * a_Vertices;"
        +	"}";

    public static final String vertexShader_STANDARD =
            "uniform mat4 u_ProjMatrix;"
        +   "uniform vec4 u_Position;"
        +   "uniform vec3 u_Forward;"
        +   "uniform vec3 u_Up;"
        +   "uniform vec3 u_Right;"
        +   "uniform vec3 u_Scale;"
        +   "uniform float u_Roll;"

        +	"attribute vec4 a_Vertices;"

        + 	"void main()"
        +	"{"
        +   "   mat4 model;"
        +   "   model[0] = vec4(u_Right.x, u_Right.y, u_Right.z, 0);"
        +   "   model[1] = vec4(u_Up.x, u_Up.y, u_Up.z, 0);"
        +   "   model[2] = vec4(u_Forward.x, u_Forward.y, u_Forward.z, 0);"
        +   "   model[3] = vec4(u_Position.x, u_Position.y, u_Position.z, 1);"

        +   "   float cosTheta = cos(u_Roll);"
        +   "   float sinTheta = sin(u_Roll);"

        +   "   mat4 roll;"
        +   "   roll[0] = vec4(cosTheta, -sinTheta, 0, 0);"
        +   "   roll[1] = vec4(sinTheta,  cosTheta, 0, 0);"
        +   "   roll[2] = vec4(0, 0, 1, 0);"
        +   "   roll[3] = vec4(0, 0, 0, 1);"

        +   "   mat4 scale;"
        +   "   scale[0] = vec4(u_Scale.x, 0, 0, 0);"
        +   "   scale[1] = vec4(0, u_Scale.y, 0, 0);"
        +   "   scale[2] = vec4(0, 0, u_Scale.z, 0);"
        +   "   scale[3] = vec4(0, 0, 0, 1);"

        +	"	gl_Position = (u_ProjMatrix * (model * roll * scale)) * a_Vertices;"
        +	"}";

    public static final String vertexShader_LINE =
            "uniform mat4 u_ProjMatrix;"
        +   "uniform vec4 u_WorldPos;"
        +   "uniform float u_Yaw;"
        +   "uniform vec3 u_Scale;"

        +	"attribute vec4 a_Position;  \n"
        +	"attribute float a_Weight;	 \n"

        +   "varying float v_Weight;	 \n"

        +   "void main()                 \n"
        +   "{"
        +	"	v_Weight = a_Weight;     \n"

        +   "   mat4 world;"
        +   "   world[0] = vec4(1,0,0,0);"
        +   "   world[1] = vec4(0,1,0,0);"
        +   "   world[2] = vec4(0,0,1,0);"
        +   "   world[3] = u_WorldPos;"

        +   "   float cosTheta = cos(u_Yaw);"
        +   "   float sinTheta = sin(u_Yaw);"

        +   "   mat4 yaw;"
        +   "   yaw[0] = vec4(cosTheta, 0, sinTheta, 0);"
        +   "   yaw[1] = vec4(0, 1, 0, 0);"
        +   "   yaw[2] = vec4(-sinTheta,0,cosTheta,0);"
        +   "   yaw[3] = vec4(0, 0, 0, 1);"

        +   "   mat4 scale;"
        +   "   scale[0] = vec4(u_Scale.x, 0, 0, 0);"
        +   "   scale[1] = vec4(0, u_Scale.y, 0, 0);"
        +   "   scale[2] = vec4(0, 0, u_Scale.z, 0);"
        +   "   scale[3] = vec4(0, 0, 0, 1);"

        +   "   world *= yaw;"
        +   "   world *= scale;"

        +   "   mat4 mvp = u_ProjMatrix * world;"
        +	"	gl_Position = mvp * a_Position;"
        +   "}";

    public static final String fragmentShader_LINE =
            "precision lowp float;"

        +   "uniform vec4 u_Color;"
        +	"uniform vec4 u_EndPointColor;"

        +   "varying float v_Weight;	 "

        +   "void main()"
        +   "{"
        +   "	gl_FragColor = mix(u_Color, u_EndPointColor, v_Weight); "
        +   "}";

    public static final String vertexShader_TRAIL =
            "uniform mat4 u_ProjMatrix;"

                    +	"attribute vec4 a_Position;  \n"
                    +   "attribute vec4 a_Color;"

                    +   "varying vec4 v_Color;"

                    +   "void main()                 \n"
                    +   "{"
                    +   "   v_Color = a_Color;"
                    +	"	gl_Position = u_ProjMatrix * a_Position;"
                    +   "}";

    public static final String fragmentShader_TRAIL =
            "precision lowp float;"

        +   "varying vec4 v_Color;"

        +   "void main()"
        +   "{"
        +   "	gl_FragColor = v_Color;"
        +   "}";

        public static final String vertexShader_ROPE =
            "uniform mat4 u_ProjMatrix;"

        +	"attribute vec4 a_Position;"
        +   "attribute float a_NormalisedLength;"
        +   "attribute float a_Alpha;"

        +   "varying float v_NormalisedLength;"
        +   "varying float v_Alpha;"

        +   "void main()"
        +   "{"
        +   "   v_NormalisedLength = a_NormalisedLength;"
        +   "   v_Alpha = a_Alpha;"

        +	"	gl_Position = u_ProjMatrix * a_Position;"
        +   "}";

    public static final String fragmentShader_ROPE =
            "precision lowp float;"

        +   "uniform vec4 u_HotColor;"
        +   "uniform vec4 u_ColdColor;"
        +   "uniform float u_ColorBloomPoint;"

        +   "varying float v_NormalisedLength;"
        +   "varying float v_Alpha;"

        +   "void main()"
        +   "{"
        +   "   float threshold = 0.2f;"
        +   "   float min = clamp(u_ColorBloomPoint - threshold, 0.0f, 1.0f);"
        +   "   float max = clamp(u_ColorBloomPoint + threshold, 0.0f, 1.0f);"
        +   "   float lerp = sin(clamp((v_NormalisedLength - min) / (max - min),0.0, 1.0) * 3.14159265);"
        +   "	gl_FragColor = ((u_ColdColor * (1.0f - lerp)) + (u_HotColor * lerp));"
        +   "   gl_FragColor.a *= v_Alpha;"
        +   "}";

    public static final String vertexShader_TEXTURED =
            "uniform mat4 u_ProjMatrix;"
        +   "uniform vec4 u_WorldPos;"
        +   "uniform vec3 u_Scale;"

        +	"attribute vec4 a_Position;"
        + 	"attribute vec2 a_TexCoord;"

        + 	"varying vec2 v_TexCoord;"

        +	"void main()"
        +	"{"
        +	"	v_TexCoord = a_TexCoord;"

        +   "   mat4 world;"
        +   "   world[0] = vec4(1,0,0,0);"
        +   "   world[1] = vec4(0,1,0,0);"
        +   "   world[2] = vec4(0,0,1,0);"
        +   "   world[3] = u_WorldPos;"

        +   "   mat4 scale;"
        +   "   scale[0] = vec4(u_Scale.x, 0, 0, 0);"
        +   "   scale[1] = vec4(0, u_Scale.y, 0, 0);"
        +   "   scale[2] = vec4(0, 0, u_Scale.z, 0);"
        +   "   scale[3] = vec4(0, 0, 0, 1);"

        +   "   world = world * scale;"
        +   "   mat4 mvp = u_ProjMatrix * world;"
        +	"	gl_Position = mvp * a_Position;"
        +	"}";

    public static final String fragmentShader_TEXTURED =
            "precision lowp float;"

        + 	"uniform sampler2D u_Texture;"
        +   "uniform vec2 u_TexOffset;"
        +   "uniform vec4 u_Color;"

        + 	"varying vec2 v_TexCoord;"

        +   "void main()"
        +   "{"
        +   "	gl_FragColor = u_Color * texture2D(u_Texture, v_TexCoord + u_TexOffset);"
        +   "}";

    public static final String vertexShader_WIREFRAME =
            "uniform mat4 u_ProjMatrix;"
        +   "uniform vec4 u_Position;"
        +   "uniform vec3 u_Forward;"
        +   "uniform vec3 u_Up;"
        +   "uniform vec3 u_Right;"
        +   "uniform vec3 u_Scale;"
        +   "uniform float u_Roll;"

        +	"attribute vec4 a_Vertices;"
        +	"attribute vec3 a_Barycentric;"

        +	"varying vec3 v_Barycentric;"

        + 	"void main()"
        +	"{"
        +	"	v_Barycentric = a_Barycentric;"

        +   "   mat4 model;"
        +   "   model[0] = vec4(u_Right.x, u_Right.y, u_Right.z, 0);"
        +   "   model[1] = vec4(u_Up.x, u_Up.y, u_Up.z, 0);"
        +   "   model[2] = vec4(u_Forward.x, u_Forward.y, u_Forward.z, 0);"
        +   "   model[3] = vec4(u_Position.x, u_Position.y, u_Position.z, 1);"
/*
        +   "   model[0] = vec4(1, 0, 0, 0);"
        +   "   model[1] = vec4(0, 1, 0, 0);"
        +   "   model[2] = vec4(0, 0, 1, 0);"
        +   "   model[3] = vec4(0, 0, 0, 1);"
*/
        +   "   float cosTheta = cos(u_Roll);"
        +   "   float sinTheta = sin(u_Roll);"

        +   "   mat4 roll;"
        +   "   roll[0] = vec4(cosTheta, -sinTheta, 0, 0);"
        +   "   roll[1] = vec4(sinTheta,  cosTheta, 0, 0);"
        +   "   roll[2] = vec4(0, 0, 1, 0);"
        +   "   roll[3] = vec4(0, 0, 0, 1);"
/*
        +   "   roll[0] = vec4(1, 0, 0, 0);"
        +   "   roll[1] = vec4(0, 1, 0, 0);"
        +   "   roll[2] = vec4(0, 0, 1, 0);"
        +   "   roll[3] = vec4(0, 0, 0, 1);"
*/
        +   "   mat4 scale;"
        +   "   scale[0] = vec4(u_Scale.x, 0, 0, 0);"
        +   "   scale[1] = vec4(0, u_Scale.y, 0, 0);"
        +   "   scale[2] = vec4(0, 0, u_Scale.z, 0);"
        +   "   scale[3] = vec4(0, 0, 0, 1);"

        +	"	gl_Position = (u_ProjMatrix * (model * roll * scale)) * a_Vertices;"
        +	"}";

    public static final String fragmentShader_WIREFRAME =
            "precision lowp float;"

        +	"uniform vec4 u_Color;"
        +   "uniform float u_InnerIntensity;"

        +	"varying vec3 v_Barycentric;"

        +	"void main()"
        +	"{"
        +   "       float outerMultiplier = float(any(lessThan(v_Barycentric, vec3(0.06))));"
        +   "       float innerMultiplier = u_InnerIntensity * (1.0 - outerMultiplier);"

        +   "       vec4 outerColor = u_Color * outerMultiplier;"
        +   "       vec4 innerColor = u_Color * innerMultiplier;"

        +   "       gl_FragColor = outerColor + innerColor;"
        +   "       gl_FragColor.w = u_Color.w;"
        +	"}";

    public static final String fragmentShader_PHASED =
            "precision lowp float;"

        +	"uniform vec4 u_Color;"

        +	"varying vec3 v_Barycentric;"

        +	"void main()"
        +	"{"
        +   "   gl_FragColor = u_Color * inversesqrt((1.0 - length(v_Barycentric)) * 20.0);"
        +	"}";

    public static final String fragmentShader_BARYCENTRIC_HOLLOW =
            "precision lowp float;"

        +	"uniform vec4 u_Color;"

        +	"varying vec3 v_Barycentric;"

        +	"void main()"
        +	"{"
        +   "       gl_FragColor = u_Color * float(any(lessThan(v_Barycentric, vec3(0.06))));"
        +   "       gl_FragColor.w = 0.0;"
        +	"}";

    public static final String vertexShader_POINT =
            "uniform mat4 u_ProjMatrix;"
        +   "uniform vec4 u_EyePos;"

        +	"attribute vec4 a_Position;"
        +   "attribute vec4 a_Color;"

        +   "varying vec4 v_Color;"

        +   "void main()"
        +   "{"
        + "	    vec4 toEye;"
        + " 	toEye = u_EyePos - a_Position;"
        + "     float distance = length(toEye);"
        + "	    gl_PointSize = 30.0 * inversesqrt(distance);"

        +   "   v_Color = a_Color;"
        +	"	gl_Position = u_ProjMatrix * a_Position;"
        +   "}";

    public static final String fragmentShader_POINT =
            "precision lowp float;"

        +   "varying vec4 v_Color;"

        +   "void main()"
        +   "{"
        +   "	gl_FragColor = v_Color;"
        +   "}";

    public static final String vertexShader_POINTMULTIPLIER =
            "uniform mat4 u_ProjMatrix;"
        +   "uniform vec4 u_EyePos;"

        +	"attribute vec4 a_Position;"
        +   "attribute vec4 a_Color;"

        +   "varying vec4 v_Color;"

        +   "void main()"
        +   "{"
        + "	    vec4 toEye;"
        + " 	toEye = u_EyePos - a_Position;		"
        + "     float distance = length(toEye);	    "
        + "	    gl_PointSize = 90.0 * inversesqrt(distance);	"

        +   "   v_Color = a_Color;"
        +	"	gl_Position = u_ProjMatrix * a_Position;"
        +   "}";

    public static final String fragmentShader_POINTMULTIPLIER =
            "precision lowp float;"
        +   "varying vec4 v_Color;"

        +   "void main()"
        +   "{"
        +   " 	gl_FragColor = v_Color;"

        +	"	float x = (gl_PointCoord.x * 2.0) - 1.0;"		// Find the position of this fragment relative to the center of the glPoint.
        +	"	float y = (gl_PointCoord.y * 2.0) - 1.0;"
        +   "   float distSqr = (x * x) + (y * y);"

        +   "   gl_FragColor.a *= float(distSqr < 1.0);"
        + "}";

    public static final String vertexShader_SCROLLTEX =
            "uniform mat4 u_ProjMatrix;"
        +   "uniform vec4 u_WorldPos;"

        +	"attribute vec4 a_Position;  \n"
        +   "attribute vec2 a_TexCoord;  \n"

        +   "varying vec2 v_TexCoord;    \n"

        +   "void main()                 \n"
        +   "{"
        +	"   v_TexCoord = a_TexCoord; \n"

        +   "   mat4 world;"
        +   "   world[0] = vec4(1,0,0,0);"
        +   "   world[1] = vec4(0,1,0,0);"
        +   "   world[2] = vec4(0,0,1,0);"
        +   "   world[3] = u_WorldPos;"

        +   "   mat4 mvp = u_ProjMatrix * world;"
        +	"	gl_Position = mvp * a_Position;"
        +   "}";

    public static final String fragmentShader_SCROLLTEX =
            "precision lowp float;"
        +   "uniform vec4 u_Color;"
        + 	"uniform sampler2D u_Texture;"
        +   "uniform vec2 u_TexOffset;"
        +   "uniform float u_AttenuationCoefficient;"

        +   "varying vec2 v_TexCoord; "

        +   "void main()"
        +   "{"

        +   "   gl_FragColor = u_Color * texture2D(u_Texture, v_TexCoord + u_TexOffset);"

        +   "   float x = 4.0 - v_TexCoord.x;"
        +   "   float y = 4.0 - v_TexCoord.y;"

        + 	"	float dist = sqrt((x * x) + (y * y)) * 0.5;"		// Find the Distance of this fragment from the center.
        +   "   float attenuation = 1.0 / (1.0 + (u_AttenuationCoefficient * (dist * dist)));"
        + 	"	gl_FragColor.a *= attenuation;"
        +   "}";



    public static final String vertexShader_SCREENQUAD =
            "attribute vec4 a_Position;"
        +   "attribute vec2 a_TexCoord;"

        +   "varying vec2 v_TexCoord;"

        +   "void main()"
        +   "{"
        +   "  	gl_Position = a_Position;"
        +   "	v_TexCoord = a_TexCoord;"
        +   "}";

    public static final String fragmentShader_ADD =
          "precision lowp float;"
        + "uniform sampler2D u_TextureA;"
        + "uniform sampler2D u_TextureB;"
        + "uniform sampler2D u_TextureC;"
        + "varying vec2 v_TexCoord;"

        + "void main(void)"
        + "{"
        + "		gl_FragColor = texture2D(u_TextureA, v_TexCoord) + texture2D(u_TextureB, v_TexCoord) + texture2D(u_TextureC, v_TexCoord);"
        + "}";

    public static final String fragmentShader_FILMGRAIN =
            "precision highp float;"

        +   "uniform vec2 u_RandomOffset;"
        +   "uniform float u_Intensity;"
        +   "varying vec2 v_TexCoord;"

        +   "void main()"
        +   "{"
        +   "   float rand = u_Intensity * fract(sin(dot((v_TexCoord + u_RandomOffset) * 0.5, vec2(12.9898, 78.233)))* 43758.5453);"
        +   "   gl_FragColor = vec4(1.0, 1.0, 1.0, rand);"
        +   "}";

    public static final String fragmentShader_BLURV =
            "precision lowp float;"
        +   "uniform sampler2D u_Texture;" // this should hold the texture rendered by the horizontal blur pass
        +   "uniform float u_GlowIntensity;"
        +   "varying vec2 v_TexCoord;"

        +   "const float blurSize = 1.0/256.0;"

        +   "void main(void)"
        +   "{"
        +   "   vec4 sum = vec4(0.0);"

        // blur in y (vertical)
        // take nine samples, with the distance blurSize between them
        +   "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y - 4.0*blurSize)) * 0.05;"
        +   "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y - 3.0*blurSize)) * 0.09;"
        +   "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y - 2.0*blurSize)) * 0.12;"
        +   "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y - blurSize)) * 0.15;"
        +   "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y)) * 0.16;"
        +   "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y + blurSize)) * 0.15;"
        +   "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y + 2.0*blurSize)) * 0.12;"
        +   "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y + 3.0*blurSize)) * 0.09;"
        +   "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y + 4.0*blurSize)) * 0.05;"

        + "     gl_FragColor = sum * u_GlowIntensity;"

        + "}";

    public static final String fragmentShader_BLURH =
          "  precision lowp float;"
        + "  uniform sampler2D u_Texture;" // the texture with the scene you want to blur
        + "	    uniform float u_GlowIntensity;"
        + "     varying vec2 v_TexCoord;"

        + "     const float blurSize = 1.0/256.0;"

        + "   void main(void)"
        + "   {"
        + "      vec4 sum = vec4(0.0);"

        // blur in x (Horizontal)
        // take nine samples, with the distance blurSize between them
        + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x - 4.0*blurSize, v_TexCoord.y)) * 0.05;"
        + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x - 3.0*blurSize, v_TexCoord.y)) * 0.09;"
        + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x - 2.0*blurSize, v_TexCoord.y)) * 0.12;"
        + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x - blurSize, v_TexCoord.y)) * 0.15;"
        + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y)) * 0.16;"
        + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x + blurSize, v_TexCoord.y)) * 0.15;"
        + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x + 2.0*blurSize, v_TexCoord.y)) * 0.12;"
        + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x + 3.0*blurSize, v_TexCoord.y)) * 0.09;"
        + "      sum += texture2D(u_Texture, vec2(v_TexCoord.x + 4.0*blurSize, v_TexCoord.y)) * 0.05;"

        + "      gl_FragColor = sum * u_GlowIntensity;"
        + "   }";
}