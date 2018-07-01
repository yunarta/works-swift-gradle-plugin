package com.mobilesolutionworks.gradle.swift.model

internal open class Artifactory {

    internal open class FileSpecs(val files: List<FileSpec>) {

        internal open class FileSpec(val pattern: String)
    }

    internal open class ResultSpec(val path: String) 
}