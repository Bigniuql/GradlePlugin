package com.nql.plugin


import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

public class ClickClassVisitor extends ClassVisitor {


    private static final CLICK_METHOD = "onClick"
    private static final RESUME_METHOD = "onResume"

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
        if (name.startsWith(CLICK_METHOD)) {
            return new ClickToastVisitor(api, methodVisitor)
        } else if (name.startsWith(RESUME_METHOD)) {// 找到OnResume方法
            return new ClickLogVisitor(api, methodVisitor)
        } else {
            return methodVisitor
        }
    }
}