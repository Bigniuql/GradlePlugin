package com.nql.plugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

public class ClickToastVisitor extends org.objectweb.asm.MethodVisitor {

    ClickToastVisitor(int api) {
        super(api)
    }

    ClickToastVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor)
    }

    @Override
    void visitCode() {
        super.visitCode()
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitTypeInsn(Opcodes.CHECKCAST, "android/content/Context")
        mv.visitLdcInsn("You Click!")
        mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/CharSequence")
        mv.visitInsn(Opcodes.ICONST_1)
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/widget/Toast", "makeText", "(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;", false)
        mv.visitVarInsn(Opcodes.ASTORE, 1)
        mv.visitInsn(Opcodes.ICONST_0)
        mv.visitVarInsn(Opcodes.ISTORE, 2)
        mv.visitInsn(Opcodes.ICONST_0)
        mv.visitVarInsn(Opcodes.ISTORE, 3)
        mv.visitVarInsn(Opcodes.ALOAD, 1)
        mv.visitVarInsn(Opcodes.ASTORE, 4)
        mv.visitInsn(Opcodes.ICONST_0)
        mv.visitVarInsn(Opcodes.ISTORE, 5)
        mv.visitVarInsn(Opcodes.ALOAD, 4)
        mv.visitIntInsn(Opcodes.BIPUSH, 17)
        mv.visitInsn(Opcodes.ICONST_0)
        mv.visitInsn(Opcodes.ICONST_0)
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/widget/Toast", "setGravity", "(III)V", false)
        mv.visitInsn(Opcodes.NOP)
        mv.visitVarInsn(Opcodes.ALOAD, 1)
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "android/widget/Toast", "show", "()V", false)
    }
}