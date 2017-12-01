//
// Created by S.Ross on 26/08/2017.
//
#ifndef C_RENDERER__H
#define C_RENDERER__H

#include <memory>
#include <jni.h>

#include "OpenGL/GLRenderer.h"

namespace CRenderer
{
    static std::unique_ptr<GLRenderer> Renderer_OpenGL;

    extern "C"
    {
        JNIEXPORT void JNICALL Java_com_raggamuffin_protorunnerv2_renderer_GLRenderer_OnSurfaceCreated_1Native(JNIEnv* env, jobject callingObj, jfloatArray jVertices, jintArray jVerticesPerModel, jintArray jTexturePixels, jintArray jPixelsPerTexture, jint jNumTextures, jint jScreenWidth, jint jScreenHeight)
        {
            float* vertices = env->GetFloatArrayElements(jVertices, 0);
            int* verticesPerModel = env->GetIntArrayElements(jVerticesPerModel, 0);

            int* textures = env->GetIntArrayElements(jTexturePixels, 0);
            int* pixelsPerTexture = env->GetIntArrayElements(jPixelsPerTexture, 0);

            Renderer_OpenGL = std::make_unique<GLRenderer>(vertices, verticesPerModel, textures, pixelsPerTexture, jNumTextures, jScreenWidth, jScreenHeight);
            Renderer_OpenGL->OnSurfaceCreated();

            env->ReleaseFloatArrayElements(jVertices, vertices, 0);
            env->ReleaseIntArrayElements(jVerticesPerModel, verticesPerModel, 0);

            env->ReleaseIntArrayElements(jTexturePixels, textures, 0);
            env->ReleaseIntArrayElements(jPixelsPerTexture, pixelsPerTexture, 0);
        }

        JNIEXPORT void JNICALL Java_com_raggamuffin_protorunnerv2_renderer_GLRenderer_StartRender_1Native()
        {
            Renderer_OpenGL->StartRender();
        }

        JNIEXPORT void JNICALL Java_com_raggamuffin_protorunnerv2_renderer_GLRenderer_FinaliseRender_1Native()
        {
            Renderer_OpenGL->FinaliseRender();
        }

        JNIEXPORT void JNICALL Java_com_raggamuffin_protorunnerv2_renderer_GLRenderer_SetCamera_1Native(JNIEnv* env, jobject callingObj, jfloatArray jPosition, jfloatArray jLookAt, jfloatArray jUp)
        {
            float* position = env->GetFloatArrayElements(jPosition, 0);
            float* lookAt = env->GetFloatArrayElements(jLookAt, 0);
            float* up = env->GetFloatArrayElements(jUp, 0);

            Renderer_OpenGL->SetCameraTranforms(position, lookAt, up);

            env->ReleaseFloatArrayElements(jPosition, position, 0);
            env->ReleaseFloatArrayElements(jLookAt, lookAt, 0);
            env->ReleaseFloatArrayElements(jUp, up, 0);
        }

        JNIEXPORT void JNICALL Java_com_raggamuffin_protorunnerv2_renderer_GLRenderer_DrawVehicles_1Native(JNIEnv* env, jobject callingObj, jfloatArray jTransforms, jfloatArray jColours, jfloatArray  jInnerIntensity, jint jModelIndex, jint jNumInstances)
        {
            float* transforms = env->GetFloatArrayElements(jTransforms, 0);
            float* colours = env->GetFloatArrayElements(jColours, 0);
            float* innerIntenisty = env->GetFloatArrayElements(jInnerIntensity, 0);

            Renderer_OpenGL->DrawVehicles(transforms, colours, innerIntenisty, jModelIndex, jNumInstances);

            env->ReleaseFloatArrayElements(jTransforms, transforms, 0);
            env->ReleaseFloatArrayElements(jColours, colours, 0);
            env->ReleaseFloatArrayElements(jInnerIntensity, innerIntenisty, 0);
        }

        JNIEXPORT void JNICALL Java_com_raggamuffin_protorunnerv2_renderer_GLRenderer_DrawGenericObject_1Native(JNIEnv* env, jobject callingObj, jfloatArray jTransforms, jfloatArray jColours, jint jModelIndex, jint jNumInstances)
        {
            float* transforms = env->GetFloatArrayElements(jTransforms, 0);
            float* colours = env->GetFloatArrayElements(jColours, 0);

            Renderer_OpenGL->DrawObjects(transforms, colours, jModelIndex, jNumInstances);

            env->ReleaseFloatArrayElements(jTransforms, transforms, 0);
            env->ReleaseFloatArrayElements(jColours, colours, 0);
        }

        JNIEXPORT void JNICALL Java_com_raggamuffin_protorunnerv2_renderer_GLRenderer_DrawParticles_1Native(JNIEnv* env, jobject callingObj, jfloatArray jTransforms, jfloatArray jColours, jint jNumInstances)
        {
            float* transforms = env->GetFloatArrayElements(jTransforms, 0);
            float* colours = env->GetFloatArrayElements(jColours, 0);

            Renderer_OpenGL->DrawParticles(transforms, colours, jNumInstances);

            env->ReleaseFloatArrayElements(jTransforms, transforms, 0);
            env->ReleaseFloatArrayElements(jColours, colours, 0);
        }

        JNIEXPORT void JNICALL Java_com_raggamuffin_protorunnerv2_renderer_GLRenderer_DrawUIText_1Native(JNIEnv* env, jobject callingObj, jfloatArray jPositions, jfloatArray jScales, jfloatArray jColours, jcharArray jText, jint jNumInstances)
        {
            float* positions = env->GetFloatArrayElements(jPositions, 0);
            float* scales = env->GetFloatArrayElements(jScales, 0);
            float* colours = env->GetFloatArrayElements(jColours, 0);

            jboolean isCopy;
            jchar* jTextPtr = env->GetCharArrayElements(jText, &isCopy);

            char text[jNumInstances];
            for(int i = 0; i < jNumInstances; ++i)
            {
                text[i] = (char)jTextPtr[i];
            }


            Renderer_OpenGL->DrawUI_Text(positions, scales, colours, text, jNumInstances);

            env->ReleaseFloatArrayElements(jPositions, positions, 0);
            env->ReleaseFloatArrayElements(jScales, scales, 0);
            env->ReleaseFloatArrayElements(jColours, colours, 0);
            env->ReleaseCharArrayElements(jText, jTextPtr, 0);
        }

        JNIEXPORT void JNICALL Java_com_raggamuffin_protorunnerv2_renderer_GLRenderer_DrawUIElements_1Native(JNIEnv* env, jobject callingObj, jfloatArray jPositions, jfloatArray jScales, jfloatArray jColours, jint jElementType, jint jNumInstances)
        {
            float* positions = env->GetFloatArrayElements(jPositions, 0);
            float* scales = env->GetFloatArrayElements(jScales, 0);
            float* colours = env->GetFloatArrayElements(jColours, 0);

            Renderer_OpenGL->DrawUI(positions, scales, colours, jElementType, jNumInstances);

            env->ReleaseFloatArrayElements(jPositions, positions, 0);
            env->ReleaseFloatArrayElements(jScales, scales, 0);
            env->ReleaseFloatArrayElements(jColours, colours, 0);
        }

        JNIEXPORT void JNICALL Java_com_raggamuffin_protorunnerv2_renderer_GLRenderer_OnSurfaceChanged_1Native(JNIEnv* env, jobject callingObj, jint jWidth, jint jHeight)
        {
            Renderer_OpenGL->OnSurfaceChanged(jWidth, jHeight);
        }
    }
};

#endif // C_RENDERER__H