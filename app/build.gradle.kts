plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}

android {
    namespace = "com.jmgtumat.pacapps"
    compileSdk = 36
    buildToolsVersion = "35.0.0 rc4"

    defaultConfig {
        applicationId = "com.jmgtumat.pacapps"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    flavorDimensions += "env"
    productFlavors {
        create("dev") { // <-- Use create("flavorName")
            dimension = "env"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        create("prod") { // <-- Use create("flavorName")
            dimension = "env"
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // AndroidX Core y Lifecycle
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.0")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-android:2.9.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.8.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2025.05.01"))
    implementation("androidx.compose.ui:ui:1.8.2")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.9.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("com.android.car.ui:car-ui-lib:2.6.0")
    implementation("androidx.wear.compose:compose-material:1.4.1")
    implementation("androidx.test:monitor:1.7.2")
    implementation("androidx.test.ext:junit-ktx:1.2.1")
    implementation("androidx.room:room-ktx:2.7.1")
    implementation("com.google.firebase:firebase-firestore-ktx:25.1.4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.8.2")

    // Firebase y Google Services
    implementation(platform("com.google.firebase:firebase-bom:33.14.0"))
    implementation("com.google.firebase:firebase-auth-ktx:23.2.1")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics:19.4.3")
    implementation("com.google.firebase:firebase-config-ktx")

    // Test y Pruebas
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation("org.mockito:mockito-inline:3.11.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Material
    implementation("com.google.android.material:material:1.12.0")

    // MPAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}
