package com.raggamuffin.protorunnerv2.renderer;

public class GLParticleGroup_Standard extends GLParticleGroup
{
    public GLParticleGroup_Standard()
    {
        super(Shaders.vertexShader_POINT, Shaders.fragmentShader_POINT);
    }
}