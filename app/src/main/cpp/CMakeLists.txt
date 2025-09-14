cmake_minimum_required(VERSION 3.22.1)

find_library(log-lib log)
find_library(android-lib android)

project(YourProjectName)

set(CMAKE_C_STANDARD 26)
set(CMAKE_CXX_STANDARD_REQUIRED True)

set(SOURCE_DIR src/main/cpp)

add_library(xiaoKitty SHARED main.cpp null.cpp)

set_target_properties(xiaoKitty PROPERTIES OUTPUT_NAME "xiaoKitty")

target_link_libraries(xiaoKitty
    ${log}
    ${android}
)

target_link_options(xiaoKitty PRIVATE "-Wl,-z,max-page-size=65536")

# 4096 16384 65536