package com.raggamuffin.protorunnerv2.renderer;

import android.content.Context;
import android.opengl.GLES20;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.ui.Font;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UILabel;
import com.raggamuffin.protorunnerv2.ui.UIPanel;
import com.raggamuffin.protorunnerv2.ui.UIProgressBar;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector2;

public class UIRenderManager
{
    public final int NUM_TEXTURES = 3;

    private int m_TextureHandles[];
    private int m_ResourceIDs[];

    private Context m_Context;

    private GLTextQuad m_TextQuad;
    private GLTexQuad m_TexQuad;

    private float[] m_TempTexCoord;

    public UIRenderManager(Context context)
    {
        m_TextureHandles 	= new int[NUM_TEXTURES];
        m_ResourceIDs  		= new int[NUM_TEXTURES];
        m_ResourceIDs[0] 	= R.drawable.moire;
        m_ResourceIDs[1] 	= R.drawable.blank;
        m_ResourceIDs[2]    = R.drawable.border;

        m_Context 	 = context;

        m_TextQuad = null;
        m_TexQuad  = null;

        m_TempTexCoord = new float[2];
    }

    public void LoadAssets()
    {
        LoadModels();
        TextureLoader.LoadTextures(m_Context, m_ResourceIDs, m_TextureHandles);
    }

    private void LoadModels()
    {
        m_TextQuad = new GLTextQuad();
        m_TexQuad  = new GLTexQuad();
    }

    public void DrawElement(final UIElement element, float[] view)
    {
        if(!element.IsHidden())
        {
            switch (element.GetType())
            {
                case Label:
                    DrawText((UILabel) element, view);
                    break;

                case ProgressBar:
                    DrawProgressBar((UIProgressBar) element, view);
                    break;

                case Panel:
                    DrawPanel((UIPanel) element, view);
                    break;

                case Undefined:
                default:
                    break;
            }
        }
    }

    private void DrawText(final UILabel element, float[] projMatrix)
    {
        m_TextQuad.InitialiseModel(projMatrix, null);

        Vector2 position = element.GetPosition();
        Font textFont = element.GetFont();
        String text = element.GetText();
        int length = text.length();
        float fontSize = (float)textFont.GetSize();

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_TextureHandles[0]);

        m_TextQuad.SetColour(textFont.GetColour());

        for(int i = 0; i < length; i++)
        {
            float x = (float)position.I + fontSize * i;
            float y = (float)position.J;

            m_TextQuad.SetOffset(GetTexCoord(text.charAt(i)));
            m_TextQuad.draw(x, y, fontSize, projMatrix);
        }

