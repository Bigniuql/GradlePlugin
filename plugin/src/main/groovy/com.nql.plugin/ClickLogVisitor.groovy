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
        mv.visitLdcInsn("TAG")
        mv.visitLdcInsn("==========Start==========")
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "d", "(Ljava/lang/String;Ljava/lang/String;)I", false)
        mv.visitInsn(Opcodes.POP)
    }
}