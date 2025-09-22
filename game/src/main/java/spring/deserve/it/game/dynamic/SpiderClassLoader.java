package spring.deserve.it.game.dynamic;

import lombok.SneakyThrows;
import org.objectweb.asm.util.TraceClassVisitor;
import org.springframework.asm.*;
import org.springframework.stereotype.Service;
import spring.deserve.it.api.Spider;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SpiderClassLoader extends ClassLoader {
    int counter = 0;

    public Class<?> registerClassByBytes(byte[] b) {
        ClassReader classReader              = new ClassReader(b);
        ClassWriter classWriter              = new ClassWriter(classReader, 0);
        String      classNameMnemonic        = "org/dynamic/DynamicClass" + counter;
        String      superSpiderClassMnemonic = "spring/deserve/it/game/AbstractSpider";

        Set<String> methodsOfOriginalSpiderInterface = Arrays.stream(Spider.class.getMethods())
                                                             .map(Method::getName)
                                                             .collect(Collectors.toSet());

        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM9, classWriter) {
            @Override
            public MethodVisitor visitMethod(
                    int access,
                    String name,
                    String descriptor,
                    String signature,
                    String[] exceptions
            ) {

                if (name.equals("<init>") && signature == null) {
                    MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
                    return new MethodVisitor(Opcodes.ASM9) {

                        @Override
                        public void visitCode() {
                            //replace original init
                            //generate bytecode for init methods with call new supers
                            Label l0 = new Label();
                            Label l1 = new Label();
                            methodVisitor.visitCode();
                            methodVisitor.visitLabel(l0);
                            methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
                            methodVisitor.visitMethodInsn(
                                    Opcodes.INVOKESPECIAL,
                                    superSpiderClassMnemonic,
                                    "<init>",
                                    "()V",
                                    false
                            );
                            methodVisitor.visitInsn(Opcodes.RETURN);

                            methodVisitor.visitLabel(l1);
                            methodVisitor.visitLocalVariable(
                                    "this",
                                    "L" + classNameMnemonic + ";",
                                    null,
                                    l0,
                                    l1,
                                    0
                            );
                            methodVisitor.visitMaxs(1, 1);
                        }
                    };

                }
                if (!methodsOfOriginalSpiderInterface.contains(name) && (access & org.objectweb.asm.Opcodes.ACC_PUBLIC) != 0) {
                    //remove unknown methodsOfOriginalSpiderInterface
                    return new MethodVisitor(Opcodes.ASM9) {
                    };
                }
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }


            @Override
            public void visit(
                    int version,
                    int access,
                    String name,
                    String signature,
                    String superName,
                    String[] interfaces
            ) {
                super.visit(
                        Opcodes.V1_8,
                        access,
                        classNameMnemonic,
                        signature,
                        superSpiderClassMnemonic,
                        null

                );
            }
        };

        classReader.accept(classVisitor, 0);

        byte[] modifiedClass = classWriter.toByteArray();

//        System.out.println("====Modified class start======================================");
//        traceByteCode(modifiedClass);
//        System.out.println("====Modified class end========================================");


        Class<?> aClass = defineClass(classNameMnemonic.replace("/", "."), modifiedClass, 0, modifiedClass.length);
        counter++;
        return aClass;
    }


    /**
     * Если хотите посмотреть на классы которые получаются после генерации или как то их изменить и подебажить
     *
     * @param reader
     */
    private static void printFromReader(org.objectweb.asm.ClassReader reader) {
        PrintWriter       pw  = new PrintWriter(System.out);
        TraceClassVisitor tcv = new TraceClassVisitor(pw);
        reader.accept(tcv, 0);
    }

    @SneakyThrows
    private static void traceByteCode(String className) {
        org.objectweb.asm.ClassReader reader = new org.objectweb.asm.ClassReader(className);
        printFromReader(reader);
    }

    private static void traceByteCode(byte[] modifiedClass) {
        org.objectweb.asm.ClassReader reader = new org.objectweb.asm.ClassReader(modifiedClass);
        printFromReader(reader);
    }
}
