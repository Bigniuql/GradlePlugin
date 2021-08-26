package com.nql.plugin


import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class ClickLogVisitor extends MethodVisitor {


    ClickLogVisitor(int i, MethodVisitor methodVisitor) {
        super(i, methodVisitor)
    }

    // 将插件中的相关代码复制进来,并导入相关常量
    @Override
    void visitCode() {
        super.visitCode()
        mv.visitLdcInsn("MyTransform")
        mv.visitLdcInsn("OnResume")
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)I", false)
        mv.visitInsn(Opcodes.POP)
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitTypeInsn(Opcodes.CHECKCAST, "android/content/Context");
        mv.visitLdcInsn("Start!");
        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/CharSequence");
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/widget/Toast", "makeText", "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;", false);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/widget/Toast", "show", "()V", false);
    }
}