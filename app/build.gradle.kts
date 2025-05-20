plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.appghichu"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.appghichu"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures.viewBinding = true

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //SmoothBottomBar
    implementation("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")

    //lottie
    implementation(libs.lottie)

    //viewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //livedata
    implementation(libs.androidx.lifecycle.livedata.ktx)

    //annotation processor
    implementation(libs.androidx.lifecycle.common.java8)

    //room database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //coroutines
    implementation(libs.kotlinx.coroutines.android)

    //color picker library
    implementation(libs.spectrum)

    //markdown
    implementation(libs.markdownedittext)
    implementation(libs.core)
    implementation(libs.ext.strikethrough)
    implementation(libs.ext.tasklist)

    //animatoo
    implementation(libs.animatoo)

    implementation(libs.gson)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //okhttp
    implementation(libs.okhttp)

    //gson
    implementation(libs.gson)

    //BlurView
    implementation(libs.blurview)

    implementation(libs.glide)

    implementation("com.tbuonomo:dotsindicator:5.1.0")

    implementation ("com.google.android.gms:play-services-ads:22.1.0")

    implementation ("androidx.lifecycle:lifecycle-process:2.7.0")
}