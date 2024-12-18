import com.google.protobuf.gradle.id

plugins {
    id("java")
    id("com.google.protobuf") version "0.9.4"
}

group = "com.king.vertx"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(enforcedPlatform("io.grpc:grpc-bom:1.69.0"))
    implementation(platform("io.vertx:vertx-stack-depchain:5.0.0.CR3"))

    implementation("io.vertx:vertx-core")
    implementation("io.vertx:vertx-web")
    implementation("io.vertx:vertx-grpcio-server")
    implementation("io.vertx:vertx-grpcio-client")
    // TODO -> WORKS WITHOUT THIS / FAILS WITH THIS
    implementation("io.vertx:vertx-grpcio-context-storage")
    implementation("io.grpc:grpc-api")
    implementation("io.grpc:grpc-protobuf")
    implementation("io.grpc:grpc-stub")

    // required - this is going to be fixed soon in grpc
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")

    runtimeOnly("io.grpc:grpc-netty-shaded")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.27.2"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.69.0"
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
            }
        }
    }

}