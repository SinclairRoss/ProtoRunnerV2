package com.raggamuffin.protorunnerv2.renderer;

public class Shaders
{
    public static final String fragmentShader_STANDARD =
            "precision mediump float;"
        +   "uniform vec4 u_Color;"

        +   "void main()"
        +   "{"
        +   "	gl_FragColor = u_Color;"
        +   "}";

    public static final String vertexShader_STANDARD =
            "uniform mat4 u_ProjMatrix;"
        +   "uniform vec4 u_WorldPos;"
        +   "uniform float u_Yaw;"
        +   "uniform float u_Roll;"
        +   "uniform float u_Pitch;"
        +   "uniform vec3 u_Scale;"

        +	"attribute vec4 a_Position;"

        + 	"void main()"
        +	"{"
        +   "   float cosT = cos(u_Yaw);"
        +   "   float sinT = sin(u_Yaw);"

        +   "   mat4 world;"
        +   "   world[0] = vec4(cosT, 0, sinT, 0);"
        +   "   world[1] = vec4(0, 1, 0, 0);"
        +   "   world[2] = vec4(-sinT,0, cosT,0);"
        +   "   world[3] = u_WorldPos;"

        +   "   cosT = cos(u_Roll);"
        +   "   sinT = sin(u_Roll);"

        +   "   mat4 roll;"
        +   "   roll[0] = vec4(cosT, -sinT, 0, 0);"
        +   "   roll[1] = vec4(sinT, cosT,  0, 0);"
        +   "   roll[2] = vec4(0, 0, 1, 0);"
        +   "   roll[3] = vec4(0, 0, 0, 1);"

        +   "   cosT = cos(u_Pitch);"
        +   "   sinT = sin(u_Pitch);"

        +   "   mat4 pitch;"
        +   "   pitch[0] = vec4(1, 0, 0, 0);"
        +   "   pitch[1] = vec4(0, cosT,-sinT, 0);"
        +   "   pitch[2] = vec4(0, sinT, cosT, 0);"
        +   "   pitch[3] = vec4(0, 0, 0, 1);"

        +   "   mat4 scale;"
        +   "   scale[0] = vec4(u_Scale.x, 0, 0, 0);"
        +   "   scale[1] = vec4(0, u_Scale.y, 0, 0);"
        +   "   scale[2] = vec4(0, 0, u_Scale.z, 0);"
        +   "   scale[3] = vec4(0, 0, 0, 1);"

        +   "   mat4 mvp = u_ProjMatrix * (world * pitch * roll * scale);"
        +	"	gl_Position = mvp * a_Position;"
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
            "precision mediump float;"

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
            "precision mediump float;"

        +   "varying vec4 v_Color;"

        +   "void main()"
        +   "{"
        +   "	gl_FragColor = v_Color;"
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
            "precision mediump float;"

        + 	"uniform sampler2D u_Texture;"
        +   "uniform vec2 u_TexOffset;"
        +   "uniform vec4 u_Color;"

        + 	"varying vec2 v_TexCoord;"

        +   "void main()"
        +   "{"
        +   "	gl_FragColor = u_Color * texture2D(u_Texture, v_TexCoord + u_TexOffset);"
        +   "}";

    public static final String vertexShader_BARYCENTRIC =
            "uniform mat4 u_ProjMatrix;"
        +   "uniform vec4 u_WorldPos;"
        +   "uniform float u_Yaw;"
        +   "uniform float u_Roll;"
        +   "uniform vec3 u_Scale;"

        +	"attribute vec4 a_Position;"
        +	"attribute vec3 a_Barycentric;"

        +	"varying vec3 v_Barycentric;"

        + 	"void main()"
        +	"{"
        +	"	v_Barycentric = a_Barycentric;"

        +   "   float cosT = cos(u_Yaw);"
        +   "   float sinT = sin(u_Yaw);"

        +   "   mat4 world;"
        +   "   world[0] = vec4(cosT, 0, sinT, 0);"
        +   "   world[1] = vec4(0, 1, 0, 0);"
        +   "   world[2] = vec4(-sinT,0, cosT,0);"
        +   "   world[3] = u_WorldPos;"

