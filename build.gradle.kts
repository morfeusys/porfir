// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlinVersion = "1.3.50"

    extra.set("kotlinVersion", kotlinVersion)

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.android.tools.build:gradle:3.5.0")
        classpath("com.getkeepsafe.dexcount:dexcount-gradle-plugin:0.8.6")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
        classpath("org.jfrog.buildinfo:build-info-extractor-gradle:4.7.5")
    }
}

allprojects {

    repositories {
        mavenLocal()
        google()
        jcenter()
        mavenCentral()
        maven("https://kotlin.bintray.com/kotlinx")
        maven("https://dl.bintray.com/aimybox/aimybox-android-sdk/")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}