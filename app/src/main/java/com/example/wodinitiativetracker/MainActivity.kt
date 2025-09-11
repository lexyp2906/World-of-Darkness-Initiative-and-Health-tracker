package com.example.wodinitiativetracker

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.collection.intIntMapOf
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.wodinitiativetracker.ui.theme.WoDInitiativeTrackerTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Greeting()
            }
        }
}

@Composable
//crea il bottone che inserisce le creature
fun AddInitiativeButton(onIncrement:() -> Unit){
    Column {
        Button(
            onClick = { onIncrement() },
            modifier = Modifier.fillMaxWidth())
        {
            Text("Insert Creature")
        }
    }
}

@Composable
fun Greeting() {
    var initiative by remember { mutableStateOf(0) }
    //box che contiene tutto
    Box(modifier = Modifier.fillMaxSize()){
        //colonna di sotto col bottone
        Column(modifier = Modifier.align(Alignment.BottomCenter).padding(32.dp)){
            AddInitiativeButton({initiative++})
        }
        //colonna di sopra col titolo
        Column(modifier = Modifier
            .align(Alignment.TopCenter)
            .background(Color.LightGray)
            .fillMaxWidth()
            .padding(24.dp)){
            Text(text = "World of Darkness Health and Initiative tracker")
            Text(text = "$initiative")
        }
    }
}



