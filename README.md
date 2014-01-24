GradleCurse
===========

GradleCurse is a gradle plugin which allows you to push build artifacts to CurseForge and dev.bukkit.org.

How to use
----------
**Note**: *This Gradle plugin is still in beta which means that it might be unstable or not working at all - please file a bug report including the error/exception if you experience any issues. Furthermore, some features are incomplete or not well documented. Please visit: http://www.curseforge.com/wiki/projects/uploading-through-a-script/ or check out the source to see which upload parameters are available/what the parameters mean. A full documentation will be published once the plugin in stable.*

Clone this repository and run `gradle publishToMavenLocal` to install this plugin locally (I'm working on getting it into maven central)

```Bash
git clone https://github.com/Monofraps/GradleCurse.git
cd GradleCurse
gralde publishToMavenLocal
```

Now that you have GradleCurse installed locally, you can use it in your project's build script.

```Groovy
buildscript {
    repositories {
            mavenLocal()
            mavenCentral()
    }
    dependencies {
        classpath group: 'net.monofraps', name: 'GradleCurse', version: '1.0'
    }
}

apply plugin: 'gradle-curse'

task uploadArtifact(type: net.monofraps.gradlecurse.tasks.CurseDeployTask) {
    gameHandle = "wow"
    projectName = "my-project"
    apiKey = "ThisKeyMustRemainSecret"
    uploadFileName = "ThisIsTheNameTheUploadedFileWillHave.zip"
    changeLog = "Enter whatever you want."
    gameVersions = ["1", "2", "3"] // TODO: explain this parameter
    sourceObject = "build/dist/MyCoolArtifact.zip"
}

```
If you run `gradle uploadArtifact` now the plugin should upload to your project's Curse page.
