package com.example.data_retrofit.services

import co.infinum.retromock.BodyFactory
import java.io.File
import java.io.InputStream

class AssetsBodyFactory(private val basePath: String) : BodyFactory {
    override fun create(path: String): InputStream? {
        val file = File(basePath, path)
        return file.inputStream()
    }
}
