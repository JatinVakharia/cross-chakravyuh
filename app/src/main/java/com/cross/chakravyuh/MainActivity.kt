package com.cross.chakravyuh

import android.content.Context
import android.content.SharedPreferences
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
enum class Behaviour { None, Retry, NextLevel }

lateinit var sharedPreferences: SharedPreferences
val levelList = getLevelObjects()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =  getSharedPreferences("Chakravyuh_Pref", Context.MODE_PRIVATE)
        setContent {
            startGame(levelList[sharedPreferences.getInt("user_level", 0)])
        }
    }

    @Composable
    fun startGame(level: Level) {
        Log.d(TAG, "Time1 : "+ System.currentTimeMillis())
        // Handles Win and Loss of game
        var gameState = remember { mutableStateOf(State.InProgress) }
        // Handles next level or try again of game
        var gameBehaviour = remember { mutableStateOf(Behaviour.None) }

        CrossChakravyuhTheme(darkTheme = true) {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Log.d(TAG, "Time2 : "+ System.currentTimeMillis())
                BallsRevolving(level, gameState)
            }
        }
        observeGameState(gameState, gameBehaviour)
        observeGameBehaviour(gameBehaviour)
    }

    @Composable
    private fun observeGameBehaviour(gameBehaviour: MutableState<Behaviour>) {
        if (gameBehaviour.value == Behaviour.Retry) {
            startGame(levelList[sharedPreferences.getInt("user_level", 0)])
        } else if (gameBehaviour.value == Behaviour.NextLevel) {
            startGame(levelList[sharedPreferences.getInt("user_level", 0)])
        }
    }

    @Composable
    private fun observeGameState(
        gameState: MutableState<State>,
        gameBehaviour: MutableState<Behaviour>
    ) {
        // Handles show and hide of dialog
        var dialogState = remember { mutableStateOf(true) }

        if (gameState.value == State.Loss) {
            Log.d(TAG, "You Loss")
            if (dialogState.value)
                createDialog(
                    openDialogCustom = dialogState,
                    State.Loss,
                    actionFunction = { tryAgainSameLevel(gameBehaviour) },
                    ::exitApp
                )
        } else if (gameState.value == State.Win) {
            Log.d(TAG, "You Win")
            if (dialogState.value)
                createDialog(
                    openDialogCustom = dialogState,
                    State.Win,
                    actionFunction = { moveToNextLevel(gameBehaviour) },
                    ::exitApp
                )
        }
    }

    private fun exitApp() {
        finish()
    }

    private fun moveToNextLevel(gameBehaviour: MutableState<Behaviour>) {
        clearData()
        // Increment level
        val userLevel = sharedPreferences.getInt("user_level", 0)
        if(userLevel < levelList.size - 1)
            sharedPreferences.edit().putInt("user_level", userLevel + 1).apply()

        // Move to next level
        gameBehaviour.value = Behaviour.NextLevel
    }

    private fun tryAgainSameLevel(gameBehaviour: MutableState<Behaviour>) {
        clearData()
        gameBehaviour.value = Behaviour.Retry
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CrossChakravyuhTheme {
        var gameState = remember { mutableStateOf(State.InProgress) }
        BallsRevolving(levelList[sharedPreferences.getInt("user_level", 0)], gameState)
    }
}