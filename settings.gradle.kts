pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(uri("https://repository.map.naver.com/archive/maven"))
        maven(uri("https://devrepo.kakao.com/nexus/content/groups/public/"))
    }
}

rootProject.name = "petbulance"
include(":app")
include(":presentation")
include(":domain")
include(":data")
