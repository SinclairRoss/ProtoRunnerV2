package com.raggamuffin.protorunnerv2.renderer;

// Author: Sinclair Ross
// Date:   03/06/2017

import android.content.Context;
import android.opengl.GLES31;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.ui.UIElement_Label;
import com.raggamuffin.protorunnerv2.utils.Vector2;

public class TextRenderer
{
    private GLTextQuad m_TextQuad;
    private int m_TextureHandle;

    private float[] m_TexCoords;

    public TextRenderer()
    {
        m_TexCoords = new float[2];
    }

    public void LoadAssets(Context context)
    {
        m_TextQuad = new GLTextQuad();
        m_TextureHandle = GetTextureHandle(context);
    }

    public void PrepareForRender(float[] viewMatrix)
    {
        m_TextQuad.InitialiseModel(viewMatrix, null);

        GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, m_TextureHandle);
    }

    public void DrawText(final UIElement_Label element)
    {
        Vector2 position = element.GetPosition();
        String text = element.GetText();
        int length = text.length();
        float fontSize = (float)element.GetFontSize();

        for(int i = 0; i < length; ++i)
        {
            float x = (float)position.X + fontSize * i;
            float y = (float)position.Y;

            m_TextQuad.SetOffset(GetTexCoord(text.charAt(i)));
            m_TextQuad.draw(x, y, fontSize, element.GetColour());
        }
    }

    public void CleanUp()
    {
        m_TextQuad.CleanModel();
    }

    private int GetTextureHandle(Context context)
    {
        int m_TextureHandles[] = new int[1];
        int m_ResourceIDs[] = new int[1];
        m_ResourceIDs[0] = R.drawable.moire;

        TextureLoader.LoadTextures(context, m_ResourceIDs, m_TextureHandles);

        return m_TextureHandles[0];
    }

    private float[] GetTexCoord(char c)
    {
        switch(c)
        {
            case ' ':
                m_TexCoords[0] = 0.0f;
                m_TexCoords[1] = 0.0f;
                break;

            ///// Lower case.
            case 'a':
                m_TexCoords[0] = 0.0625f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'b':
                m_TexCoords[0] = 0.125f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'c':
                m_TexCoords[0] = 0.1875f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'd':
                m_TexCoords[0] = 0.25f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'e':
                m_TexCoords[0] = 0.3125f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'f':
                m_TexCoords[0] = 0.375f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'g':
                m_TexCoords[0] = 0.4375f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'h':
                m_TexCoords[0] = 0.5f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'i':
                m_TexCoords[0] = 0.5625f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'j':
                m_TexCoords[0] = 0.625f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'k':
                m_TexCoords[0] = 0.6875f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'l':
                m_TexCoords[0] = 0.75f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'm':
                m_TexCoords[0] = 0.8125f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'n':
                m_TexCoords[0] = 0.875f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'o':
                m_TexCoords[0] = 0.9375f;
                m_TexCoords[1] = 0.375f;
                break;

            case 'p':
                m_TexCoords[0] = 0.0f;
                m_TexCoords[1] = 0.4375f;
                break;

            case 'q':
                m_TexCoords[0] = 0.0625f;
                m_TexCoords[1] = 0.4375f;
                break;

            case 'r':
                m_TexCoords[0] = 0.125f;
                m_TexCoords[1] = 0.4375f;
                break;

            case 's':
                m_TexCoords[0] = 0.1875f;
                m_TexCoords[1] = 0.4375f;
                break;

            case 't':
                m_TexCoords[0] = 0.25f;
                m_TexCoords[1] = 0.4375f;
                break;

            case 'u':
                m_TexCoords[0] = 0.3125f;
                m_TexCoords[1] = 0.4375f;
                break;

            case 'v':
                m_TexCoords[0] = 0.375f;
                m_TexCoords[1] = 0.4375f;
                break;

            case 'w':
                m_TexCoords[0] = 0.4375f;
                m_TexCoords[1] = 0.4375f;
                break;

            case 'x':
                m_TexCoords[0] = 0.5f;
                m_TexCoords[1] = 0.4375f;
                break;

            case 'y':
                m_TexCoords[0] = 0.5625f;
                m_TexCoords[1] = 0.4375f;
                break;

            case 'z':
                m_TexCoords[0] = 0.625f;
                m_TexCoords[1] = 0.4375f;
                break;

            ///// Upper case.
            case 'A':
                m_TexCoords[0] = 0.0625f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'B':
                m_TexCoords[0] = 0.125f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'C':
                m_TexCoords[0] = 0.1875f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'D':
                m_TexCoords[0] = 0.25f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'E':
                m_TexCoords[0] = 0.3125f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'F':
                m_TexCoords[0] = 0.375f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'G':
                m_TexCoords[0] = 0.4375f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'H':
                m_TexCoords[0] = 0.5f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'I':
                m_TexCoords[0] = 0.5625f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'J':
                m_TexCoords[0] = 0.625f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'K':
                m_TexCoords[0] = 0.6875f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'L':
                m_TexCoords[0] = 0.75f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'M':
                m_TexCoords[0] = 0.8125f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'N':
                m_TexCoords[0] = 0.875f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'O':
                m_TexCoords[0] = 0.9375f;
                m_TexCoords[1] = 0.25f;
                break;

            case 'P':
                m_TexCoords[0] = 0.0f;
                m_TexCoords[1] = 0.3125f;
                break;

            case 'Q':
                m_TexCoords[0] = 0.0625f;
                m_TexCoords[1] = 0.3125f;
                break;

            case 'R':
                m_TexCoords[0] = 0.125f;
                m_TexCoords[1] = 0.3125f;
                break;

            case 'S':
                m_TexCoords[0] = 0.1875f;
                m_TexCoords[1] = 0.3125f;
                break;

            case 'T':
                m_TexCoords[0] = 0.25f;
                m_TexCoords[1] = 0.3125f;
                break;

            case 'U':
                m_TexCoords[0] = 0.3125f;
                m_TexCoords[1] = 0.3125f;
                break;

            case 'V':
                m_TexCoords[0] = 0.375f;
                m_TexCoords[1] = 0.3125f;
                break;

            case 'W':
                m_TexCoords[0] = 0.4375f;
                m_TexCoords[1] = 0.3125f;
                break;

            case 'X':
                m_TexCoords[0] = 0.5f;
                m_TexCoords[1] = 0.3125f;
                break;

            case 'Y':
                m_TexCoords[0] = 0.5625f;
                m_TexCoords[1] = 0.3125f;
                break;

            case 'Z':
                m_TexCoords[0] = 0.625f;
                m_TexCoords[1] = 0.3125f;
                break;

            ///// Numbers.
            case '0':
                m_TexCoords[0] = 0.0f;
                m_TexCoords[1] = 0.1875f;
                break;

            case '1':
                m_TexCoords[0] = 0.0625f;
                m_TexCoords[1] = 0.1875f;
                break;

            case '2':
                m_TexCoords[0] = 0.125f;
                m_TexCoords[1] = 0.1875f;
                break;

            case '3':
                m_TexCoords[0] = 0.1875f;
                m_TexCoords[1] = 0.1875f;
                break;

            case '4':
                m_TexCoords[0] = 0.25f;
                m_TexCoords[1] = 0.1875f;
                break;

            case '5':
                m_TexCoords[0] = 0.3125f;
                m_TexCoords[1] = 0.1875f;
                break;

            case '6':
                m_TexCoords[0] = 0.375f;
                m_TexCoords[1] = 0.1875f;
                break;

            case '7':
                m_TexCoords[0] = 0.4375f;
                m_TexCoords[1] = 0.1875f;
                break;

            case '8':
                m_TexCoords[0] = 0.5f;
                m_TexCoords[1] = 0.1875f;
                break;

            case '9':
                m_TexCoords[0] = 0.5625f;
                m_TexCoords[1] = 0.1875f;
                break;

            ///// Misc.

            case '>':
                m_TexCoords[0] = 0.875f;
                m_TexCoords[1] = 0.1875f;
                break;

            case '<':
                m_TexCoords[0] = 0.75f;
                m_TexCoords[1] = 0.1875f;
                break;

            case ':':
                m_TexCoords[0] = 0.6250f;
                m_TexCoords[1] = 0.1875f;
                break;

            case '&':
                m_TexCoords[0] = 0.3750f;
                m_TexCoords[1] = 0.125f;
                break;

            case '.':
                m_TexCoords[0] = 0.875f;
                m_TexCoords[1] = 0.1250f;
                break;

            case '!':
                m_TexCoords[0] = 0.0625f;
                m_TexCoords[1] = 0.125f;
                break;

            case '?':
                m_TexCoords[0] = 0.9375f;
                m_TexCoords[1] = 0.1875f;
                break;

            case '£' :
                m_TexCoords[0] = 0.0f;
                m_TexCoords[1] = 0.5625f;
                break;

            case '$' :
                m_TexCoords[0] = 0.0f;
                m_TexCoords[1] = 0.625f;
                break;

            case'-':
                m_TexCoords[0] = 0.375f;
                m_TexCoords[1] = 0.5625f;
                break;

            case',':
                m_TexCoords[0] = 0.75f;
                m_TexCoords[1] = 0.125f;
                break;

            case'\'':
                m_TexCoords[0] = 0.4375f;
                m_TexCoords[1] = 0.125f;
                break;
            case'_':
                m_TexCoords[0] = 0.9375f;
                m_TexCoords[1] = 0.3125f;
                break;
        }

        return m_TexCoords;
    }
}
