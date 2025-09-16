package com.example.wodinitiativetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import androidx.compose.foundation.lazy.items

open class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (!isTaskRoot) {
            finish()
            return
        }
        setContent {
            val viewModel: InsertViewModel by viewModels()
            @Composable
            fun CreatureDialog(onClose: () -> Unit, onConfirmation: () -> Unit) {
                Dialog(
                    onDismissRequest = onClose,
                    properties = DialogProperties(
                        usePlatformDefaultWidth = false,
                        decorFitsSystemWindows = false
                    )) {
                    val creatureName = viewModel.creatureName.value
                    val creatureHealth = viewModel.creatureHealth.value
                    val creatureInitiative = viewModel.creatureInitiative.value
                    val warning = viewModel.warning.value
                    val textMaxLength = 64
                    val numberMaxLength = 3
                    //first we put all the dialog in a box so that with the imepadding it can go up
                    //when keyboard is shown, then we create the card where the textfields will be
                    //then we put all the textfields in a column
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .padding(12.dp),
                        contentAlignment = Alignment.Center) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(430.dp),
                            shape = RoundedCornerShape(16.dp),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                            ) {
                                Text("Creature Name")
                                OutlinedTextField(
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    value = creatureName,
                                    onValueChange = {it ->
                                        if(it.length <= textMaxLength){
                                            viewModel.onNameChanged(it)}},
                                    modifier = Modifier.fillMaxWidth())
                                Spacer(modifier = Modifier.padding(15.dp))
                                Text("Creature Health (Optional)")
                                OutlinedTextField(
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    value = creatureHealth,
                                    onValueChange = {it ->
                                        if(it.length <= numberMaxLength){
                                            viewModel.onHealthChanged(it) }
                                    })
                                Spacer(modifier = Modifier.padding(15.dp))
                                Text("Creature Initiative")
                                OutlinedTextField(
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    value = creatureInitiative,
                                    onValueChange = {it ->
                                        if (it.length <= numberMaxLength){
                                            viewModel.onInitiativeChanged(it) }
                                    })
                                Spacer(modifier = Modifier.padding(15.dp))

                                //the logic of the warning text.
                                if (viewModel.isWarningShown.value && viewModel.warning.value.isNotEmpty()){
                                    Text(warning, color = Color.Red)}
                                if (viewModel.isWarningShown.value){
                                    LaunchedEffect(key1 =viewModel.warningTrigger.intValue) {
                                        delay(3000L)
                                        viewModel.hideWarning()
                                    }
                                }

                                Row {
                                    Button(
                                        onClick = { onClose() },
                                        modifier = Modifier.padding(12.dp),
                                    ) {
                                        Text("Cancel")
                                    }
                                    Button(
                                        onClick = { onConfirmation() },
                                        modifier = Modifier.padding(12.dp),
                                    ) {
                                        Text("Add Creature")
                                    }
                                }

                            }

                        }
                    }
                }
            }
            @Composable
            //crea il bottone che inserisce le creature
            fun AddCreatureButton(){
                val showDialog = viewModel.showDialog.value
                if (showDialog) {
                    //Crea la finestra per inserire le creature, poi da opzione per
                    // chiudere la finestra e resettare le caselle di testo, o per confermare e aggiungere la creatura.
                    CreatureDialog(onClose = { viewModel.DialogToggled(false)
                        viewModel.onNameChanged("")
                        viewModel.onHealthChanged("")
                        viewModel.onInitiativeChanged("")},
                        //conferma l'input
                        onConfirmation = {if(viewModel.creatureName.value.isEmpty() or (viewModel.creatureInitiative.value.isEmpty())){
                            viewModel.showWarning("cannot add a creature without name or initiative")}
                        else{
                            val newCreature = Creature(
                                id = viewModel.addedCreaturesList.size,
                                name = viewModel.creatureName.value,
                                health = viewModel.creatureHealth.value,
                                initiative = viewModel.creatureInitiative.value
                            )
                            viewModel.addedCreaturesList.add(newCreature)
                            viewModel.DialogToggled(false)
                            viewModel.onNameChanged("")
                            viewModel.onHealthChanged("")
                            viewModel.onInitiativeChanged("")}})}


                Column {
                    Button(
                        onClick = {viewModel.DialogToggled(true)},
                        modifier = Modifier.fillMaxWidth())
                    {
                        Text("Insert Creature")
                    }
                }
            }

            @Composable
            fun creaturesColumn () {
                val sortedItemsInDescendingOrder by remember(viewModel.addedCreaturesList) {
                    derivedStateOf {
                        viewModel.addedCreaturesList.sortedByDescending { creature -> creature.initiative.toInt() }
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .background(Color.White)
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .fillMaxWidth()
                ) {
                    items(
                        items = sortedItemsInDescendingOrder,
                        key = {creature -> creature.id}
                    ) { creature ->
                        //variabili per i cubi della vita
                        val updatedHealth = remember { mutableStateOf("") }
                        val healthCubes = StringBuilder(updatedHealth.value)

                        val numberMaxLength = 3

                        fun increaseBashing() {
                            if (healthCubes.isNotEmpty() and viewModel.damageDealt.value.isNotEmpty()) {
                                if (!(healthCubes.toString()
                                        .contains("☐")) and !(healthCubes.toString()
                                        .contains("◫")) and !(healthCubes.toString().contains("☒"))
                                ) {
                                    null
                                } else if (!(healthCubes.toString()
                                        .contains("☐")) and !(healthCubes.toString()
                                        .contains("◫")) and (healthCubes.toString().contains("☒"))
                                ) {
                                    healthCubes.replace(
                                        healthCubes.toString().indexOf("☒"),
                                        healthCubes.toString().indexOf("☒") + 1,
                                        "⧆"
                                    )
                                } else if (!(healthCubes.toString().contains("☐"))) {
                                    healthCubes.replace(
                                        healthCubes.toString().indexOf("◫"),
                                        healthCubes.toString().indexOf("◫") + 1,
                                        "☒"
                                    )
                                } else if ((healthCubes.toString().contains("☐"))) {
                                    healthCubes.replace(
                                        healthCubes.toString().indexOf("☐"),
                                        healthCubes.toString().indexOf("☐") + 1,
                                        "◫"
                                    )
                                }

                                updatedHealth.value = healthCubes.toString()
                            }
                        }

                        fun increaseLethal() {
                            if (healthCubes.isNotEmpty() and viewModel.damageDealt.value.isNotEmpty()) {
                                if (!(healthCubes.toString()
                                        .contains("☐")) and !(healthCubes.toString()
                                        .contains("◫")) and !(healthCubes.toString().contains("☒"))
                                ) {
                                    null
                                } else if (!(healthCubes.toString()
                                        .contains("☐")) and !(healthCubes.toString()
                                        .contains("◫")) and (healthCubes.toString().contains("☒"))
                                ) {
                                    healthCubes.replace(
                                        healthCubes.toString().indexOf("☒"),
                                        healthCubes.toString().indexOf("☒") + 1,
                                        "⧆"
                                    )
                                    healthCubes.setLength(creature.health.toInt())
                                }
//
                                else if ((healthCubes.toString().contains("◫"))) {
                                    healthCubes.replace(
                                        healthCubes.toString().indexOf("◫"),
                                        healthCubes.toString().indexOf("◫"),
                                        "☒"
                                    )
                                    healthCubes.setLength(creature.health.toInt())
                                } else if ((healthCubes.toString()
                                        .contains("☐")) and !(healthCubes.toString().contains("◫"))
                                ) {
                                    healthCubes.replace(
                                        healthCubes.toString().indexOf("☐"),
                                        healthCubes.toString().indexOf("☐") + 1,
                                        "☒"
                                    )
                                }
                                updatedHealth.value = healthCubes.toString()
                            }
                        }

                        fun increaseAggr() {
                            if (healthCubes.isNotEmpty() and viewModel.damageDealt.value.isNotEmpty()) {
                                if (!(healthCubes.toString()
                                        .contains("☐")) and !(healthCubes.toString()
                                        .contains("◫")) and !(healthCubes.toString().contains("☒"))
                                ) {
                                    null
                                } else if (!(healthCubes.toString()
                                        .contains("☐")) and !(healthCubes.toString()
                                        .contains("◫")) and (healthCubes.toString().contains("☒"))
                                ) {
                                    healthCubes.replace(
                                        healthCubes.toString().indexOf("☒"),
                                        healthCubes.toString().indexOf("☒") + 1,
                                        "⧆"
                                    )
                                } else if ((healthCubes.toString().contains("☒"))) {
                                    healthCubes.replace(
                                        healthCubes.toString().indexOf("☒"),
                                        healthCubes.toString().indexOf("☒"),
                                        "⧆"
                                    )
                                    healthCubes.setLength(creature.health.toInt())
                                } else if ((healthCubes.toString().contains("◫"))) {
                                    healthCubes.replace(
                                        healthCubes.toString().indexOf("◫"),
                                        healthCubes.toString().indexOf("◫"),
                                        "⧆"
                                    )
                                    healthCubes.setLength(creature.health.toInt())
                                } else if ((healthCubes.toString()
                                        .contains("☐")) and !(healthCubes.toString().contains("◫"))
                                ) {
                                    healthCubes.replace(
                                        healthCubes.toString().indexOf("☐"),
                                        healthCubes.toString().indexOf("☐") + 1,
                                        "⧆"
                                    )
                                }

                                updatedHealth.value = healthCubes.toString()
                            }
                        }
                        val keyboardController = LocalSoftwareKeyboardController.current
                        val focusManager = LocalFocusManager.current
                        Box(modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd){
                            Button(
                                modifier = Modifier.padding(8.dp),
                                onClick = {
                                    viewModel.addedCreaturesList.remove(creature)
                                    }) {
                                Text("Delete creature")
                            }
                        }
                        Text(text = "Name: ${creature.name}", fontSize = 20.sp)
                        Text(text = "Initiative: ${creature.initiative}", fontSize = 20.sp)
                        if (creature.health.isEmpty()) {
                            Text(text = "Health: ", fontSize = 20.sp)
                        } else {
                            //impedisce al for loop di ripetersi ogni volta che premi un bottone
                            if (healthCubes.toString() == "") {
                                //determina quanti cubi di vita ci sono
                                for (i in 0 until creature.health.toInt()) {
                                    //☐
                                    //☒
                                    //◫
                                    //⧆
                                    healthCubes.append("☐")
                                }

                            }
                            val shownHealth = healthCubes.toString()
                            Text(text = "Health: $shownHealth", fontSize = 20.sp)
                            TextField(
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Done,
                                    keyboardType = KeyboardType.Number
                                ),
                                value = viewModel.damageDealt.value,
                                onValueChange = { it ->
                                    if (it.length <= numberMaxLength) {
                                        viewModel.showDamageDealt(it)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().padding(4.dp),
                                placeholder = { Text("insert damage") })
                            Row {
                                Button(
                                    modifier = Modifier.padding(8.dp),
                                    onClick = {
                                        val numberOfTimes =
                                            viewModel.damageDealt.value.toIntOrNull() ?: 0
                                        if (numberOfTimes > 0) {
                                            repeat(numberOfTimes) {
                                                increaseBashing()
                                            }
                                        }
                                        viewModel.showDamageDealt("")
                                        keyboardController?.hide()
                                        focusManager.clearFocus()

                                    }) {
                                    Text("Bashing")
                                }
                                Button(
                                    modifier = Modifier.padding(8.dp),
                                    onClick = {
                                        val numberOfTimes =
                                            viewModel.damageDealt.value.toIntOrNull() ?: 0
                                        if (numberOfTimes > 0) {
                                            repeat(numberOfTimes) {
                                                increaseLethal()
                                            }
                                        }
                                        viewModel.showDamageDealt("")
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                    }) {
                                    Text("Lethal")
                                }
                                Button(
                                    modifier = Modifier.padding(8.dp),
                                    onClick = {
                                        val numberOfTimes =
                                            viewModel.damageDealt.value.toIntOrNull() ?: 0
                                        if (numberOfTimes > 0) {
                                            repeat(numberOfTimes) {
                                                increaseAggr()
                                            }
                                        }
                                        viewModel.showDamageDealt("")
                                        keyboardController?.hide()
                                        focusManager.clearFocus()
                                    }) {
                                    Text("Aggravated")
                                }
                            }
                        }

                        HorizontalDivider(thickness = 6.dp)

                    }
                }
            }

            @Composable
            fun MainPage() {
                //box che contiene tutto
                Box(modifier = Modifier.fillMaxSize()) {
                    //colonna di sopra col titolo
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .background(Color.LightGray)
                            .statusBarsPadding()
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "World of Darkness Health and Initiative tracker",
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic)
                        AddCreatureButton()
                        //colonna che contiene la lista di creature inserite.
                        creaturesColumn()


                    }

                }

            }

            MainPage()
        }

    }
}



