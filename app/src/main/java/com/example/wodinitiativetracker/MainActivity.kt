package com.example.wodinitiativetracker

import android.R.attr.text
import android.R.attr.value
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay


open class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
                                    onValueChange = { viewModel.onNameChanged(it)},
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
                                    onValueChange = { viewModel.onHealthChanged(it) })
                                Spacer(modifier = Modifier.padding(15.dp))
                                Text("Creature Initiative")
                                OutlinedTextField(
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    value = creatureInitiative,
                                    onValueChange = { viewModel.onInitiativeChanged(it) })
                                Spacer(modifier = Modifier.padding(15.dp))

                                //the logic of the warning text.
                                if (viewModel.isWarningShown.value && viewModel.warning.value.isNotEmpty()){
                                    Text(warning, color = Color.Red)}
                                if (viewModel.isWarningShown.value){
                                    LaunchedEffect(key1 =viewModel.warningTrigger.value) {
                                        delay(3000L)
                                        viewModel.hideWarning()
                                    }
                                }

                                Row {
                                    Button(
                                        onClick = { onClose() },
                                        modifier = Modifier.padding(36.dp),
                                    ) {
                                        Text("Cancel")
                                    }
                                    Button(
                                        onClick = { onConfirmation() },
                                        modifier = Modifier.padding(36.dp),
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
                var showDialog = viewModel.showDialog.value
                if (showDialog) {
                    //Crea la finestra per inserire le creature, poi da opzione per
                    // chiudere la finestra e resettare le caselle di testo, o per confermare e aggiungere la creatura.
                    CreatureDialog(onClose = { viewModel.DialogToggled(false);
                        viewModel.onNameChanged("");
                        viewModel.onHealthChanged("");
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
                            viewModel.DialogToggled(false) }})}


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
            fun MainPage() {
                val creatures = viewModel.addedCreaturesList
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
                        Text(text = "World of Darkness Health and Initiative tracker")
                        //colonna che contiene la lista di creature inserite.
                        var scrollState = rememberScrollState()
                        Column (modifier = Modifier
                            .verticalScroll(scrollState)
                            .background(Color.White)
                            .statusBarsPadding()
                            .fillMaxWidth(),){
                            creatures.forEach{ creature ->
                                Text(text = "${creature.name}", fontSize = 20.sp) //LAVORA QUI!!!!!
                                //PENSO CHE LA LISTA DELLE CREATURE NON SI AGGIORNI QUANDO LE AGGIUNGI, ECCO PERCHè NON APPAIONO
                                HorizontalDivider(thickness = 2.dp)
                            }
                        }
                    }
                    //colonna di sotto col bottone
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .navigationBarsPadding()
                            .padding(32.dp)
                    ) {
                        AddCreatureButton()

                    }
                }
            }

            MainPage()
        }

    }
}



