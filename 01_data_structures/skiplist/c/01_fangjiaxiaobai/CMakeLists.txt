# cmake_minimum_required(VERSION <specify CMake version here>)
project(01_skiplist C)
cmake_minimum_required(VERSION 3.15)
set(CMAKE_C_STANDARD 11)

include_directories(.)

add_executable(01_skiplist main.c skiplist.c skiplist.h)
