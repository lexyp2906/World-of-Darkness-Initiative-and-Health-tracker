package com.example.wodinitiativetracker

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class InsertViewModel : ViewModel() {
    //mantiene nel viewmodel i valori default di queste variabili così non vengono persi quando
    //si gira lo schermo
    var showDialog = mutableStateOf(false)
    val creatureName = mutableStateOf("")
    val creatureHealth = mutableStateOf("")
    val creatureInitiative = mutableStateOf("")
    val warning = mutableStateOf("")
    val isWarningShown = mutableStateOf(false)
    val warningTrigger = mutableStateOf(0)

    //val creatureNumber = mutableStateOf(0)

    val addedCreaturesList = mutableStateListOf<Creature>()
    fun showWarning(message: String){
        warning.value = message
        isWarningShown.value = true
        warningTrigger.value++
    }
    fun hideWarning(){
        warning.value = ""
        isWarningShown.value = false
    }
    fun onNameChanged(creatureName: String){
        this.creatureName.value = creatureName
    }
    //queste funzioni aggiornano i valori nel viewmodel
    fun onHealthChanged(creatureHealth: String){
        this.creatureHealth.value = creatureHealth.filter { it.isDigit() }
    }
    fun onInitiativeChanged(creatureInitiative: String){
        this.creatureInitiative.value = creatureInitiative.filter { it.isDigit() }
    }
    fun DialogToggled(showDialog: Boolean){
        this.showDialog.value = showDialog
    }
}
