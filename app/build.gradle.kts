// app/build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

  id("com.google.firebase.crashlytics") // Optional: Crashlytics for analytics
  //  id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
      //id ("dagger.hilt.android.plugin") // For Hilt
}

android {
    namespace = "com.example.musicappui"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.musicappui"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Read the API key from gradle.properties
        buildConfigField("String", "API_KEY", "\"${project.properties["API_KEY"]}\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //  implementation ("com.google.dagger:hilt-android:2.44")
    //  kapt ("com.google.dagger:hilt-compiler:2.44")
    //  implementation ("androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03")
    //  kapt ("androidx.hilt:hilt-compiler:1.2.0")


    implementation("com.google.ai.client.generativeai:generativeai:0.6.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    // add the dependency for the Google AI client SDK for Android
    implementation ("com.google.android.gms:play-services-auth:20.7.0")

    implementation ("com.google.firebase:firebase-database:20.2.2")


    implementation ("androidx.compose.material:material-icons-extended:1.6.7")


    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("androidx.core:core-ktx:1.13.1")


    val nav_version = "2.7.5"
    val compose_version = "1.6.0-alpha08"
    implementation ("androidx.compose.ui:ui:$compose_version")
    implementation ("androidx.compose.foundation:foundation:$compose_version")
    implementation("androidx.navigation:navigation-compose:$nav_version")

    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")

    implementation ("io.coil-kt:coil-compose:2.4.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib: 1.6.10")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")

    implementation ("io.coil-kt:coil:2.5.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    implementation ("io.coil-kt:coil-compose:2.5.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")

    //fire
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    //implementation ("com.google.firebase:firebase-storage-ktx:21.0.1")




            implementation ("androidx.compose.runtime:runtime-livedata:1.6.7")


    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}