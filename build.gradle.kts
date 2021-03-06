plugins {
    id ("fabric-loom") version "0.2.7-SNAPSHOT"
}

version = properties["mod_version"] as String + "-mc1.14"
group = properties["maven_group"] as String

base {
    archivesBaseName = properties["archives_base_name"] as String
}

repositories {
    maven {
        url = uri("https://masa.dy.fi/maven")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${properties["minecraft_version"]}")
    mappings("net.fabricmc:yarn:${properties["yarn_mappings"]}:v2")
    modCompile("net.fabricmc:fabric-loader:${properties["loader_version"]}")
    modCompile("net.fabricmc.fabric-api:fabric-api-base:0.1.2+28f8190f42")
    modCompile(	"net.fabricmc.fabric-api:fabric-command-api-v1:1.0.1+b534aa0c42")

    modCompile("carpet:fabric-carpet:${properties["minecraft_version"]}-${properties["carpet_core_version"]}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.getByName<ProcessResources>("processResources"){
    inputs.property("version", project.version)

    from(sourceSets.main.get().resources.srcDirs) {
        include("fabric.mod.json")
        expand("version" to project.version)
    }

    from(sourceSets.main.get().resources.srcDirs) {
        exclude("fabric.mod.json")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}