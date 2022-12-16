package com.cross.chakravyuh

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cross.chakravyuh.ui.theme.CrossChakravyuhTheme

private const val TAG = "MainActivity"
enum class State { InProgress, Loss, Win }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var gameState = remember { mutableStateOf(State.InProgress) }
            CrossChakravyuhTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BallsRevolving(4, 1, gameState)
                }
            }
            observeGameState(gameState)
        }
    }

    private fun observeGameState(gameState: MutableState<State>) {
        if(gameState.value == State.Loss){
            Log.d(TAG, "You Loss")
        } else if(gameState.value == State.Win){
            Log.d(TAG, "You Win")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CrossChakravyuhTheme {
        var gameState = remember { mutableStateOf(State.InProgress) }
        BallsRevolving(4, 1, gameState)
    }
}