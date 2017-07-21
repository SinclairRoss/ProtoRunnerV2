package com.raggamuffin.protorunnerv2.ui;

// Author: Sinclair Ross
// Date:   11/06/2017

public abstract class UITeacher
{
    public  void Update(double deltaTime)
    {
        UpdateTouchInputs();
        UpdateLogic(deltaTime);
        UpdateAesthetic(deltaTime);
    }

    protected abstract void UpdateLogic(double deltaTime);
    protected abstract void UpdateAesthetic(double deltaTime);
    protected abstract void UpdateTouchInputs();

    public abstract void Initialise();
    public abstract void CleanUp();

    public abstract boolean HasCompletedLesson();
}
