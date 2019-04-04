package com.lxyx.hellplugin.dir;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by habbyge on 2019/4/5.
 * todo 目前仅用于测试用途
 */
public class HellBaseActivityGenerator extends ClassLoader {

    private HellBaseActivityGenerator() {
    }

    public static void create(String className) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cw.visit(Opcodes.ASM5, Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL, className, null, "android/app/Activity", null);

        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "onCreate", "(Landroid/os/Bundle;)V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0); // this指针
        mv.visitVarInsn(Opcodes.ALOAD, 1); // Bundle参数
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "android/app/Activity", "onCreate", "(Landroid/os/Bundle;)V", false);
        mv.visitEnd();

        mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "onResume", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0); // this指针
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "android/app/Activity", "onResume", "()V", false);
        mv.visitEnd();

        byte[] classBytes = cw.toByteArray();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("../helllib/src/main/java/com/lxyx/helllib/HellBaseActivity.class");
            fos.write(classBytes);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}
