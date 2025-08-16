plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "app.compile"
    compileSdk = 35
    buildToolsVersion = "35.0.0"
    ndkVersion = "28.1.13356709"
    
    androidResources {
         noCompress.add(".so")
       // noCompress.addAll(listOf(".so", "adb", "fastboot", "dmtracedump", "magiskboot"))
      //  generateLocaleConfig = true
    }
    
    sourceSets {
        getByName("main") {
            jniLibs {
                srcDirs("libs")
            }
        }
    }
    
    packaging {
        jniLibs {
            useLegacyPackaging = false
        }
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
    defaultConfig {
        applicationId = "XiaoKitty.HyperOS.Class"
        minSdk = 27
        targetSdk = 35
        versionCode = 91
        versionName = "v0.13.8-202508"
        
        buildConfigField("int", "TARGET_SDK_VERSION", targetSdk.toString())
        buildConfigField("int", "MIN_SDK_VERSION", minSdk.toString())
        buildConfigField("int", "COMPILE_SDK_VERSION", compileSdk.toString())
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        vectorDrawables { 
            useSupportLibrary = true
        }
        
        externalNativeBuild {
            cmake {
                abiFilters("arm64-v8a", "armeabi-v7a", "x86", "x86_64", "riscv64")
                version = "3.22.1"
            }
        }
    }
    
    externalNativeBuild {
        cmake {
            // CMakeLists.txt 
            path("src/main/cpp/CMakeLists.txt")
            
        }
        
    }
    
    signingConfigs {
        create("url") {
        // keystore file，.bks & .jks
            storeFile = file("keystore/kspace.keystore")
            storePassword = findProperty("KSPACE_KEY_PASSWORD") as String
            keyAlias = findProperty("KSPACE_KEY_ALIAS") as String
            keyPassword = findProperty("KSPACE_KEY_KEYPASSWORD") as String
            
            enableV1Signing = false
            enableV2Signing = true
            enableV3Signing = true
            enableV4Signing = true
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("url")
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            signingConfig = signingConfigs.getByName("url")
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        aidl = true
        buildConfig = true
    }
    
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
    
    lint {
        htmlReport = true
        xmlReport = true
        textReport = true
        sarifReport = true
       // disable("ProtectedPermissions")
    }
    
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    
}

tasks
    .withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>()
    .configureEach {
        compilerOptions
            .jvmTarget
            .set(
                org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
            )
    }

dependencies {
    val lifecycle_version = "2.9.2"
    val navigation_version = "2.9.2"
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigation_version")
    implementation("androidx.navigation:navigation-ui-ktx:$navigation_version")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-process:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-service:$lifecycle_version")
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.activity:activity-ktx:1.10.1")
    implementation("androidx.fragment:fragment-ktx:1.8.8")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.7.3")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation(fileTree("configs") { include("*.jar") })
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("androidx.annotation:annotation-experimental:1.4.1")
    implementation("com.squareup.okio:okio:3.14.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
}