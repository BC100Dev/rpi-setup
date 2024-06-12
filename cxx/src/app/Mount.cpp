#include <Application/Mount.h>
#include <Application/commons/ExceptionHandle.h>

#include <sys/mount.h>
#include <blkid/blkid.h>

JNIEXPORT void JNICALL
Java_net_bc100dev_rpisetup_lib_Mount_nMount(JNIEnv *env, jobject, jstring src, jstring target, jstring opts) {
    const char *srcStr = env->GetStringUTFChars(src, JNI_FALSE);
    const char *targetStr = env->GetStringUTFChars(target, JNI_FALSE);
    const char *optsStr = env->GetStringUTFChars(opts, JNI_FALSE);

    const char *fsTypeStr = blkid_get_tag_value(nullptr, "TYPE", srcStr);
    if (!fsTypeStr) {
        env->ReleaseStringUTFChars(src, srcStr);
        env->ReleaseStringUTFChars(target, targetStr);
        env->ReleaseStringUTFChars(opts, optsStr);

        throwJavaAppIOException(env, "Unable to detect filesystem type");
        return;
    }

    int mFlags = 0;
    if (mount(srcStr, targetStr, fsTypeStr, mFlags, optsStr) != 0) {
        env->ReleaseStringUTFChars(src, srcStr);
        env->ReleaseStringUTFChars(target, targetStr);
        env->ReleaseStringUTFChars(opts, optsStr);

        throwJavaAppIOException(env, "Mount failed");
        return;
    }

    env->ReleaseStringUTFChars(src, srcStr);
    env->ReleaseStringUTFChars(target, targetStr);
    env->ReleaseStringUTFChars(opts, optsStr);
}

JNIEXPORT void JNICALL Java_net_bc100dev_rpisetup_lib_Mount_nUnmount(JNIEnv *env, jobject, jstring src) {
    const char *srcStr = env->GetStringUTFChars(src, JNI_FALSE);

    if (umount(srcStr) != 0) {
        env->ReleaseStringUTFChars(src, srcStr);
        throwJavaAppIOException(env, "Unmount failed");
    }

    env->ReleaseStringUTFChars(src, srcStr);
}