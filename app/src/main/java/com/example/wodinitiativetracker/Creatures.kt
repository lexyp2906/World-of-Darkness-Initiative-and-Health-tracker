package com.example.wodinitiativetracker

data class Creature(
    val id: Int,
    val name: String,
    val health: String,
    val initiative: String,
    var currentHealth: String = ""
)