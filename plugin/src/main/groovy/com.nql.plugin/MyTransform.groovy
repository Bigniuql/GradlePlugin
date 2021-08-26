package com.nql.plugin

import com.android.SdkConstants
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

import java.util.jar.JarFile

public class MyTransform extends Transform {

    /**
     *
     * @param transformInvocation
     * @throws TransformException* @throws InterruptedException* @throws IOException
     */
    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        def outputProvider = transformInvocation.outputProvider
        println("=====isIncremental=======:  ${isIncremental()}")
        for (input in transformInvocation.inputs) {
            // jar chu li
            input.jarInputs.forEach { ji ->
                def jarInput = ji.file
                def jarOutput = outputProvider.getContentLocation(
                        ji.name,
                        ji.contentTypes,
                        ji.scopes,
                        Format.JAR
                )
                // zeng liang bian yi
                if (transformInvocation.isIncremental()) {
                    switch (ji.status) {
                        case (Status.NOTCHANGED):
                            break
                        case (Status.ADDED):
                        case (Status.CHANGED):
                            jarTransform(jarInput, jarOutput)
                            break
                        case (Status.REMOVED):
                            FileUtils.deleteIfExists(jarOutput)
                            break
                        default:
                            break
                    }
                } else {
                    jarTransform(jarInput, jarOutput)
                }
            }
            // directory chu li
            input.directoryInputs.forEach { di ->
                def dirInput = di.file
                def dirOutput = outputProvider.getContentLocation(
                        di.name,
                        di.contentTypes,
                        di.scopes,
                        Format.DIRECTORY
                )
                if (transformInvocation.isIncremental()) {
                    for (entry in di.changedFiles.entrySet()) {
                        def inputFile = entry.key
                        switch (entry.value) {
                            case (Status.NOTCHANGED):
                                break
                            case (Status.ADDED):
                            case (Status.CHANGED):
                                if (!inputFile.isDirectory() && inputFile.name.endsWith(SdkConstants.DOT_CLASS)) {
                                    def out = toOutputFile(dirOutput, dirInput, inputFile)
                                    directoryTransform(inputFile, out)
                                }
                                break
                            case (Status.REMOVED):
                                def outputFile = toOutputFile(outputDir, inputDir, inputFile)
                                FileUtils.deleteIfExists(outputFile)
                                break
                            default:
                                break
                        }
                    }
                } else {
                    println "directoryInput File:" + dirInput
                    dirInput.eachFileRecurse {
                        directoryTransform(it)
                    }
                    FileUtils.copyDirectory(dirInput, dirOutput)
                }
            }
        }
    }

    // jar transform
    private static def jarTransform(File input, File output) {
        if (!output.parentFile.exists()) {
            output.parentFile.mkdirs()
        }
        FileUtils.copyFile(input, output)
    }

    // directory transform
    private static def directoryTransform(File input) {
        fileDoTransform(input)
    }

    private static def fileDoTransform(File it) {
        // 获取class文件
        if (it.absolutePath.endsWith(SdkConstants.DOT_CLASS)) {
            println "directoryInput File Start:" + it
            def classReader = new ClassReader(it.bytes)
            def classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
            def classVisitor = new ClickClassVisitor(Opcodes.ASM9, classWriter)
            // 代码插装
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
            def bytes = classWriter.toByteArray()
            def outputStream = new FileOutputStream(it.path)
            outputStream.write(bytes)
            outputStream.close()
            println "directoryInput File End:" + it
        }
    }

    private static File toOutputFile(File outputDir, File inputDir, File inputFile) {
        return new File(outputDir, FileUtils.relativePossiblyNonExistingPath(inputFile, inputDir))
    }

    @Override
    String getName() {
        return "MyTransform"
    }

    /**
     * 输入文件类型，CLASSES, RESOURCES
     * {@link TransformManager}
     * CONTENT_CLASS = ImmutableSet.of(CLASSES)
     * CONTENT_JARS = ImmutableSet.of(CLASSES, RESOURCES)
     * CONTENT_RESOURCES = ImmutableSet.of(RESOURCES)
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getReferencedScopes() {
        return super.getReferencedScopes()
    }
/**
 * 作用域
 * @return
 */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.PROJECT_ONLY
    }


    /**
     * 增量编译
     * @return
     */
    @Override
    boolean isIncremental() {
        return false
    }
}