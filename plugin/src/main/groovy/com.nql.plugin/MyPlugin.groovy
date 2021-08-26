package com.nql.plugin


import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.getExtensions().create("myExt", MyExtension)
        // 编译的生命周期，afterEvaluate 表示在配置完成后，此时可以获取到配置的Extension
        project.afterEvaluate {
            def myExt = project.getExtensions().findByType(MyExtension)
            println("Hello from " + myExt.name + myExt.sur)
        }
        project.android.registerTransform(new MyTransform())

    }
}