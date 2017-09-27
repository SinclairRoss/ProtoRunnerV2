package com.raggamuffin.protorunnerv2.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES31;
import android.opengl.GLUtils;

public class TextureLoader 
{
	private static int NumTextures = 0;
	
	public static void LoadTextures(final Context context, int[] resourceIds, int[] textureHandles)
	{
		int numNewTextures = resourceIds.length;
	    GLES31.glGenTextures(numNewTextures, textureHandles, 0);
	    
	    for(int n = 0; n < numNewTextures; n++)
	    {
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inScaled = false;   // No pre-scaling
	 
	        // Read in the resource
	        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceIds[n], options);

		    GLES31.glActiveTexture(GLES31.GL_TEXTURE0 + NumTextures);
	        // Bind to the texture in OpenGL
	        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureHandles[n]);
	 
	        // Set filtering
	        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MIN_FILTER, GLES31.GL_NEAREST);
	        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_MAG_FILTER, GLES31.GL_NEAREST);
	        
	        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_S, GLES31.GL_REPEAT);
	        GLES31.glTexParameteri(GLES31.GL_TEXTURE_2D, GLES31.GL_TEXTURE_WRAP_T, GLES31.GL_REPEAT);
	 
	        // Load the bitmap into the bound texture.
	        GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, bitmap, 0);

	        // Recycle the bitmap, since its data has been loaded into OpenGL.
	        bitmap.recycle();
		    
		    if (textureHandles[n] == 0)
			{
				throw new RuntimeException("Error loading texture.");
			}
		    
		    ++NumTextures;
	    }
	}
	
	public static int GetNumTexures()
	{
		return NumTextures;
	}
}
