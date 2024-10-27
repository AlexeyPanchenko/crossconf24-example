package ru.punchman.crossconf24.example.data

import ru.punchman.crossconf24.example.controller.Release

// Stub implementation with random result
class ReleaseStorage(
    // private val db: DbDriver
) {

    private val cache = HashMap<String, HashMap<String, Release>>()

    suspend fun createRelease(appId: String, release: Release) {
        val random = (0 .. 100).random()
        when (random) {
            in 0 .. 80 -> {
                val appVersions = cache[appId] ?: HashMap()
                appVersions[release.version] = release
                cache[appId] = appVersions
            }
            else -> throw DbException()
        }
    }

    suspend fun getRelease(appId: String, version: String): Release? {
        val release = cache[appId]?.get(version)
        if (release != null) {
            return release
        }
        val random = (0 .. 100).random()
        return when (random) {
            in 0 .. 80 -> null
            else -> throw DbException()
        }
    }
}

class DbException() : RuntimeException("Database connection error")
