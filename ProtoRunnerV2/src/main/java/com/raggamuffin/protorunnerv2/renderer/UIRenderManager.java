package com.raggamuffin.protorunnerv2.renderer;

import android.content.Context;

import com.raggamuffin.protorunnerv2.ui.UIElement;
import com.raggamuffin.protorunnerv2.ui.UIElementType;
import com.raggamuffin.protorunnerv2.ui.UIElement_Label;
import com.raggamuffin.protorunnerv2.ui.UIElement_Triangle;

public class UIRenderManager
{
    private Context m_Context;

    private GLModel_UIBlock m_TexQuad;
    private GLModel_UIBlockRight m_TexQuad_Right;
    private GLModel_UIBlockLeft m_TexQuad_Left;

    private TextRenderer m_TextRenderer;
    private GLModel_UITriangle m_TouchMarker;

    public UIRenderManager(Context context)
    {
        m_Context = context;
        m_TextRenderer = new TextRenderer();
    }

    public void LoadAssets()
    {
        LoadModels();

        m_TextRenderer.LoadAssets(m_Context);
    }

    private void LoadModels()
    {
        m_TexQuad = new GLModel_UIBlock();
        m_TexQuad_Right = new GLModel_UIBlockRight();
        m_TexQuad_Left = new GLModel_UIBlockLeft();

        m_TouchMarker = new GLModel_UITriangle();
    }

    public void InitialiseModel(UIElementType type, float[] viewMatrix)
    {
        switch (type)
        {
            case Block_Centered:
                m_TexQuad.InitialiseModel(viewMatrix, null);
                break;
            case Block_Right:
                m_TexQuad_Right.InitialiseModel(viewMatrix, null);
                break;
            case Block_Left:
                m_TexQuad_Left.InitialiseModel(viewMatrix, null);
                break;
            case Label:
                m_TextRenderer.PrepareForRender(viewMatrix);
                break;
            case Triangle:
                m_TouchMarker.InitialiseModel(viewMatrix);
                break;
        }
    }

    public void DrawElement(final UIElement element)
    {
        if (!element.IsHidden())
        {
            switch (element.GetType())
            {
                case Label:
                    m_TextRenderer.DrawText((UIElement_Label) element);
                    break;
                case Block_Centered:
                    m_TexQuad.draw(element.GetPosition(), element.GetScale(), element.GetColour(), element.GetRotation());
                    break;
                case Block_Right:
                    m_TexQuad_Right.draw(element.GetPosition(), element.GetScale(), element.GetColour(), element.GetRotation());
                    break;
                case Block_Left:
                    m_TexQuad_Left.draw(element.GetPosition(), element.GetScale(), element.GetColour(), element.GetRotation());
                    break;
                case Triangle:
                    UIElement_Triangle element_triangle = (UIElement_Triangle) element;
                    m_TouchMarker.Draw(element_triangle.GetPosition(), element_triangle.GetScale(), element_triangle.GetRotation(), element_triangle.GetColour(), element_triangle.GetLineWidth());
                    break;
                default:
                    break;
            }
        }
    }

    public void CleanModel(UIElementType type)
    {
        switch (type)
        {
            case Block_Centered:
                m_TexQuad.CleanModel();
                break;
            case Block_Right:
                m_TexQuad_Right.CleanModel();
                break;
            case Block_Left:
                m_TexQuad_Left.CleanModel();
                break;
            case Label:
                m_TextRenderer.CleanUp();
                break;
            case Triangle:
                break;
        }
    }
}