#include <system_error>
#include <cstdlib>
#include <jni.h>
#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <cmath>
#include <list>
#include <map>
#include <set>
#include <unordered_map> 
#include <unordered_set>
#include <fstream>
#include <memory>
#include <thread>
#include <chrono>
#include <exception>
#include <android/log.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

#include <fcntl.h>
#include <cstring>
#include <unistd.h>
#include <cstdlib>
#include <cstdio>

const char* XP_STRING_CONSTANT = "/system/bin/";

extern "C" JNIEXPORT jstring JNICALL
Java_app_prop_Prop_XullgetNullString(JNIEnv *env, jobject) {
    return env->NewStringUTF(XP_STRING_CONSTANT);
}

const int XP_CONSTANT = 2000;

extern "C" JNIEXPORT jint JNICALL
Java_app_prop_Prop_XullgetNullConstant(JNIEnv *env, jobject) {
    return XP_CONSTANT;
}

const unsigned long long MAIN_KEY_CONSTANT = 8880787807078080888;

extern "C" JNIEXPORT jlong JNICALL
Java_app_MainActivity_MainKeyConstant(JNIEnv *env, jobject) {
    return MAIN_KEY_CONSTANT;
}

const char* MAIN_NAME_STRING_CONSTANT = "XiaoKitty";

extern "C" JNIEXPORT jstring JNICALL
Java_app_MainActivity_MainNameString(JNIEnv *env, jobject) {
    return env->NewStringUTF(MAIN_NAME_STRING_CONSTANT);
}