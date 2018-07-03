package com.mobilesolutionworks.gradle.swift.tasks.carthage

import junit5.assertMany
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIf
import org.junit.jupiter.api.extension.ExtendWith
import testKit.GradleRunnerProvider
import testKit.newFile


@ExtendWith(GradleRunnerProvider::class)
@DisplayName("Test CarthageBootstrap")
class CarthageBootstrapTests {

    @Test
    @DisplayName("verify carthageBootstrap")
    fun test1(runner: GradleRunner) {
        runner.newFile("settings.gradle.kts").writeText("""
        """.trimIndent())

        val build = runner.newFile("build.gradle.kts")
        build.writeText("""
            plugins {
                id("com.mobilesolutionworks.gradle.athena")
            }

            rome {
                enabled = false
            }

            carthage {
                github("yunarta/NullFramework") version "1.0.0"
            }
        """.trimIndent())

        runner.withArguments("carthageBootstrap")
                .build().let {
                    assertMany {
                        TaskOutcome.SUCCESS expectedFrom it.task(":carthageBootstrap")?.outcome
                    }
                }
    }

    @Test
    @DisplayName("verify carthageBootstrap incremental build")
    fun test2(runner: GradleRunner) {
        runner.newFile("settings.gradle.kts").writeText("""
        """.trimIndent())

        val build = runner.newFile("build.gradle.kts")
        build.writeText("""
            plugins {
                id("com.mobilesolutionworks.gradle.athena")
            }

            rome {
                enabled = false
            }

            carthage {
                github("yunarta/NullFramework") version "1.0.0"
            }
        """.trimIndent())

        runner.withArguments("carthageBootstrap")
                .build().let {
                    assertMany {
                        TaskOutcome.SUCCESS expectedFrom it.task(":carthageBootstrap")?.outcome
                    }
                }

        runner.withArguments("carthageBootstrap")
                .build().let {
                    assertMany {
                        TaskOutcome.UP_TO_DATE expectedFrom it.task(":carthageBootstrap")?.outcome
                    }
                }
    }

    @Test
    @DisplayName("verify carthageBootstrap incremental build after modification")
    fun test3(runner: GradleRunner) {
        runner.newFile("settings.gradle.kts").writeText("""
        """.trimIndent())

        val build = runner.newFile("build.gradle.kts")
        build.writeText("""
            plugins {
                id("com.mobilesolutionworks.gradle.athena")
            }

            rome {
                enabled = false
            }

            carthage {
                github("yunarta/NullFramework") version "1.0.0"
            }
        """.trimIndent())

        runner.withArguments("carthageBootstrap")
                .build().let {
                    assertMany {
                        TaskOutcome.SUCCESS expectedFrom it.task(":carthageBootstrap")?.outcome
                    }
                }

        build.writeText("""
            plugins {
                id("com.mobilesolutionworks.gradle.athena")
            }

            rome {
                enabled = false
            }

            carthage {
                github("yunarta/NullFramework") compatible "1.1.0"
            }
        """.trimIndent())

        runner.withArguments("carthageBootstrap")
                .build().let {
                    assertMany {
                        TaskOutcome.SUCCESS expectedFrom it.task(":carthageBootstrap")?.outcome
                    }
                }
    }

    @Test
    @DisplayName("verify carthageBootstrap with different toolchain")
    @EnabledIf(value = arrayOf(
            "def out = new StringBuilder(), err = new StringBuilder()",
            "def proc = 'swift -version'.execute(['TOOLCHAINS=org.swift.4020170919'], new File('.'))",
            "proc.consumeProcessOutput(out, err)",
            "proc.waitForOrKill(1000)",
            "def token = out.toString() =~ /Apple Swift version (.*) \\((.*)\\)/",
            "token[0][1] == '4.0'"
    ), engine = "groovy")
    fun test4(runner: GradleRunner) {
        runner.newFile("settings.gradle.kts").writeText("""
        """.trimIndent())

        val build = runner.newFile("build.gradle.kts")
        build.writeText("""
            plugins {
                id("com.mobilesolutionworks.gradle.athena")
            }

            xcode {
                swiftToolchains = "org.swift.4020170919"
            }

            rome {
                enabled = false
            }

            carthage {
                github("yunarta/NullFramework") version "1.0.0"
            }
        """.trimIndent())

        runner.withArguments("carthageBootstrap", "-i")
                .build().let {
                    assertMany {
                        TaskOutcome.SUCCESS expectedFrom it.task(":carthageBootstrap")?.outcome
                    }
                }
    }
}