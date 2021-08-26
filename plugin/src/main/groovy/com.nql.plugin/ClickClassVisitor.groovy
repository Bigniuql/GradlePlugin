package com.nql.plugin

import com.android.ddmlib.Log
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

public class ClickClassVisitor extends ClassVisitor {


    private static final METHOD = "onResume"

    ClickClassVisitor(int api) {
        super(api)
    }

    ClickClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor)
    }

    // 方法代码插装
    @Override
    MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        println "method:$name"
        //找到onClick方法
        if (name.startsWith(METHOD)) {
            return new ClickLogVisitor(api, methodVisitor)
        }
        return methodVisitor
    }
}