        +   "   cosT = cos(u_Roll);"
        +   "   sinT = sin(u_Roll);"

        +   "   mat4 roll;"
        +   "   roll[0] = vec4(cosT, -sinT, 0, 0);"
        +   "   roll[1] = vec4(sinT, cosT,  0, 0);"
        +   "   roll[2] = vec4(0, 0, 1, 0);"
        +   "   roll[3] = vec4(0, 0, 0, 1);"

        +   "   mat4 scale;"
        +   "   scale[0] = vec4(u_Scale.x, 0, 0, 0);"
        +   "   scale[1] = vec4(0, u_Scale.y, 0, 0);"
        +   "   scale[2] = vec4(0, 0, u_Scale.z, 0);"
        +   "   scale[3] = vec4(0, 0, 0, 1);"

        +   "   mat4 mvp = u_ProjMatrix * (world * roll * scale);"
        +	"	gl_Position = mvp * a_Position;"
        +	"}";


    public static final String fragmentShader_BARYCENTRIC =
            "precision mediump float;"

        +	"uniform vec4 u_Color;"

        +	"varying vec3 v_Barycentric;"

        +	"void main()"
        +	"{"
        +   "       gl_FragColor = u_Color * float(any(lessThan(v_Barycentric, vec3(0.06))));"
        +   "       gl_FragColor.w = u_Color.w;"
        +	"}";

    public static final String fragmentShader_BARYCENTRIC_HOLLOW =
            "precision mediump float;"

        +	"uniform vec4 u_Color;"

        +	"varying vec3 v_Barycentric;"

        +	"void main()"
        +	"{"
        +   "       gl_FragColor = u_Color * float(any(lessThan(v_Barycentric, vec3(0.06))));"
        +   "       gl_FragColor.w = 0.0;"
        +	"}";

    public static final String vertexShader_BULLET =
          "uniform mat4 u_ProjMatrix;       \n"     // A constant representing the combined model/view/projection matrix.
        + "uniform float u_Size;			\n"
        + "uniform vec4 u_EyePos;			\n"
        + "uniform vec4 u_WorldPos;		    \n"

        + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.

        + "void main()                    \n"
        + "{                              \n"
        + "	    vec4 toEye;					        \n"
        + " 	toEye = u_EyePos - u_WorldPos;		\n"
        + "     float distance = length(toEye);	    \n"
        + "	    gl_PointSize = u_Size * inversesqrt(distance);		\n"

        +   "   mat4 world;"
        +   "   world[0] = vec4(1, 0, 0, 0);"
        +   "   world[1] = vec4(0, 1, 0, 0);"
        +   "   world[2] = vec4(0, 0, 1, 0);"
        +   "   world[3] = u_WorldPos;"

        +   "   mat4 mvp = u_ProjMatrix * world;"
        +	"	gl_Position = mvp * a_Position;"
        + "}                              \n";


    public static final String vertexShader_POINT =
            "uniform mat4 u_ProjMatrix;"
        +   "uniform vec4 u_EyePos;			\n"

        +	"attribute vec4 a_Position;  \n"
        +   "attribute vec4 a_Color;"

        +   "varying vec4 v_Color;"

        +   "void main()                 \n"
        +   "{"
        + "	    vec4 toEye;					        \n"
        + " 	toEye = u_EyePos - a_Position;		"
        + "     float distance = length(toEye);	    "
        + "	    gl_PointSize = 30.0 * inversesqrt(distance);	"

        +   "   v_Color = a_Color;"
        +	"	gl_Position = u_ProjMatrix * a_Position;"
        +   "}";

    public static final String fragmentShader_POINT =
            "precision mediump float;"

        +   "varying vec4 v_Color;"

        +   "void main()"
        +   "{"
        +   "	gl_FragColor = v_Color;"
        +   "}";

    public static final String fragmentShader_FADEPOINT =
            "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
        + "uniform vec4 u_Color;			\n"     // triangle per fragment.

