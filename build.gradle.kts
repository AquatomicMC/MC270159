plugins {
    val fabricLoomVersion: String by System.getProperties()
    id("net.fabricmc.fabric-loom") version fabricLoomVersion
}

val modVersion: String by project
val minecraftVersion: String by project
version = "$modVersion+$minecraftVersion"
val mavenGroup: String by project
group = mavenGroup

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang", "minecraft", minecraftVersion)
    val fabricLoaderVersion: String by project
    implementation("net.fabricmc", "fabric-loader", fabricLoaderVersion)
}

tasks {
    val javaVersion = JavaVersion.VERSION_25
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        options.release.set(javaVersion.toString().toInt())
    }
    jar { from("LICENSE") { rename { "${it}_${base.archivesName.get()}" } } }
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") { expand(mapOf("version" to project.version)) }
    }
}
