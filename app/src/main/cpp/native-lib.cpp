//
// Created by Irfan Amrullah on 1/1/21.
//

#include <jni.h>
#include <string>
#include <iostream>
#include <stdlib.h>
#include <time.h>

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_MuhammadIrfanAmrullah_Nikki_AddEditDiaryActivity_randEmotion( JNIEnv *env, jobject /* this */) {
    // Initialize String Array
    std::string emotion[3] = { "Sad", "Normal","Happy"};
    int RandIndex = rand() % 3;
    std::string ans = emotion[RandIndex];
    return env->NewStringUTF(ans.c_str());
}