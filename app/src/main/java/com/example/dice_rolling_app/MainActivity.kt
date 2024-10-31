package com.example.dice_rolling_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.dice_rolling_app.ui.theme.Dice_Rolling_AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Dice_Rolling_AppTheme {
                // Main container with theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DiceRoller()
                }
            }
        }
    }
}

@Composable
fun DiceRoller() {
    var diceRoll by remember { mutableIntStateOf(1) }
    var score by remember { mutableIntStateOf(0) }
    var userGuess by remember { mutableIntStateOf(1) }
    var showDialog by remember { mutableStateOf(false) }
    var guessFeedback by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    val imageResource = when (diceRoll) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB2FEFA),
                        Color(0xFF0ED2F7)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title Text
        Text(
            text = "Dice Roller",
            fontSize = 55.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .shadow(4.dp, CircleShape) // Just using shadow for depth
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display the current score
        Text(
            text = "Score: $score",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Guessing Options
        Text(
            text = "Your Guess:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        // Row of buttons for guessing
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            for (i in 1..6) {
                Button(
                    onClick = { userGuess = i },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (userGuess == i) Color(0xFF009FFD) else Color.Gray,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(4.dp)
                        .width(50.dp)
                ) {
                    Text(
                        text = i.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card with the dice image
        Card(
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = imageResource),
                contentDescription = "Dice",
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale.value)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Roll button
        Button(
            onClick = {
                scope.launch {
                    scale.animateTo(
                        1.5f, // Scale up
                        animationSpec = tween(durationMillis = 150)
                    )
                    scale.animateTo(
                        1f, // Scale back
                        animationSpec = tween(durationMillis = 150)
                    )
                }

                val newRoll = (1..6).random()
                diceRoll = newRoll

                // Check if the guess is correct
                if (userGuess == newRoll) {
                    score += 1
                    guessFeedback = "Correct! You guessed $userGuess!"
                } else {
                    score -= 1
                    guessFeedback = "Wrong! You guessed $userGuess, but it was $newRoll."
                }

                showDialog = true
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF009FFD),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .padding(16.dp)
                .height(50.dp)
                .width(180.dp)
        ) {
            Text(
                text = "Roll Dice",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Win/Loss Dialog
        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = guessFeedback,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF009FFD),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF009FFD),
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "OK", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiceRollerPreview() {
    Dice_Rolling_AppTheme {
        DiceRoller()
    }
}
