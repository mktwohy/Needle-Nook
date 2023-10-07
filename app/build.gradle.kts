plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
    id("org.jlleitschuh.gradle.ktlint") version "11.6.0"
}

android {
    namespace = "com.mktwohy.needlenook"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mktwohy.needlenook"
        minSdk = 28
        targetSdk = 34
        versionCode = 3
        versionName = "0.3.0"
        setProperty("archivesBaseName", "needlenook-v$versionName")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packagingOptions {
        resources {
            exclude("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

ktlint {
    android.set(true)
    disabledRules.addAll(
        "trailing-comma-on-call-site",
        "trailing-comma-on-declaration-site"
    )
    version.set("0.48.0")
}

dependencies {

    // AndroidX
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.navigation:navigation-compose:2.7.3")
    
    // Jetpack Compose
    val composeUiVersion = "1.5.1"
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui:$composeUiVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeUiVersion")

    // Material Design
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("androidx.compose.material:material-icons-extended:1.5.1")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Shared Preferences
    implementation("androidx.preference:preference-ktx:1.2.1")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Compose Testing
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeUiVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeUiVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeUiVersion")

    // Room
    val roomVersion = "2.5.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Markdown
    implementation("org.jetbrains:markdown:0.5.0")
}

//val ktlintCheck = tasks.register<JavaExec>("ktlintCheck") {
//    group = LifecycleBasePlugin.VERIFICATION_GROUP
//    description = "Check Kotlin code style"
//    classpath = ktlint
//    mainClass.set("com.pinterest.ktlint.Main")
//    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
//    args(
//        "**/src/**/*.kt",
//        "**.kts",
//        "!**/build/**",
//    )
//}
//
//tasks.check {
//    dependsOn(ktlintCheck)
//}
//
//tasks.register<JavaExec>("ktlintFormat") {
//    group = LifecycleBasePlugin.VERIFICATION_GROUP
//    description = "Check Kotlin code style and format"
//    classpath = ktlint
//    mainClass.set("com.pinterest.ktlint.Main")
//    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
//    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
//    args(
//        "-F",
//        "**/src/**/*.kt",
//        "**.kts",
//        "!**/build/**",
//    )
//}
