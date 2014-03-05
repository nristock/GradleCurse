GradleCurse
===========

GradleCurse is a gradle plugin which allows you to push build artifacts to CurseForge and dev.bukkit.org.

How to use
----------
**Note**: *This Gradle plugin is still in beta which means that it might be unstable or not working at all - please file a bug report including the error/exception if you experience any issues. Furthermore, some features are incomplete or not well documented. Please visit: http://www.curseforge.com/wiki/projects/uploading-through-a-script/ or check out the source to see which upload parameters are available/what the parameters mean. A full documentation will be published once the plugin in stable.*

The latest snapshot build of the GradleCurse plugin is available in the OSS Sonatype Snapshots Repository: https://oss.sonatype.org/content/groups/public
As soon as I have a stable version ready, it will also be available through Maven Central.

**gradle.build example**
```Groovy
buildscript {
    repositories {
            mavenCentral()
            maven {
                url 'https://oss.sonatype.org/content/groups/public'
            }
    }
    dependencies {
        classpath group: 'net.monofraps', name: 'GradleCurse', version: '1.0-SNAPSHOT'
    }
}

apply plugin: 'gradle-curse'

curseDeploy {
    gameHandle = 'wow'

    deployment {
        curseProjectName = 'my-project'
        apiKey = 'KeepThisKeySecret'
        uploadFileName = 'FileName-Beta.jar'
        changeLog = 'Something changed, I think...'
        file = 'build/distribution/artifact.jar'
        gameVersions = [1, 2, 3]
    }
}
```

If you run `gradle deployToCurse` now the plugin should upload to your project's Curse page.

Plugin Configuration
--------------------
* `curseDeploy`
 * `gameHandle` (required) - the short name of the game you're developing for (e.g. wow for World of Warcraft, rom for Runes of Magic)
 * `baseUrl` - the base URL to use when getting game version information and uploading. $gameHandle$ will be replaced with whatever gameHandle is set to.
 * `deployment`
  * `enabled` (defaults to true) - only enabled deployments are uploaded to Curse
  * `curseProjectName` (required) - the slug of your project
  * `uploadUrl` - the URL to use for uploading. $baseUrl$ will be replaced with whatever baseUrl is set to. $projectName$ will be replaced with whatever projectName is set to.
  * `apiKey` (required) - your CurseForge API key. Get your API key here: http://www.curseforge.com/home/api-key/
  * `uploadFileName` (defaults to $project.name$-$project.version$) - the name of the uploaded file. This will show up on the download page.
  * `changeLog` - the change log. This will show up on the download page.
  * `knownCaveats` - any known caveats. This will show up the download page.
  * `changeLogMarkup` - the markup used for the changeLog.
  * `caveatMarkup` - the markup used for knownCaveats.
  * `file` - the file to upload.
  * `fileType` - type of the file you want to upload. This will show up on the download page.
  * `gameVersions` - a list of 1 - 3 compatible game versions

**Available Markup Types**:
Markup types are defined by enum net.monofraps.gradlecurse.MarkupType

* PLAIN
* CREOLE
* MARKDOWN
* BBCODE
* SAFEHTML

**Available File Types**:
File types are defined by enum net.monofraps.gradlecurse.FileType

* RELEASE
* BETA
* ALPHA

Default Tasks
-------------
The GradleCurse plugin automatically creates two tasks.

* `showGameVersions` - shows the latest 3 versions of the game you're developing for.
* `deployToCurse` - uploads all (enabled) deployments defined in `curseDeploy` to Curse

How to deploy to dev.bukkit.org
-------------------------------
Since dev.bukkit.org is part of the Curse network you can use this plugin to publish Bukkit plugins as well. Simply set
`baseUrl` to `http://dev.bukkit.org`.

Contributors
------------
* Monofraps (Lead Developer)
* nekosune (Contributor)
