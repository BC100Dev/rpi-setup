cmake_minimum_required(VERSION 3.28)
project(rpi-setup)

set(CMAKE_CXX_STANDARD 17)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fPIC")

include_directories("include/app")
include_directories("include/jni")
include_directories("include/jni/linux")

add_library(rpi-setup SHARED
        src/app/commons/ConsoleSize.cpp
        src/app/commons/ExceptionHandle.cpp
        src/app/commons/UserIO.cpp
        src/app/Mount.cpp)
