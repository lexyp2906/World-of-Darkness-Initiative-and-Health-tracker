package com.example.wodinitiativetracker

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class InsertViewModel : ViewModel() {
    //mantiene nel viewmodel i valori default di queste variabili così non vengono persi quando
    //si gira lo schermo
    var showDialog = mutableStateOf(false)
    val creatureName = mutableStateOf("")
    val creatureHealth = mutableStateOf("")
    val creatureInitiative = mutableStateOf("")
    fun onNameChanged(creatureName: String){
        this.creatureName.value = creatureName
    }
    //queste funzioni aggiornano i valori nel viewmodel
    fun onHealthChanged(creatureHealth: String){
        this.creatureHealth.value = creatureHealth
    }
    fun onInitiativeChanged(creatureInitiative: String){
        this.creatureInitiative.value = creatureInitiative
    }
    fun DialogToggled(showDialog: Boolean){
        this.showDialog.value = showDialog
    }
}
