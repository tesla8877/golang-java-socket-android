#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_redlion_go_1java_1socket_1communication_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}