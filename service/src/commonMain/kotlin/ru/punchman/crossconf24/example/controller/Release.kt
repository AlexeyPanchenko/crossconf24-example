package ru.punchman.crossconf24.example.controller

import kotlinx.serialization.Serializable

@Serializable
class Release(
    val id: String,
    val version: String,
    val status: ReleaseStatus
)

@Serializable
enum class ReleaseStatus {
    DRAFT, IN_PROGRESS, COMPLETED, HALTED
}
