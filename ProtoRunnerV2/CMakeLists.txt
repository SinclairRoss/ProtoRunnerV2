cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++14 -fno-rtti -fno-exceptions -Wall")

add_library( CRenderer-lib
             SHARED
             src/main/cpp/CRenderer.cpp
             src/main/cpp/OpenGL/GLRenderer.cpp
             src/main/cpp/OpenGL/Models/ModelContainer.cpp
             src/main/cpp/OpenGL/Models/Model_Vehicle.cpp
             src/main/cpp/OpenGL/Models/Model_SolidObject.cpp
             src/main/cpp/OpenGL/Models/Model_FloorGrid.cpp
             src/main/cpp/OpenGL/Particles/ParticleGroup_Standard.cpp
             src/main/cpp/OpenGL/Textures/TextureContainer.cpp
             src/main/cpp/OpenGL/Text/TextRenderer.cpp
             src/main/cpp/OpenGL/Text/Model_LetterBlock.cpp
             src/main/cpp/OpenGL/Shaders/Shader_Wireframe.h
             src/main/cpp/OpenGL/Shaders/Shader_Basic.h
             src/main/cpp/OpenGL/Shaders/Shader_Particles.h
             src/main/cpp/OpenGL/Shaders/Shader_FloorGrid.h
             src/main/cpp/OpenGL/Utils/Helpers.cpp
             src/main/cpp/OpenGL/Utils/ProceduralTextures.cpp)

include_directories(src/main/cpp/)

target_link_libraries(CRenderer-lib
                       android
                       log
                       EGL
                       GLESv3)