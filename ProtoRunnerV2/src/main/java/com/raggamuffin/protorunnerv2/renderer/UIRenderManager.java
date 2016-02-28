package com.raggamuffin.protorunnerv2.renderer;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.ui.Font;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UILabel;
import com.raggamuffin.protorunnerv2.ui.UIPanel;
import com.raggamuffin.protorunnerv2.ui.UIProgressBar;
import com.raggamuffin.protorunnerv2.utils.Colour;
import com.raggamuffin.protorunnerv2.utils.Vector2;

import android.content.Context;
import android.opengl.GLES20;

public class UIRenderManager
{
    public final int NUM_TEXTURES = 3;

    private int m_TextureHandles[];
    private int m_ResourceIDs[];

    private Context m_Context;

    private GLTextQuad m_TextQuad;
    private GLTexQuad m_TexQuad;

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
        if(element.IsHidden())
            return;

        switch(element.GetType())
        {
            case Label:
                DrawText((UILabel)element, view);
                break;

            case ProgressBar:
                DrawProgressBar((UIProgressBar)element, view);
                break;

            case Panel:
                DrawPanel((UIPanel)element, view);
                break;

            case Undefined:
            default:
                break;
        }
    }

    private void DrawText(final UILabel Element, float[] projMatrix)
    {
        m_TextQuad.InitialiseModel(projMatrix, null);

        Vector2 Position = Element.GetPosition();
        Font TextFont 	 = Element.GetFont();
        String Text 	 = Element.GetText();
        int Length 		 = Text.length();
        float fontSize 	 = (float)TextFont.GetSize();

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_TextureHandles[0]);

        m_TextQuad.SetColour(TextFont.GetColour());

        for(int i = 0; i < Length; i++)
        {
            float x = (float)Position.I + fontSize * i;
            float y = (float)Position.J;

            m_TextQuad.SetOffset(GetTexCoord(Text.charAt(i)));
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
        float coord[] = new float [2];

        switch(c)
        {
            case ' ':
                coord[0] = 0.0f;
                coord[1] = 0.0f;
                break;

            ///// Lower case.
            case 'a':
                coord[0] = 0.0625f;
                coord[1] = 0.375f;
                break;

            case 'b':
                coord[0] = 0.125f;
                coord[1] = 0.375f;
                break;

            case 'c':
                coord[0] = 0.1875f;
                coord[1] = 0.375f;
                break;

            case 'd':
                coord[0] = 0.25f;
                coord[1] = 0.375f;
                break;

            case 'e':
                coord[0] = 0.3125f;
                coord[1] = 0.375f;
                break;

            case 'f':
                coord[0] = 0.375f;
                coord[1] = 0.375f;
                break;

            case 'g':
                coord[0] = 0.4375f;
                coord[1] = 0.375f;
                break;

            case 'h':
                coord[0] = 0.5f;
                coord[1] = 0.375f;
                break;

            case 'i':
                coord[0] = 0.5625f;
                coord[1] = 0.375f;
                break;

            case 'j':
                coord[0] = 0.625f;
                coord[1] = 0.375f;
                break;

            case 'k':
                coord[0] = 0.6875f;
                coord[1] = 0.375f;
                break;

            case 'l':
                coord[0] = 0.75f;
                coord[1] = 0.375f;
                break;

            case 'm':
                coord[0] = 0.8125f;
                coord[1] = 0.375f;
                break;

            case 'n':
                coord[0] = 0.875f;
                coord[1] = 0.375f;
                break;

            case 'o':
                coord[0] = 0.9375f;
                coord[1] = 0.375f;
                break;

            case 'p':
                coord[0] = 0.0f;
                coord[1] = 0.4375f;
                break;

            case 'q':
                coord[0] = 0.0625f;
                coord[1] = 0.4375f;
                break;

            case 'r':
                coord[0] = 0.125f;
                coord[1] = 0.4375f;
                break;

            case 's':
                coord[0] = 0.1875f;
                coord[1] = 0.4375f;
                break;

            case 't':
                coord[0] = 0.25f;
                coord[1] = 0.4375f;
                break;

            case 'u':
                coord[0] = 0.3125f;
                coord[1] = 0.4375f;
                break;

            case 'v':
                coord[0] = 0.375f;
                coord[1] = 0.4375f;
                break;

            case 'w':
                coord[0] = 0.4375f;
                coord[1] = 0.4375f;
                break;

            case 'x':
                coord[0] = 0.5f;
                coord[1] = 0.4375f;
                break;

            case 'y':
                coord[0] = 0.5625f;
                coord[1] = 0.4375f;
                break;

            case 'z':
                coord[0] = 0.625f;
                coord[1] = 0.4375f;
                break;

            ///// Upper case.
            case 'A':
                coord[0] = 0.0625f;
                coord[1] = 0.25f;
                break;

            case 'B':
                coord[0] = 0.125f;
                coord[1] = 0.25f;
                break;

            case 'C':
                coord[0] = 0.1875f;
                coord[1] = 0.25f;
                break;

            case 'D':
                coord[0] = 0.25f;
                coord[1] = 0.25f;
                break;

            case 'E':
                coord[0] = 0.3125f;
                coord[1] = 0.25f;
                break;

            case 'F':
                coord[0] = 0.375f;
                coord[1] = 0.25f;
                break;

            case 'G':
                coord[0] = 0.4375f;
                coord[1] = 0.25f;
                break;

            case 'H':
                coord[0] = 0.5f;
                coord[1] = 0.25f;
                break;

            case 'I':
                coord[0] = 0.5625f;
                coord[1] = 0.25f;
                break;

            case 'J':
                coord[0] = 0.625f;
                coord[1] = 0.25f;
                break;

            case 'K':
                coord[0] = 0.6875f;
                coord[1] = 0.25f;
                break;

            case 'L':
                coord[0] = 0.75f;
                coord[1] = 0.25f;
                break;

            case 'M':
                coord[0] = 0.8125f;
                coord[1] = 0.25f;
                break;

            case 'N':
                coord[0] = 0.875f;
                coord[1] = 0.25f;
                break;

            case 'O':
                coord[0] = 0.9375f;
                coord[1] = 0.25f;
                break;

            case 'P':
                coord[0] = 0.0f;
                coord[1] = 0.3125f;
                break;

            case 'Q':
                coord[0] = 0.0625f;
                coord[1] = 0.3125f;
                break;

            case 'R':
                coord[0] = 0.125f;
                coord[1] = 0.3125f;
                break;

            case 'S':
                coord[0] = 0.1875f;
                coord[1] = 0.3125f;
                break;

            case 'T':
                coord[0] = 0.25f;
                coord[1] = 0.3125f;
                break;

            case 'U':
                coord[0] = 0.3125f;
                coord[1] = 0.3125f;
                break;

            case 'V':
                coord[0] = 0.375f;
                coord[1] = 0.3125f;
                break;

            case 'W':
                coord[0] = 0.4375f;
                coord[1] = 0.3125f;
                break;

            case 'X':
                coord[0] = 0.5f;
                coord[1] = 0.3125f;
                break;

            case 'Y':
                coord[0] = 0.5625f;
                coord[1] = 0.3125f;
                break;

            case 'Z':
                coord[0] = 0.625f;
                coord[1] = 0.3125f;
                break;

            ///// Numbers.
            case '0':
                coord[0] = 0.0f;
                coord[1] = 0.1875f;
                break;

            case '1':
                coord[0] = 0.0625f;
                coord[1] = 0.1875f;
                break;

            case '2':
                coord[0] = 0.125f;
                coord[1] = 0.1875f;
                break;

            case '3':
                coord[0] = 0.1875f;
                coord[1] = 0.1875f;
                break;

            case '4':
                coord[0] = 0.25f;
                coord[1] = 0.1875f;
                break;

            case '5':
                coord[0] = 0.3125f;
                coord[1] = 0.1875f;
                break;

            case '6':
                coord[0] = 0.375f;
                coord[1] = 0.1875f;
                break;

            case '7':
                coord[0] = 0.4375f;
                coord[1] = 0.1875f;
                break;

            case '8':
                coord[0] = 0.5f;
                coord[1] = 0.1875f;
                break;

            case '9':
                coord[0] = 0.5625f;
                coord[1] = 0.1875f;
                break;

            ///// Misc.

            case '>':
                coord[0] = 0.875f;
                coord[1] = 0.1875f;
                break;

            case '<':
                coord[0] = 0.75f;
                coord[1] = 0.1875f;
                break;

            case ':':
                coord[0] = 0.6250f;
                coord[1] = 0.1875f;
                break;

            case '&':
                coord[0] = 0.3750f;
                coord[1] = 0.125f;
                break;

            case '.':
                coord[0] = 0.875f;
                coord[1] = 0.1250f;
                break;

            case '!':
                coord[0] = 0.0625f;
                coord[1] = 0.125f;
                break;

            case '?':
                coord[0] = 0.9375f;
                coord[1] = 0.1875f;
                break;

            case '£' :
                coord[0] = 0.0f;
                coord[1] = 0.5625f;
                break;

            case '$' :
                coord[0] = 0.0f;
                coord[1] = 0.625f;
                break;

            case'-':
                coord[0] = 0.375f;
                coord[1] = 0.5625f;
                break;

            case',':
                coord[0] = 0.75f;
                coord[1] = 0.125f;
                break;

            case'\'':
                coord[0] = 0.4375f;
                coord[1] = 0.125f;
                break;
            case'_':
                coord[0] = 0.9375f;
                coord[1] = 0.3125f;
                break;
        }

        return coord;
    }
}
