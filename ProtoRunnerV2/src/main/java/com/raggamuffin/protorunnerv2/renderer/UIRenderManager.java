package com.raggamuffin.protorunnerv2.renderer;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.ui.Font;
import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UILabel;
import com.raggamuffin.protorunnerv2.ui.UIProgressBar;
import com.raggamuffin.protorunnerv2.utils.Vector2;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class UIRenderManager 
{
	public final int NUM_TEXTURES = 2;
	
	private final float[] m_MVPMatrix = new float[16];	// MVP Matrix.
    private final float[] m_MMatrix   = new float[16];	// Model Matrix		// Projection and View Matrices in camera class.
	
	private int m_TextureHandles[];
	private int m_ResourceIDs[];

	private Context m_Context;
	private GLOrthoCamera m_Camera;
	
	private GLTextQuad m_TextQuad;
	private GLTexQuad m_TexQuad;
	
	public UIRenderManager(Context context, GLOrthoCamera Camera)
	{
		m_TextureHandles 	= new int[NUM_TEXTURES];
		m_ResourceIDs  		= new int[NUM_TEXTURES];
		m_ResourceIDs[0] 	= R.drawable.moire;
		m_ResourceIDs[1] 	= R.drawable.blank;
		
		m_Context 	 = context;
		m_Camera = Camera;
		
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
	
	public void DrawElement(final UIElement Element)
	{
		if(!Element.IsHidden())
		{
			switch(Element.GetType())
			{

				case Button:
				case Label:
					DrawText((UILabel)Element);
					break;
					
				case ProgressBar:
					UIProgressBar Bar = (UIProgressBar)Element;
					DrawProgressBar(Bar);
				
					break;
				case Undefined:
					break;
				default:
					break;
			}
		}
	}
	
	private void DrawText(final UILabel Element)
	{
        m_TextQuad.InitialiseModel();

		Vector2 Position = Element.GetPosition();
		Font TextFont 	 = Element.GetFont();
		String Text 	 = Element.GetText();
		int Length 		 = Text.length();
		float fontSize 	 = (float)TextFont.GetSize();

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_TextureHandles[0]);
		
		m_TextQuad.SetColour(TextFont.GetColour());

        float[] view = new float[16];
        Matrix.setIdentityM(view, 0);
        Matrix.multiplyMM(view, 0, m_Camera.m_ProjMatrix, 0, m_Camera.m_VMatrix, 0);
		
		for(int i = 0; i < Length; i++)
		{
			float x = (float)Position.I + fontSize * i;
			float y = (float)Position.J;

			m_TextQuad.SetOffset(GetTexCoord(Text.charAt(i)));
			m_TextQuad.draw(x, y, fontSize, view);
		}

        m_TextQuad.CleanModel();
	}
	
	private void DrawProgressBar(final UIProgressBar Element)
	{
		Vector2 underBarPosition = Element.GetUnderBarPosition();
        Vector2 underBarSize = Element.GetUnderBarScale();
		Vector2 Position = Element.GetPosition();
		Vector2 Size = Element.GetSize();
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, m_TextureHandles[1]);
		
		///// Draw UnderLayer
		m_TexQuad.SetColour(Element.GetUnderColour());	
		
		Matrix.setIdentityM(m_MMatrix, 0);

		Matrix.translateM(m_MMatrix, 0, (float)underBarPosition.I, (float)underBarPosition.J, 0.0f);
		Matrix.scaleM(m_MMatrix, 0, (float)underBarSize.I, (float)underBarSize.J, 1.0f);
		
		// Combine the rotation matrix with the projection and camera view
        Matrix.multiplyMM(m_MVPMatrix, 0, m_Camera.m_VMatrix, 0, m_MMatrix, 0);
        Matrix.multiplyMM(m_MVPMatrix, 0, m_Camera.m_ProjMatrix, 0, m_MVPMatrix, 0);
        
        m_TexQuad.draw(m_MVPMatrix);
		
		///// Draw OverLayer
        m_TexQuad.SetColour(Element.GetColour());	
		
		Matrix.setIdentityM(m_MMatrix, 0);
		
		Matrix.translateM(m_MMatrix, 0, (float)Position.I, (float)Position.J, 0.0f);
		Matrix.scaleM(m_MMatrix, 0, (float)Size.I, (float)Size.J, 1.0f);
		
		// Combine the rotation matrix with the projection and camera view
        Matrix.multiplyMM(m_MVPMatrix, 0, m_Camera.m_VMatrix, 0, m_MMatrix, 0);
        Matrix.multiplyMM(m_MVPMatrix, 0, m_Camera.m_ProjMatrix, 0, m_MVPMatrix, 0);
        
        m_TexQuad.draw(m_MVPMatrix);
	}
	
	private float[] GetTexCoord(char c)
	{
		float Coord[] = new float [2];
		
		switch(c)
		{
			case ' ':
				Coord[0] = 0.0f;
				Coord[1] = 0.0f;
				break;
			
			///// Lower case.	
			case 'a':
				Coord[0] = 0.0625f;
				Coord[1] = 0.375f;
				break;
				
			case 'b':
				Coord[0] = 0.125f;
				Coord[1] = 0.375f;
				break;
				
			case 'c':
				Coord[0] = 0.1875f;
				Coord[1] = 0.375f;
				break;
				
			case 'd':
				Coord[0] = 0.25f;
				Coord[1] = 0.375f;
				break;
				
			case 'e':
				Coord[0] = 0.3125f;
				Coord[1] = 0.375f;
				break;
				
			case 'f':
				Coord[0] = 0.375f;
				Coord[1] = 0.375f;
				break;
				
			case 'g':
				Coord[0] = 0.4375f;
				Coord[1] = 0.375f;
				break;
				
			case 'h':
				Coord[0] = 0.5f;
				Coord[1] = 0.375f;
				break;
				
			case 'i':
				Coord[0] = 0.5625f;
				Coord[1] = 0.375f;
				break;
				
			case 'j':
				Coord[0] = 0.625f;
				Coord[1] = 0.375f;
				break;
				
			case 'k':
				Coord[0] = 0.6875f;
				Coord[1] = 0.375f;
				break;
				
			case 'l':
				Coord[0] = 0.75f;
				Coord[1] = 0.375f;
				break;
				
			case 'm':
				Coord[0] = 0.8125f;
				Coord[1] = 0.375f;
				break;
				
			case 'n':
				Coord[0] = 0.875f;
				Coord[1] = 0.375f;
				break;
				
			case 'o':
				Coord[0] = 0.9375f;
				Coord[1] = 0.375f;
				break;
				
			case 'p':
				Coord[0] = 0.0f;
				Coord[1] = 0.4375f;
				break;
				
			case 'q':
				Coord[0] = 0.0625f;
				Coord[1] = 0.4375f;
				break;
				
			case 'r':
				Coord[0] = 0.125f;
				Coord[1] = 0.4375f;
				break;
				
			case 's':
				Coord[0] = 0.1875f;
				Coord[1] = 0.4375f;
				break;
				
			case 't':
				Coord[0] = 0.25f;
				Coord[1] = 0.4375f;
				break;
				
			case 'u':
				Coord[0] = 0.3125f;
				Coord[1] = 0.4375f;
				break;
				
			case 'v':
				Coord[0] = 0.375f;
				Coord[1] = 0.4375f;
				break;
				
			case 'w':
				Coord[0] = 0.4375f;
				Coord[1] = 0.4375f;
				break;
				
			case 'x':
				Coord[0] = 0.5f;
				Coord[1] = 0.4375f;
				break;
				
			case 'y':
				Coord[0] = 0.5625f;
				Coord[1] = 0.4375f;
				break;
				
			case 'z':
				Coord[0] = 0.625f;
				Coord[1] = 0.4375f;
				break;
				
			///// Upper case.
			case 'A':
				Coord[0] = 0.0625f;
				Coord[1] = 0.25f;
				break;
				
			case 'B':
				Coord[0] = 0.125f;
				Coord[1] = 0.25f;
				break;
				
			case 'C':
				Coord[0] = 0.1875f;
				Coord[1] = 0.25f;
				break;
				
			case 'D':
				Coord[0] = 0.25f;
				Coord[1] = 0.25f;
				break;
				
			case 'E':
				Coord[0] = 0.3125f;
				Coord[1] = 0.25f;
				break;
				
			case 'F':
				Coord[0] = 0.375f;
				Coord[1] = 0.25f;
				break;
				
			case 'G':
				Coord[0] = 0.4375f;
				Coord[1] = 0.25f;
				break;
				
			case 'H':
				Coord[0] = 0.5f;
				Coord[1] = 0.25f;
				break;
				
			case 'I':
				Coord[0] = 0.5625f;
				Coord[1] = 0.25f;
				break;
				
			case 'J':
				Coord[0] = 0.625f;
				Coord[1] = 0.25f;
				break;
				
			case 'K':
				Coord[0] = 0.6875f;
				Coord[1] = 0.25f;
				break;
				
			case 'L':
				Coord[0] = 0.75f;
				Coord[1] = 0.25f;
				break;
				
			case 'M':
				Coord[0] = 0.8125f;
				Coord[1] = 0.25f;
				break;
				
			case 'N':
				Coord[0] = 0.875f;
				Coord[1] = 0.25f;
				break;
				
			case 'O':
				Coord[0] = 0.9375f;
				Coord[1] = 0.25f;
				break;
				
			case 'P':
				Coord[0] = 0.0f;
				Coord[1] = 0.3125f;
				break;
				
			case 'Q':
				Coord[0] = 0.0625f;
				Coord[1] = 0.3125f;
				break;
				
			case 'R':
				Coord[0] = 0.125f;
				Coord[1] = 0.3125f;
				break;
				
			case 'S':
				Coord[0] = 0.1875f;
				Coord[1] = 0.3125f;
				break;
				
			case 'T':
				Coord[0] = 0.25f;
				Coord[1] = 0.3125f;
				break;
				
			case 'U':
				Coord[0] = 0.3125f;
				Coord[1] = 0.3125f;
				break;
				
			case 'V':
				Coord[0] = 0.375f;
				Coord[1] = 0.3125f;
				break;
				
			case 'W':
				Coord[0] = 0.4375f;
				Coord[1] = 0.3125f;
				break;
				
			case 'X':
				Coord[0] = 0.5f;
				Coord[1] = 0.3125f;
				break;
				
			case 'Y':
				Coord[0] = 0.5625f;
				Coord[1] = 0.3125f;
				break;
				
			case 'Z':
				Coord[0] = 0.625f;
				Coord[1] = 0.3125f;
				break;
				
			///// Numbers.
			case '0':
				Coord[0] = 0.0f;
				Coord[1] = 0.1875f;
				break;
				
			case '1':
				Coord[0] = 0.0625f;
				Coord[1] = 0.1875f;
				break;
				
			case '2':
				Coord[0] = 0.125f;
				Coord[1] = 0.1875f;
				break;
				
			case '3':
				Coord[0] = 0.1875f;
				Coord[1] = 0.1875f;
				break;
				
			case '4':
				Coord[0] = 0.25f;
				Coord[1] = 0.1875f;
				break;
				
			case '5':
				Coord[0] = 0.3125f;
				Coord[1] = 0.1875f;
				break;
				
			case '6':
				Coord[0] = 0.375f;
				Coord[1] = 0.1875f;
				break;
				
			case '7':
				Coord[0] = 0.4375f;
				Coord[1] = 0.1875f;
				break;
				
			case '8':
				Coord[0] = 0.5f;
				Coord[1] = 0.1875f;
				break;
				
			case '9':
				Coord[0] = 0.5625f;
				Coord[1] = 0.1875f;
				break;
				
			///// Misc.
			
			case '>':
				Coord[0] = 0.875f;
				Coord[1] = 0.1875f;
				break;
			
			case '<':
				Coord[0] = 0.75f;
				Coord[1] = 0.1875f;
				break;
				
			case ':':
				Coord[0] = 0.6250f;
				Coord[1] = 0.1875f;
				break;
				
			case '&':
				Coord[0] = 0.3750f;
				Coord[1] = 0.125f;
				break;
				
			case '.':
				Coord[0] = 0.875f;
				Coord[1] = 0.1250f;
				break;

            case '!':
                Coord[0] = 0.0625f;
                Coord[1] = 0.125f;
                break;

            case '?':
                Coord[0] = 0.9375f;
                Coord[1] = 0.1875f;
                break;

			case '£' :
				Coord[0] = 0.0f;
				Coord[1] = 0.5625f;
				break;
				
			case '$' :
				Coord[0] = 0.0f;
				Coord[1] = 0.625f;
				break;

            case'-':
                Coord[0] = 0.375f;
                Coord[1] = 0.5625f;
                break;

            case',':
                Coord[0] = 0.75f;
                Coord[1] = 0.125f;
                break;

            case'\'':
                Coord[0] = 0.4375f;
                Coord[1] = 0.125f;
                break;
		}
		
		return Coord;
	}
	
}

