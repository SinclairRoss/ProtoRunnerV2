package com.raggamuffin.protorunnerv2.renderer;

public class GLParticleGroup_Multiplier extends GLParticleGroup
{
    public GLParticleGroup_Multiplier()
    {
        super(Shaders.vertexShader_POINTMULTIPLIER, Shaders.fragmentShader_POINTMULTIPLIER);
    }
}