        + "void main()                    \n"     // The entry point for our fragment shader.
        + "{                              \n"
        +	"	float x = (gl_PointCoord.x * 2.0) - 1.0;	\n"		// Find the position of this fragment relative to the center of the glPoint.
        +	"	float y = (gl_PointCoord.y * 2.0) - 1.0;	\n"

        + "	    float Dist = sqrt((x * x) + (y * y)); 		\n"		// Find the Distance of this fragment from the center.

        + " 	gl_FragColor = u_Color;    					\n"  	// Set the fragment colour

        + "	    if(Dist > 1.0)								\n"		// if the distance from the center is less than 0.1 we set the alpha value to 1
        + "	    {                              				\n"		// this creates a solid center for the particle and creates the illusion of glow
        + "  		gl_FragColor.a = 0.0;					\n"		// without using glow shaders.
        + "	    }                              				\n"
        + "}                              				\n";

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
            "precision mediump float;		\n"
        +   "uniform vec4 u_Color;			\n"
        + 	"uniform sampler2D u_Texture;	\n"
        +   "uniform vec2 u_TexOffset;		\n"

        +   "varying vec2 v_TexCoord;    	\n"

        +   "void main()"
        +   "{"
        +   "	gl_FragColor = u_Color * texture2D(u_Texture, v_TexCoord + u_TexOffset);"

        +   "   float x = 2.0 - v_TexCoord.x;		\n"
        +   "   float y = 2.0 - v_TexCoord.y;		\n"

        + 	"	float Dist = (sqrt((x * x) + (y * y)) * 0.5); 		\n"		// Find the Distance of this fragment from the center.
        +	"	float Alpha = 1.0 - ((Dist - 0.4) / 0.6);			\n"
        + 	"	gl_FragColor.a *= Alpha * 0.5;	\n"
        +   "}";

    public static final String vertexShader_SCREENQUAD =
            "attribute vec4 a_Position;"
        + "attribute vec2 a_TexCoord;"

        + "varying vec2 v_TexCoord;"

        + "void main()"
        + "{"
        + "  	gl_Position = a_Position;"
        + "		v_TexCoord = a_TexCoord;"
        + "}";

    public static final String fragmentShader_ADD =
          "precision mediump float;"
        + "uniform sampler2D u_TextureA;"
        + "uniform sampler2D u_TextureB;"
        + "varying vec2 v_TexCoord;"

        + "void main(void)"
        + "{"
        + "		gl_FragColor = texture2D(u_TextureA, v_TexCoord) + texture2D(u_TextureB, v_TexCoord);"
        + "}";


    public static final String fragmentShader_BLURV =
            "precision mediump float;"
                    + "uniform sampler2D u_Texture;" // this should hold the texture rendered by the horizontal blur pass
                    + "uniform float u_GlowIntensity;"
                    + "varying vec2 v_TexCoord;"

                    + "const float blurSize = 1.0/256.0;"

                    + "void main(void)"
                    + "{"
                    + "   vec4 sum = vec4(0.0);"

                    // blur in y (vertical)
                    // take nine samples, with the distance blurSize between them
                    + "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y - 4.0*blurSize)) * 0.05;"
                    + "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y - 3.0*blurSize)) * 0.09;"
                    + "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y - 2.0*blurSize)) * 0.12;"
                    + "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y - blurSize)) * 0.15;"
                    + "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y)) * 0.16;"
                    + "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y + blurSize)) * 0.15;"
                    + "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y + 2.0*blurSize)) * 0.12;"
                    + "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y + 3.0*blurSize)) * 0.09;"
                    + "   sum += texture2D(u_Texture, vec2(v_TexCoord.x, v_TexCoord.y + 4.0*blurSize)) * 0.05;"

                    + "   gl_FragColor = sum * u_GlowIntensity;"

                    + "}";

    public static final String fragmentShader_BLURH =
            "  precision mediump float;"
                    + "  uniform sampler2D u_Texture;" // the texture with the scene you want to blur
                    + "	 uniform float u_GlowIntensity;"
                    + "  varying vec2 v_TexCoord;"

                    + "const float blurSize = 1.0/256.0;"

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