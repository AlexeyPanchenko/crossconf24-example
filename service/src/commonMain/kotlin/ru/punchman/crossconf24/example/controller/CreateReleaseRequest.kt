package ru.punchman.crossconf24.example.controller

import kotlinx.serialization.Serializable

@Serializable
class CreateReleaseRequest(
    val version: String,
    val files: List<String>
)