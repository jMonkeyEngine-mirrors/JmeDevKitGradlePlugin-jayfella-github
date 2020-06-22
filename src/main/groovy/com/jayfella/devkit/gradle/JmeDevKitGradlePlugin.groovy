package com.jayfella.devkit.gradle

import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.internal.jvm.Jvm

class JmeDevKitGradlePlugin implements Plugin<Project> {

    Project project;

    @Override
    void apply(Project project) {
        this.project = project
        configureProject()
    }

    /**
     * Configures project
     */
    private void configureProject() {
        addPlugins()
        configureEncoding()
        addRepositories()
        configureTasks()
        configureDependencies()
    }

    /**
     * Adds all needed plugins
     */
    private void addPlugins() {
        project.with {
            plugins.with {
                apply('java')
                apply('application')
            }
        }
    }

    /**
     * Sets force encoding on compile to UTF-8
     */
    private void configureEncoding() {
        project.tasks.withType(JavaCompile) {
            options.encoding = 'UTF-8'
        }
    }

    /**
     * Adds needed repositories
     */
    private void addRepositories() {
        project.repositories {
            jcenter()
        }
    }

    /**
     * Adds tasks
     */
    private void configureTasks() {

        project.task("runSdk", type: JavaExec) {
            main = "com.jayfella.devkit.Main"
            classpath = project.sourceSets.main.runtimeClasspath
        }
    }

    private void configureDependencies() {

        // include the devkit as a dependency
        Dependency dep = project.dependencies.create("com.jayfella:jme-jfx-devkit:0.0.2")
        project.dependencies.add("runtimeOnly", dep);

        // if we're not using java 8 we must include javafx
        if (Jvm.current().javaVersion > JavaVersion.VERSION_1_8) {

            project.with {
                plugins.with {
                    apply({ 'org.openjfx.javafxplugin:0.0.8' })
                }
            }

            Dependency jfxDep = project.dependencies.create("org.openjfx:javafx:14.0.1")
            project.dependencies.add("implementation", jfxDep)
        }

    }

}
