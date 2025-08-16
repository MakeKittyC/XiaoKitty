cmake_minimum_required(VERSION 3.22.1)

find_library(log-lib log)
find_library(android-lib android)

project(YourProjectName)

set(CMAKE_C_STANDARD 26)
set(CMAKE_CXX_STANDARD_REQUIRED True)

set(SOURCE_DIR src/main/cpp)

add_library(NonNull SHARED main.cpp null.cpp)

set_target_properties(NonNull PROPERTIES OUTPUT_NAME "NonNull")

target_link_libraries(NonNull
    ${log}
    ${android}
)

target_link_options(NonNull PRIVATE "-Wl,-z,max-page-size=65536")

# 4096 16384 65536