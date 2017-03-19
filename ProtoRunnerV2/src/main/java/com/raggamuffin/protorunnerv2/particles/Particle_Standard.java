package com.raggamuffin.protorunnerv2.particles;

// Author: Sinclair Ross
// Date:   15/01/2017

public class Particle_Standard extends Particle
{
    public Particle_Standard()
    {
        super(0.1, 0.2, 0.7);
    }

    @Override
    protected void AdditionalBehaviour(double deltaTime)
    {}

    @Override
    public void OnInvalidation()
    {}
}