        m_TextQuad.CleanModel();
    }

    private void DrawProgressBar(final UIProgressBar element, final float[] projMatrix)
    {
        m_TexQuad.InitialiseModel(projMatrix, null);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_TextureHandles[1]);

        Vector2 underBarPosition = element.GetUnderBarPosition();
        Vector2 underBarSize = element.GetUnderBarScale();
        Vector2 Position = element.GetPosition();
        Vector2 size = element.GetSize();

        m_TexQuad.draw(underBarPosition, underBarSize, element.GetUnderColour(), projMatrix);	// Draw UnderLayer
        m_TexQuad.draw(Position, size, element.GetColour(), projMatrix);      // Draw OverLayer

        m_TextQuad.CleanModel();
    }

    private void DrawPanel(final UIPanel element, final float[] projMatrix)
    {
        m_TexQuad.InitialiseModel(projMatrix, null);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_TextureHandles[2]);

        Colour col = new Colour(element.GetColour());
        col.Alpha = element.GetAlpha();

        m_TexQuad.draw(element.GetPosition(), element.GetSize(), col, projMatrix);
        m_TexQuad.CleanModel();
    }

    private float[] GetTexCoord(char c)
    {
        switch(c)
        {
            case ' ':
                m_TempTexCoord[0] = 0.0f;
                m_TempTexCoord[1] = 0.0f;
                break;

            ///// Lower case.
            case 'a':
                m_TempTexCoord[0] = 0.0625f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'b':
                m_TempTexCoord[0] = 0.125f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'c':
                m_TempTexCoord[0] = 0.1875f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'd':
                m_TempTexCoord[0] = 0.25f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'e':
                m_TempTexCoord[0] = 0.3125f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'f':
                m_TempTexCoord[0] = 0.375f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'g':
                m_TempTexCoord[0] = 0.4375f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'h':
                m_TempTexCoord[0] = 0.5f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'i':
                m_TempTexCoord[0] = 0.5625f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'j':
                m_TempTexCoord[0] = 0.625f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'k':
                m_TempTexCoord[0] = 0.6875f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'l':
                m_TempTexCoord[0] = 0.75f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'm':
                m_TempTexCoord[0] = 0.8125f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'n':
                m_TempTexCoord[0] = 0.875f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'o':
                m_TempTexCoord[0] = 0.9375f;
                m_TempTexCoord[1] = 0.375f;
                break;

            case 'p':
                m_TempTexCoord[0] = 0.0f;
                m_TempTexCoord[1] = 0.4375f;
                break;

            case 'q':
                m_TempTexCoord[0] = 0.0625f;
                m_TempTexCoord[1] = 0.4375f;
                break;

            case 'r':
                m_TempTexCoord[0] = 0.125f;
                m_TempTexCoord[1] = 0.4375f;
                break;

            case 's':
                m_TempTexCoord[0] = 0.1875f;
                m_TempTexCoord[1] = 0.4375f;
                break;

            case 't':
                m_TempTexCoord[0] = 0.25f;
                m_TempTexCoord[1] = 0.4375f;
                break;

            case 'u':
                m_TempTexCoord[0] = 0.3125f;
                m_TempTexCoord[1] = 0.4375f;
                break;

            case 'v':
                m_TempTexCoord[0] = 0.375f;
                m_TempTexCoord[1] = 0.4375f;
                break;

            case 'w':
                m_TempTexCoord[0] = 0.4375f;
                m_TempTexCoord[1] = 0.4375f;
                break;

            case 'x':
                m_TempTexCoord[0] = 0.5f;
                m_TempTexCoord[1] = 0.4375f;
                break;

            case 'y':
                m_TempTexCoord[0] = 0.5625f;
                m_TempTexCoord[1] = 0.4375f;
                break;

            case 'z':
                m_TempTexCoord[0] = 0.625f;
                m_TempTexCoord[1] = 0.4375f;
                break;

            ///// Upper case.
            case 'A':
                m_TempTexCoord[0] = 0.0625f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'B':
                m_TempTexCoord[0] = 0.125f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'C':
                m_TempTexCoord[0] = 0.1875f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'D':
                m_TempTexCoord[0] = 0.25f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'E':
                m_TempTexCoord[0] = 0.3125f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'F':
                m_TempTexCoord[0] = 0.375f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'G':
                m_TempTexCoord[0] = 0.4375f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'H':
                m_TempTexCoord[0] = 0.5f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'I':
                m_TempTexCoord[0] = 0.5625f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'J':
                m_TempTexCoord[0] = 0.625f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'K':
                m_TempTexCoord[0] = 0.6875f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'L':
                m_TempTexCoord[0] = 0.75f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'M':
                m_TempTexCoord[0] = 0.8125f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'N':
                m_TempTexCoord[0] = 0.875f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'O':
                m_TempTexCoord[0] = 0.9375f;
                m_TempTexCoord[1] = 0.25f;
                break;

            case 'P':
                m_TempTexCoord[0] = 0.0f;
                m_TempTexCoord[1] = 0.3125f;
                break;

            case 'Q':
                m_TempTexCoord[0] = 0.0625f;
                m_TempTexCoord[1] = 0.3125f;
                break;

            case 'R':
                m_TempTexCoord[0] = 0.125f;
                m_TempTexCoord[1] = 0.3125f;
                break;

            case 'S':
                m_TempTexCoord[0] = 0.1875f;
                m_TempTexCoord[1] = 0.3125f;
                break;

            case 'T':
                m_TempTexCoord[0] = 0.25f;
                m_TempTexCoord[1] = 0.3125f;
                break;

            case 'U':
                m_TempTexCoord[0] = 0.3125f;
                m_TempTexCoord[1] = 0.3125f;
                break;

            case 'V':
                m_TempTexCoord[0] = 0.375f;
                m_TempTexCoord[1] = 0.3125f;
                break;

            case 'W':
                m_TempTexCoord[0] = 0.4375f;
                m_TempTexCoord[1] = 0.3125f;
                break;

            case 'X':
                m_TempTexCoord[0] = 0.5f;
                m_TempTexCoord[1] = 0.3125f;
                break;

            case 'Y':
                m_TempTexCoord[0] = 0.5625f;
                m_TempTexCoord[1] = 0.3125f;
                break;

            case 'Z':
                m_TempTexCoord[0] = 0.625f;
                m_TempTexCoord[1] = 0.3125f;
                break;

            ///// Numbers.
            case '0':
                m_TempTexCoord[0] = 0.0f;
                m_TempTexCoord[1] = 0.1875f;
                break;

            case '1':
                m_TempTexCoord[0] = 0.0625f;
                m_TempTexCoord[1] = 0.1875f;
                break;

            case '2':
                m_TempTexCoord[0] = 0.125f;
                m_TempTexCoord[1] = 0.1875f;
                break;

            case '3':
                m_TempTexCoord[0] = 0.1875f;
                m_TempTexCoord[1] = 0.1875f;
                break;

            case '4':
                m_TempTexCoord[0] = 0.25f;
                m_TempTexCoord[1] = 0.1875f;
                break;

            case '5':
                m_TempTexCoord[0] = 0.3125f;
                m_TempTexCoord[1] = 0.1875f;
                break;

            case '6':
                m_TempTexCoord[0] = 0.375f;
                m_TempTexCoord[1] = 0.1875f;
                break;

            case '7':
                m_TempTexCoord[0] = 0.4375f;
                m_TempTexCoord[1] = 0.1875f;
                break;

            case '8':
                m_TempTexCoord[0] = 0.5f;
                m_TempTexCoord[1] = 0.1875f;
                break;

            case '9':
                m_TempTexCoord[0] = 0.5625f;
                m_TempTexCoord[1] = 0.1875f;
                break;

            ///// Misc.

            case '>':
                m_TempTexCoord[0] = 0.875f;
                m_TempTexCoord[1] = 0.1875f;
                break;

            case '<':
                m_TempTexCoord[0] = 0.75f;
                m_TempTexCoord[1] = 0.1875f;
                break;

            case ':':
                m_TempTexCoord[0] = 0.6250f;
                m_TempTexCoord[1] = 0.1875f;
                break;

            case '&':
                m_TempTexCoord[0] = 0.3750f;
                m_TempTexCoord[1] = 0.125f;
                break;

            case '.':
                m_TempTexCoord[0] = 0.875f;
                m_TempTexCoord[1] = 0.1250f;
                break;

            case '!':
                m_TempTexCoord[0] = 0.0625f;
                m_TempTexCoord[1] = 0.125f;
                break;

            case '?':
                m_TempTexCoord[0] = 0.9375f;
                m_TempTexCoord[1] = 0.1875f;
                break;

            case '£' :
                m_TempTexCoord[0] = 0.0f;
                m_TempTexCoord[1] = 0.5625f;
                break;

            case '$' :
                m_TempTexCoord[0] = 0.0f;
                m_TempTexCoord[1] = 0.625f;
                break;

            case'-':
                m_TempTexCoord[0] = 0.375f;
                m_TempTexCoord[1] = 0.5625f;
                break;

            case',':
                m_TempTexCoord[0] = 0.75f;
                m_TempTexCoord[1] = 0.125f;
                break;

            case'\'':
                m_TempTexCoord[0] = 0.4375f;
                m_TempTexCoord[1] = 0.125f;
                break;
            case'_':
                m_TempTexCoord[0] = 0.9375f;
                m_TempTexCoord[1] = 0.3125f;
                break;
        }

        return m_TempTexCoord;
    }
}
