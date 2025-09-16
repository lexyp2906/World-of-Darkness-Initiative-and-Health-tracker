package com.example.wodinitiativetracker

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.util.UUID


data class Creature(
    val id: UUID,
    val name: String,
    val health: String,
    val initiative: String,
    var currentHealth: String = ""
)





